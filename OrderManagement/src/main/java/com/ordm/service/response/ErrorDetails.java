/**
 * 
 */
package com.ordm.service.response;

/**
 * @author VIJAY
 *
 */
public class ErrorDetails {

	private String errorCode;

	private String errorDesc;
	
	public ErrorDetails(String errorCode,String errorDesc) {
		this.errorCode= errorCode;
		this.errorDesc = errorDesc;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

}
