/**
 * 
 */
package com.ordm.staticdata;

/**
 * @author VIJAY
 *
 */
public enum OrdmResponseErrorCodes {
	
	GENERAL_ERROR("GE","General Error. Please contact admin"),
	BOOK_NOT_FOUND("BNF","Requested Book not available"),
	BOOK_CLOSED("BC", "Requested Book is closed. Cannot add any further orders"),
	BOOK_CLOSED_ALREADY("BCA", "Requested Book is already closed."),
	CANNOT_OPEN_BOOK("COB","Cannot open new book. Please retry or contact admin"),
	CANNOT_CLOSE_BOOK("CCB","Cannot close book. Please retyr or contact admin"),
	CANNOT_OPEN_BOOK_INST_NOT_FOUND("COBINF","Cannot open book as instrument provided is invalid"),
	CANNOT_ADD_ORDER_INST_MISMATCH("CAOIM","Invalid Instrument.. has to be the same as Book"),
	CANNOT_ADD_ORDER_BOOK_INVALID("CAOBI","Book provided does not exit.. Cannot add order"),
	EXECUTION_QTY_EXECEEDS_DEMAND("EQED","Execution quantity is greter than book demand.. Cannot add"),
	BOOK_ALREADY_EXECUTED("BAE","Book is already executed.. Cannot add"),
	BOOK_OPEN_CANNOT_ADD_EXECUTION("BOCAE","Book open cannot add execution"),
	ORDER_NOT_FOUND("ONF","Specified order not found");
	
	private final String errorCode;
	private final String errorDesc;
	
	
	private OrdmResponseErrorCodes(String errorCode,String errorDesc) {
		this.errorCode = errorCode;
		this.errorDesc=errorDesc;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public String getErrorDesc() {
		return errorDesc;
	}

}
