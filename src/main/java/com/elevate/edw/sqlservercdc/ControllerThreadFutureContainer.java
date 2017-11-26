package com.elevate.edw.sqlservercdc;

import java.util.concurrent.Future;

public class ControllerThreadFutureContainer {
	private Future future;
	
	public void setFuture(Future future){
		this.future = future;
	}
	
	public Future getFuture(){
		return this.future;
	}

}
