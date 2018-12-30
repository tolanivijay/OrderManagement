/**
 * 
 */
package com.ordm.enums;

/**
 * @author VIJAY
 *
 */
public enum ResponseStatus {
	
	FAILURE("Failure"),SUCCESS("Success");

	private String status;
	
	private ResponseStatus(String status) {
		this.status=status;
	}

	public String getStatus() {
		return status;
	}
}
