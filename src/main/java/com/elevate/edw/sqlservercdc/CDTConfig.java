package com.elevate.edw.sqlservercdc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.elevate.edw.sqlservercdc.kafka.ReceiverConfig;
import com.elevate.edw.sqlservercdc.kafka.SenderConfig;
import com.elevate.edw.sqlservercdc.writer.hbase.HBaseConfig;

@Configuration

@Import({ SenderConfig.class, ReceiverConfig.class,HBaseConfig.class })
public class CDTConfig {
	public static final Logger LOG = LogManager.getLogger(CDTConfig.class);

//	@Autowired
//	@Qualifier("elasticCdcConfig")
//	CDCConfiguration config;
//
//	@Bean
//	public MetaUtil metaUtil() {
//		return new MetaUtil(config.getReadDs());
//	}
//
//	@Bean(name = "cdcTablesMeta")
//	public Map<String, Table> cdcTablesMeta() throws CdcException {
//		return metaUtil().getSourceMeta();
//	}
//
//	@Bean(name = "masterController")
//	public MasterController masterController() {
//		return new MasterController(config);
//	}
//
//	@Bean(name = "mastercontrollerfuture")
//	public ControllerThreadFutureContainer controllerThreadFutureContainer() {
//		return new ControllerThreadFutureContainer();
//	}
	
	
	
	
}
