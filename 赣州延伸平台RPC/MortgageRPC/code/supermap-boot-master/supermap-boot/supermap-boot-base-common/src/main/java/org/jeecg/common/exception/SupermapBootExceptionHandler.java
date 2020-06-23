package org.jeecg.common.exception;

public class SupermapBootExceptionHandler extends RuntimeException {

	public SupermapBootExceptionHandler(String message){
		super(message);
	}

	public SupermapBootExceptionHandler(Throwable cause)
	{
		super(cause);
	}

	public SupermapBootExceptionHandler(String message, Throwable cause)
	{
		super(message,cause);
	}
}
