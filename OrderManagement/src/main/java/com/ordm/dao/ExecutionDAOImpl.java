/**
 * 
 */
package com.ordm.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.ordm.model.Execution;
import com.ordm.util.OrdmUtil;

/**
 * @author VIJAY
 *
 */
@Component
public class ExecutionDAOImpl implements ExecutionDAO{
	
	private Map<Integer, Execution> execBookCache = new ConcurrentHashMap<Integer, Execution>();

	@Override
	public void saveExection(Execution exec) {
		exec.setExecId(OrdmUtil.getNextSequence());
		execBookCache.put(exec.getExecId(), exec);
	}

	@Override
	public Execution getExecution(Integer execId) {
		return execBookCache.get(execId);
	}

}
