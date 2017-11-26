package com.elevate.edw.sqlservercdc.writer.hbase.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import com.elevate.edw.sqlservercdc.CDTConfiguration;
import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.metamodel.Column;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.util.MetaUtil;

@Configuration
@EnableAutoConfiguration
@ImportResource({ "file:///${cdt.app.config.path}/app_main.xml" })
public class HiveDDL {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(HiveDDL.class);
		app.setWebEnvironment(false);

		ApplicationContext appContext = app.run(args);

		String[] configprefixs = { "elasticnls", "risenls", "pdolive" };
		Map<String, String> hivePrefixLkup = new HashMap<String, String>();
		hivePrefixLkup.put("elasticnls", "ela");
		hivePrefixLkup.put("risenls", "rse");
		hivePrefixLkup.put("pdolive", "rsl");

		for (String prefix : configprefixs) {
			String cdtconfigname = prefix + ".cdt.config";
			String hbasewriterbean = prefix + ".hbasewriter.config";
			String hbasenamespace = (String) ((Map) appContext.getBean(hbasewriterbean)).get("writer.hbase.namespace");
			String hivetableschema = "${DB}";
			String hivetableschema1 = "${SOURCE_DB}";
			String hiveviewschema = "${TARGET_DB}";
			String hivetableddlfile = prefix + ".table.ddl";
			String hiveviewddlfile = prefix + ".view.ddl";
			String hbaseddlfile = prefix + ".hbase.ddl";
			String hiveTablePrefix = hivePrefixLkup.get(prefix);

			
			com.elevate.edw.sqlservercdc.CDTConfiguration cdtconfig = (CDTConfiguration) appContext
					.getBean(cdtconfigname);
			MetaUtil metaUtil = new MetaUtil(cdtconfig.getTransDs(), cdtconfig.getTableList());
			try {
				Map<String, Table> tables = metaUtil.getSourceMeta();
				try (PrintWriter hivetableout = new PrintWriter(new FileWriter(hivetableddlfile));
						PrintWriter hiveviewout = new PrintWriter(new FileWriter(hiveviewddlfile));
						PrintWriter hbaseout = new PrintWriter(new FileWriter(hbaseddlfile))) {
					hbaseout.println("create_namespace '" + hbasenamespace + "'");
					tables.forEach((name, table) -> {
						hivetableout
								.println(buildHiveDDL(name, table, hbasenamespace, hivetableschema, hiveTablePrefix));
						hiveviewout.println(
								buildHiveViewDDL(name, table, hivetableschema1, hiveviewschema, hiveTablePrefix));
						hbaseout.println("disable '" + hbasenamespace + ":" + table.getFullName(false) + "'");
						hbaseout.println("drop '" + hbasenamespace + ":" + table.getFullName(false) + "'");
						hbaseout.println("create '" + hbasenamespace + ":" + table.getFullName(false)
								+ "', {NAME => 'row_data', VERSIONS => '10', COMPRESSION => 'SNAPPY'},"
								+ "{NUMREGIONS => 40 , SPLITALGO => 'HexStringSplit'}");
						String ddlFileName = (hiveTablePrefix + "_" + table.getSchemaname() + "_" + table.getTablename()
								+ ".ddl").toLowerCase();
						try {
							PrintWriter tddlOut = new PrintWriter(new FileWriter("ddl/loan_mgmt_rt/" + ddlFileName));
							PrintWriter vddlOut = new PrintWriter(new FileWriter("ddl/loan_mgmt_rv/" + ddlFileName));
							tddlOut.println(buildHiveDDL(name, table, hbasenamespace, hivetableschema, hiveTablePrefix));
							vddlOut.println(buildHiveViewDDL(name, table, hivetableschema1, hiveviewschema, hiveTablePrefix));
							tddlOut.close();
							vddlOut.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (CDTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static Object buildHiveViewDDL(String name, Table table, String hiveTableSchema, String hiveviewschema,
			String hiveTablePrefix) {
		String hiveTableName = hiveTablePrefix + "_" + table.getSchemaname() + "_" + table.getTablename();
		StringBuffer ddl = new StringBuffer();
		ddl.append("CREATE OR REPLACE VIEW " + hiveviewschema + "." + hiveTableName
				+ "\nAS\nSELECT rowkey,\nSYS_CHANGE_OPERATION,\n HBASE_UPD_TS\n");
		int idx = 0;
		for (Column c : table.getColumns()) {
			idx++;
			if (idx != table.getColumns().size())
				ddl.append("`" + c.getColumnName(false) + "`   ,\n");
			else
				ddl.append("`" + c.getColumnName(false) + "`   \n");
		}
		ddl.append("FROM " + hiveTableSchema + "." + hiveTableName + ";\n");

		return ddl.toString();
	}

	private static Object buildHiveDDL(String name, Table table, String hbaseNameSpace, String hiveSchema,
			String hiveTablePrefix) {
		String hiveTableName = hiveTablePrefix + "_" + table.getSchemaname() + "_" + table.getTablename();
		String hbaseTableName = hbaseNameSpace + ":" + table.getFullName(false);
		StringBuffer ddl = new StringBuffer();
		ddl.append("DROP TABLE IF EXISTS " + hiveSchema + "." + hiveTableName + " PURGE;\n");
		ddl.append("CREATE EXTERNAL TABLE " + hiveSchema + "." + hiveTableName + "(\n");
		ddl.append("rowkey String,\n");
		ddl.append("SYS_CHANGE_OPERATION String,\n");
		ddl.append("HBASE_UPD_TS String,\n");
		int idx = 0;
		for (Column c : table.getColumns()) {
			idx++;
			if (idx != table.getColumns().size())
				ddl.append("`" + c.getColumnName(false) + "`   String,\n");
			else
				ddl.append("`" + c.getColumnName(false) + "`   String\n");
		}
		ddl.append(
				")\nSTORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'\nWITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key,row_data:SYS_CHANGE_OPERATION,row_data:HBASE_UPD_TS,");
		idx = 0;
		for (Column c : table.getColumns()) {
			idx++;
			if (idx != table.getColumns().size())
				ddl.append("row_data:" + c.getColumnName(false).replaceAll("#", "\\\\#") + ",");
			else
				ddl.append("row_data:" + c.getColumnName(false).replaceAll("#", "\\\\#"));
		}
		ddl.append("')\nTBLPROPERTIES ('hbase.table.name' = '" + hbaseTableName + "');");

		return ddl.toString();
	}

}
