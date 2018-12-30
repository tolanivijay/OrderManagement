/**
 * 
 */
package com.ordm.validator;

import org.springframework.stereotype.Component;

import com.ordm.model.BaseOrder;
import com.ordm.model.Execution;
import com.ordm.model.Instrument;
import com.ordm.model.OrderBook;
import com.ordm.service.response.OrdmResponse;
import com.ordm.staticdata.OrdmResponseErrorCodes;

/**
 * @author VIJAY
 *
 */
@Component
public class OrdmServiceValidator {
	
	
	
	public void performAddExecValidations(OrdmResponse<?> response,OrderBook book,Execution exec) {
		if (book.getIsExecuted()) {
			response.addToError(OrdmResponseErrorCodes.BOOK_ALREADY_EXECUTED);
		}
		if (book.getIsOpen()) {
			response.addToError(OrdmResponseErrorCodes.BOOK_OPEN_CANNOT_ADD_EXECUTION);
			
		}
		if (!book.canExecutionFitIn(exec)) {
			response.addToError(OrdmResponseErrorCodes.EXECUTION_QTY_EXECEEDS_DEMAND);
		}
	}
	
	
	
	

}
