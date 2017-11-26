package com.elevate.edw.sqlservercdc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.elevate.edw.sqlservercdc.metamodel.Table;
/***
 * Database version store class implements versionStore<Table> 
 * this class provide facilities to retrieve/modify the last sync version for each 
 * table inside the cdc tracking. This class relies on a datasource instance which connect 
 * to a database which hosting a permanent table to store such version. 
 * The datasource is configurable through constructor. 
 * the tablename is also cnofigurable through constructor. 
 * the storekeystring is a string that represent this particular instance of version store. 
 * The table structure should be as following: 
 * MySQL [sqlcdc]> desc versionstore;
+-----------------+--------------+------+-----+----------------------+-------+
| Field           | Type         | Null | Key | Default              | Extra |
+-----------------+--------------+------+-----+----------------------+-------+
| storekey        | varchar(30)  | NO   | PRI | NULL                 |       |
| tablename       | varchar(300) | NO   | PRI | NULL                 |       |
| lastsyncversion | bigint(20)   | NO   |     | NULL                 |       |
| last_upd_ts     | timestamp(6) | NO   |     | CURRENT_TIMESTAMP(6) |       |
+-----------------+--------------+------+-----+----------------------+-------+


 * @author ywu
 *
 */
public class DatabaseVersionStore implements VersionStore<Table> {
	private DataSource ds;
	private String storeKeyString;
	private String storeTableName;
	private String insertSql;
	private String updateSql;
	private String retrieveSql;

	public DatabaseVersionStore(DataSource ds, String storeKeyString, String storeTableName) {
		this.ds = ds;
		this.storeKeyString = storeKeyString;
		this.setStoreTableName(storeTableName);
	}

	@Override
	public void storeVersion(Table table, Long version) throws CDTException {
		try (Connection con = ds.getConnection();
				PreparedStatement checkExsistingStmt = con.prepareStatement(retrieveSql);
				PreparedStatement instStmt = con.prepareStatement(insertSql);
				PreparedStatement updStmt = con.prepareStatement(updateSql)) {
			checkExsistingStmt.setString(1, this.storeKeyString);
			checkExsistingStmt.setString(2, table.getFullName(true));
			int rowCnt = 0;
			ResultSet rs = checkExsistingStmt.executeQuery();
			// if rs next reach end, there is no row , perform insert
			if (!rs.next()) {
				instStmt.setString(1, this.storeKeyString);
				instStmt.setString(2, table.getFullName(true));
				instStmt.setLong(3, version);
				rowCnt = instStmt.executeUpdate();
			} else {
				updStmt.setLong(1, version);
				updStmt.setString(2, this.storeKeyString);
				updStmt.setString(3, table.getFullName(true));
				rowCnt = updStmt.executeUpdate();
			}
			// if this happens... something is wrong!
			assert (rowCnt == 1);

		} catch (SQLException e) {
			throw new CDTException(e);
		}

	}

	@Override
	public Long retrieveVersion(Table table) throws CDTException {
		Long ret = null;
		try (Connection con = ds.getConnection();
				PreparedStatement selStmt = con.prepareStatement(this.retrieveSql)){
			selStmt.setString(1, this.storeKeyString);
			selStmt.setString(2, table.getFullName(true));
			ResultSet rs = selStmt.executeQuery();
			if(rs.next()){
				ret = rs.getLong(1);
			}else{
				// no record yet return zero 
				ret = 0L;
			}
			
		}catch (SQLException e) {
			throw new CDTException(e);
		}
		return ret;
	}

	public void setStoreTableName(String storeTableName) {
		this.storeTableName = storeTableName;

		this.insertSql = "INSERT INTO " + this.storeTableName
				+ "(storekey, tablename, lastsyncversion) VALUES (?,?,?);";
		this.updateSql = "UPDATE " + this.storeTableName
				+ " SET lastsyncversion = ? , last_upd_ts = current_timestamp(6) "+
				" WHERE storekey = ? AND tablename=?;";
		this.retrieveSql = "SELECT lastsyncversion FROM " + this.storeTableName
				+ "  WHERE storekey = ? AND tablename=?;";
	}

}
