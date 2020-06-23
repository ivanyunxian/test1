package org.jeecg.common.exception;

public class SupermapBootException extends RuntimeException {

	public SupermapBootException(String message){
		super(message);
	}

	public SupermapBootException(Throwable cause)
	{
		super(cause);
	}

	public SupermapBootException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
