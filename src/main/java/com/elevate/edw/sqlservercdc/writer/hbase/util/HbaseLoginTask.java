package com.elevate.edw.sqlservercdc.writer.hbase.util;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Task to perform automatica relogin for kerberos
 * 
 * @author ywu
 *
 */
public class HbaseLoginTask implements Callable<Object> {
	private static final Logger LOG = LogManager.getLogger(HbaseLoginTask.class);
	// default 1/2 hr retry
	private long _SLEEPINTERVAL = 1000 * 10;
	private boolean shutdown = false;

	private String principal;
	private String keytab;
	UserGroupInformation ugi;

	public HbaseLoginTask(org.apache.hadoop.conf.Configuration config, String principal, String keytab)
			throws IOException {
		this.principal = principal;
		this.keytab = keytab;
		UserGroupInformation.setConfiguration(config);
		ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, keytab);
		UserGroupInformation.setLoginUser(ugi);
	}

	@Override
	public Object call() throws Exception {
		shutdown = false;
		while (!shutdown) {
			LOG.debug("check tgt and relogin from keytab");
			
			ugi.checkTGTAndReloginFromKeytab();
			Thread.currentThread().sleep(_SLEEPINTERVAL);
		}
		return ugi;
	}

	public long get_SLEEPINTERVAL() {
		return _SLEEPINTERVAL;
	}

	public void set_SLEEPINTERVAL(long _SLEEPINTERVAL) {
		this._SLEEPINTERVAL = _SLEEPINTERVAL;
	}

	public boolean isShutdown() {
		return shutdown;
	}

	public void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getKeytab() {
		return keytab;
	}

	public void setKeytab(String keytab) {
		this.keytab = keytab;
	}

}
