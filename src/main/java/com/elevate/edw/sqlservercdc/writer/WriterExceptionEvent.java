package com.elevate.edw.sqlservercdc.writer;

import org.springframework.context.ApplicationEvent;

public class WriterExceptionEvent extends ApplicationEvent {

	public WriterExceptionEvent(Object source) {
		super(source);
	}

}
