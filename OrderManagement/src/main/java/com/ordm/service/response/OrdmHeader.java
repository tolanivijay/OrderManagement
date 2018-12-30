/**
 * 
 */
package com.ordm.service.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ordm.enums.ResponseStatus;

/**
 * @author VIJAY
 *
 */
public class OrdmHeader {
	
	private List<ErrorDetails> errorDetails = new ArrayList<ErrorDetails>();
	
	@JsonProperty
	public String getStatus() {
		return (CollectionUtils.isEmpty(errorDetails) ? ResponseStatus.SUCCESS.getStatus() : ResponseStatus.FAILURE.getStatus());
	}
	
	public void addToError(ErrorDetails error) {
		errorDetails.add(error);
	}

	public List<ErrorDetails> getErrorDetails() {
		return errorDetails;
	}

}
