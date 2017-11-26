package com.elevate.edw.sqlservercdc.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elevate.edw.sqlservercdc.CDTCallable;
import com.elevate.edw.sqlservercdc.CDTConfiguration;
import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.FullRefreshCallable;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.util.MetaUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@Configuration
@ImportResource({ "file:///${cdt.app.config.path}/app_main.xml" })

public class ValidateMetaDataTest extends AbstractTest {
	@Test
	public void elasticCallableTest() throws CDTException {
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		CDTConfiguration cdtConfig = (CDTConfiguration) ctx.getBean("risenls.cdt.config");
		MetaUtil metaUtil = new MetaUtil(cdtConfig.getReadDs(), cdtConfig.getTableList());
		Table t = metaUtil.getSourceMeta().get("DBO.LOANACCT");
		CDTCallable wk = new CDTCallable();
		wk.setPullInterval(cdtConfig.getPullInterval());
		wk.setChangeds(cdtConfig.getTransDs());
		wk.setDatads(cdtConfig.getReadDs());
		wk.setMetaHelper(metaUtil);
		wk.setSender(cdtConfig.getSender());
		wk.setTable(t);
		wk.setTopic(cdtConfig.getKafkatopic());
		wk.setLastSyncVersion(cdtConfig.getVersionStore().retrieveVersion(t));
		for (int i = 1; i <= 8; i++)
		{
			singleThreadExecutor.submit(wk);
			try {
				Thread.sleep(45000);
				} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
		}
	
		try {
		Thread.sleep(30000);
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("cdt.app.config.path", "C:/Users/mmuralidhar/Desktop/sqlservercdc/sqlservercdc/config/stg");
	}
}
