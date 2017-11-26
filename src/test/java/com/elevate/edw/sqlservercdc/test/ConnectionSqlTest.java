package com.elevate.edw.sqlservercdc.test;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.elevate.edw.sqlservercdc.CDTCallable;
import com.elevate.edw.sqlservercdc.CDTConfiguration;
import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.metamodel.Table;
import com.elevate.edw.sqlservercdc.util.MetaUtil;
import com.elevate.edw.sqlservercdc.writer.DataSetWriter;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableAutoConfiguration
@Configuration
@ImportResource({ "file:///${cdt.app.config.path}/app_main.xml" })
public class ConnectionSqlTest extends AbstractTest implements BeanPostProcessor{
	@Test
	public void elasticCallableTest() throws CDTException{
		ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
		CDTConfiguration cdtConfig = (CDTConfiguration) ctx.getBean("elasticnls.cdt.config");
		MetaUtil metaUtil = new MetaUtil(cdtConfig.getTransDs(), cdtConfig.getTableList());
		Map<String, Table> tableMeta = metaUtil.getSourceMeta();
		Table t = tableMeta.get(tableMeta.keySet().iterator().next());
		CDTCallable wk = new CDTCallable();
		wk.setChangeds(cdtConfig.getTransDs());
		wk.setDatads(cdtConfig.getReadDs());
		wk.setMetaHelper(metaUtil);
		wk.setSender(cdtConfig.getSender());
		wk.setTable(t);
		wk.setTopic(cdtConfig.getKafkatopic());
		wk.setLastSyncVersion(cdtConfig.getVersionStore().retrieveVersion(t));
		assert(true);
		singleThreadExecutor.submit(wk);
		try {
		Thread.sleep(15000);
		} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
	}
//

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}
	@BeforeClass
	public static void beforeClass() {
		System.setProperty("cdt.app.config.path", "C:/Users/mmuralidhar/Desktop/sqlservercdc/sqlservercdc/config/stg");
	}
}
