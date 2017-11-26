package com.elevate.edw.sqlservercdc.web.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.elevate.edw.sqlservercdc.CDTController;
import com.elevate.edw.sqlservercdc.CDTException;
import com.elevate.edw.sqlservercdc.kafka.ReceiverController;
import com.elevate.edw.sqlservercdc.kafka.TableVersion;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
public class AppWebController {
	public static final Logger LOG = LogManager.getLogger(AppWebController.class);

	@Autowired
	private ApplicationContext ctx;

	@Bean
	public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
		jsonConverter.setObjectMapper(objectMapper);
		return jsonConverter;
	}

	@RequestMapping(value = "/listcdtconfigs", method = RequestMethod.GET)
	public Set<String> listCDTConfigs() {
		Map<String, CDTController> configs = ctx.getBeansOfType(CDTController.class);
		return configs.keySet();
	}

	@RequestMapping(value = "/browseallcdtstatus", method = RequestMethod.GET)
	public Map<String, Map> allProducerStatus() {
		Map<String, Map> ret = new HashMap<String, Map>();
		Map<String, CDTController> configs = ctx.getBeansOfType(CDTController.class);
		for (String s : configs.keySet()) {
			CDTController c = configs.get(s);
			ret.put(s, c.getStatus());
		}
		return ret;
	}

	@RequestMapping(value = "/cdtstatus", method = RequestMethod.GET)
	public Map cdtStatus(@RequestParam(value = "cdtcontroller", required = true) String controllername) {
		CDTController controller = (CDTController) ctx.getBean(controllername);

		if (controller != null)
			return controller.getStatus();
		return null;
	}

	@RequestMapping(value = "/rcvstatus", method = RequestMethod.GET)
	public Map rcvStatus(@RequestParam(value = "rcvcontroller", required = true) String controllername) {
		ReceiverController controller = (ReceiverController) ctx.getBean(controllername);

		if (controller != null)
			return controller.getStatus();
		return null;
	}

	@RequestMapping(value = "/listreceiverconfigs", method = RequestMethod.GET)
	public Set<String> listReceiverConfigs() {
		Map<String, ReceiverController> receivers = ctx.getBeansOfType(ReceiverController.class);
		return receivers.keySet();
	}

	@RequestMapping(value = "/setversionsforall", method = RequestMethod.GET)
	public boolean setAllTableVersionsForCDTController(
			@RequestParam(value = "cdtcontroller", required = true) String controllername,
			@RequestParam(value = "version", required = true) long version) throws CDTException {
		CDTController controller = (CDTController) ctx.getBean(controllername);
		controller.setAllTablesVersion(version);

		return true;
	}

	@RequestMapping(value = "/setversionfortable", method = RequestMethod.GET)
	public boolean setVersionForTable(@RequestParam(value = "cdtcontrolelr", required = true) String controllername,
			@RequestParam(value = "cdtcontrolelr", required = true) String tablename,
			@RequestParam(value = "version", required = true) long version) throws CDTException {
		CDTController controller = (CDTController) ctx.getBean(controllername);
		boolean ret = controller.setTableVersion(tablename, version);

		return ret;
	}

	@RequestMapping(value = "/startcdc", method = RequestMethod.GET)
	public Map<String,Object> startMasterControler(
			@RequestParam(value = "debug", required = false, defaultValue = "false") boolean debug,
			@RequestParam(value = "cdtcontroller", required = true) String controllername)
			throws CDTException, InterruptedException {
		CDTController controller = (CDTController) ctx.getBean(controllername);

		Map<String,Object> ret  = new HashMap<String,Object>();
		if (!controller.isRunning()) {
			LOG.info("Master controller is not running. Start it now, debug= " + debug);
			controller.startController();
			// controller.setDebug(debug);
			// controller.setShutdown(false);
			// ExecutorService executor = Executors.newSingleThreadExecutor();
			// Future<String> controllerfuture = executor.submit(controller);
			// ControllerThreadFutureContainer c =
			// ctx.getBean(ControllerThreadFutureContainer.class);
			// c.setFuture(controllerfuture);
			ret.put("msg","controller is started successfully");
		} else {
			ret.put("msg","controller is already running.");
		}
		ret.put("debug",controller.isDebug());
		ret.put("isRunning",controller.isRunning());
		return ret;
	}

	@RequestMapping(value = "/stopcdc", method = RequestMethod.GET)
	public Map<String,Object> shutdownController(@RequestParam(value = "cdtcontroller", required = true) String controllername)
			throws InterruptedException, ExecutionException, TimeoutException {
		CDTController controller = (CDTController) ctx.getBean(controllername);
		Map<String, Object> ret =new HashMap<String,Object>();
		if (controller.isRunning()) {
			controller.stopController();
			// controller.setShutdown(true);
			// ControllerThreadFutureContainer c =
			// ctx.getBean(ControllerThreadFutureContainer.class);
			// Future f = c.getFuture();
			// f.get(controller.get_SLEEPINTERVAL() * 5, TimeUnit.MILLISECONDS);
			ret.put("msg","Controller is successfully shutdown.");
		} else {
			ret.put("msg","Controller was not running");
		}
		List<TableVersion> status = controller.listVersionStoreStatus();
		ret.put("versions",status);
		return ret;
	}

	@RequestMapping(value = "/refreshtable", method = RequestMethod.GET)
	public String fullRefreshTable(@RequestParam(value = "cdtcontroller", required = true) String controllername,
			@RequestParam(value = "tablename", required = true) String tableName) {
		CDTController controller = (CDTController) ctx.getBean(controllername);
		try {
			if (tableName.contains("\""))

				return controller.fullRefresh(tableName, true);

			else
				return controller.fullRefresh(tableName, false);
		} catch (CDTException e) {
			return e.getMessage();
		}

	}

	@RequestMapping(value = "/shutdown", method = RequestMethod.GET)
	public void shutDown() {
		ConfigurableApplicationContext appctx = (ConfigurableApplicationContext) ctx;
		appctx.close();
	}

	@RequestMapping(value = "/startreceiver", method = RequestMethod.GET)
	public String startReceiver(@RequestParam(value = "rcvcontroller", required = true) String controllername) {
		ReceiverController rcontroller = (ReceiverController) ctx.getBean(controllername);
		return rcontroller.startReceiver();
	}

	@RequestMapping(value = "/stopreceiver", method = RequestMethod.GET)
	public String stopReceiver(@RequestParam(value = "rcvcontroller", required = true) String controllername) {
		ReceiverController rcontroller = (ReceiverController) ctx.getBean(controllername);
		return rcontroller.stopReceiver();
	}

	@RequestMapping(value = "/loglevel", method = RequestMethod.GET)
	public Object getLogLevel(@RequestParam(value = "class", required = true) String className,
			@RequestParam(value = "level", required = true) String level) {

		Configuration conf = ((LoggerContext) LogManager.getContext(false)).getConfiguration();
		conf.getLoggerConfig(className).setLevel(Level.getLevel(level));
		((LoggerContext) LogManager.getContext(false)).updateLoggers();

		LoggerContext lctx = (LoggerContext) LogManager.getContext(false);
		Configuration lconf = lctx.getConfiguration();
		Map<String, LoggerConfig> loggers = lconf.getLoggers();
		StringBuffer buf = new StringBuffer();
		for (String k : loggers.keySet()) {
			LoggerConfig c = loggers.get(k);
			buf.append(String.format("name = %s appender = %s level = %s <br/>%n", c.getName(),
					c.getAppenderRefs().get(0).getClass().getName(), c.getLevel()));

		}
		// Logger logger = LogManager.getLogger(className);
		// Configuration conf =
		// ((LoggerContext)LogManager.getContext(false)).getConfiguration();
		// conf.getLoggerConfig(className).setLevel(Level.getLevel(level));
		// ((LoggerContext)LogManager.getContext(false)).updateLoggers();
		// LOG.info("loggername %s , Log leve %s", logger.getName(),
		// logger.getLevel().toString());
		return buf.toString();
	}

	@RequestMapping(value = "/cdtsleepinterval", method = RequestMethod.GET)
	public String cdtSleepInterval(@RequestParam(value = "cdtcontroller", required = true) String controllername,
			@RequestParam(value = "interval", required = true) long interval) {
		CDTController controller = (CDTController) ctx.getBean(controllername);
		return controller.cdtSleepInterval(interval);

	}
	@RequestMapping(value = "/rcvsleepinterval", method = RequestMethod.GET)
	public String rcvSleepInterval(@RequestParam(value = "rcvcontroller", required = true) String controllername,
			@RequestParam(value = "interval", required = true) long interval) {
		ReceiverController rcontroller = (ReceiverController) ctx.getBean(controllername);
		return rcontroller.rcvSleepInterval(interval);

	}
}
