/**
 * 
 */
package com.ordm.dao;

import com.ordm.model.Execution;

/**
 * @author VIJAY
 *
 */
public interface ExecutionDAO {
	
	public void saveExection(Execution exec);
	
	public Execution getExecution(Integer execId);

}
