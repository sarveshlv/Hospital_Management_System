package com.hms.bedms.exceptions;

public class BedStatusInvalidException extends RuntimeException {

	private static final long serialVersionUID = 8438425380489471368L;

	public BedStatusInvalidException(String bedStatus) {
		super("Bed staus invalid for request. Current bed status: " + bedStatus);
	}
	
}
