/**
 * 
 */
package com.ordm.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author VIJAY
 *
 */
public class OrdmUtil {

	private static AtomicInteger currentSequence = new AtomicInteger(0);
	
	public static Integer getNextSequence() {
		return currentSequence.incrementAndGet();
	}
	
}
