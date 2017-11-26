package com.elevate.edw.sqlservercdc.test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.security.UserGroupInformation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.elevate.edw.sqlservercdc.writer.hbase.HBaseConfig;
import com.elevate.edw.sqlservercdc.writer.hbase.HbaseWriter;

public class HbaseTest extends AbstractTest {
	private ApplicationContext ctx;

	@Configuration
	@PropertySource("classpath:test.properties")
	@ImportResource({ "classpath:app_main.xml" })
	public static class MyContextConfiguration {

	}
	@Test
	public void testHBaseConnection() throws IOException {
		org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();
		String hadoopconfigpath = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.basedir}");
		config.addResource(new Path(hadoopconfigpath + File.separator + "core-site.xml"));
		config.addResource(new Path(hadoopconfigpath + File.separator + "hdfs-site.xml"));
		config.addResource(new Path(hadoopconfigpath + File.separator + "hbase-site.xml"));

		UserGroupInformation.setConfiguration(config);
		String principal = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.login.principal}");
		String keytab = ((ConfigurableBeanFactory) ctx.getAutowireCapableBeanFactory())
				.resolveEmbeddedValue("${elasticnls.hbase.config.login.keytab}");

		System.out.println(hadoopconfigpath);
		System.out.println(principal);
		System.out.println(keytab);
		UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, keytab);
		UserGroupInformation.setLoginUser(ugi);

		org.apache.hadoop.hbase.client.Connection hcon = ConnectionFactory.createConnection(config);
		// ctx.getBean(org.apache.hadoop.hbase.client.Connection.class);

		Admin admin = hcon.getAdmin();
		HTableDescriptor[] tableDescriptor = admin.listTables();

		// printing all the table names.
		for (int i = 0; i < tableDescriptor.length; i++) {
			System.out.println(tableDescriptor[i].getNameAsString());
		}

	}
	
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;

	}

}
