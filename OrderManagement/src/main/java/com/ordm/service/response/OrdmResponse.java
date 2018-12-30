/**
 * 
 */
package com.ordm.service.response;

import org.springframework.util.CollectionUtils;

import com.ordm.staticdata.OrdmResponseErrorCodes;

/**
 * @author VIJAY
 *
 */
public class OrdmResponse<T> {

	private T body;
	
	private OrdmHeader header = new OrdmHeader();
	
	public boolean hasErrors() {
		if (!CollectionUtils.isEmpty(header.getErrorDetails())) {
			return true;
		}
		return false;
	}

	public OrdmHeader getHeader() {
		return header;
	}
	
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	
	public void addToError(OrdmResponseErrorCodes errorCode) {
		header.addToError(new ErrorDetails(errorCode.getErrorCode(),errorCode.getErrorDesc()));
	}
	
	
}
