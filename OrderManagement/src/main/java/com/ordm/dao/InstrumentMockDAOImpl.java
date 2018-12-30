/**
 * 
 */
package com.ordm.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.ordm.model.Instrument;

/**
 * @author VIJAY
 *
 */
@Component
public class InstrumentMockDAOImpl implements InstrumentDAO{
	
	private Map<Integer,Instrument> availableInstruments = new HashMap<Integer,Instrument>();
	
	public InstrumentMockDAOImpl() {
		IntStream.range(1, 10).forEach(x -> availableInstruments.put(x,new Instrument(x)));
	}

	public Instrument getInstrument(Integer instrumentId)  {
		return availableInstruments.get(instrumentId);
		
	}

}
