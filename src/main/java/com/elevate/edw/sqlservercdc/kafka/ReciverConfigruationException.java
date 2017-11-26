package com.elevate.edw.sqlservercdc.kafka;

public class ReciverConfigruationException extends Exception {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9143518064671043022L;

	public ReciverConfigruationException(Exception p){
		super(p);
	}
	
	public ReciverConfigruationException(Throwable p){
		super(p);
	}
	
	public ReciverConfigruationException(String msg){
		super(msg);
	}

}
