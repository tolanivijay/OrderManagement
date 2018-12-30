/**
 * 
 */
package com.ordm.app;

import javax.annotation.PreDestroy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author VIJAY
 *
 */
@SpringBootApplication(scanBasePackages="com.ordm")
public class OrdmApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(OrdmApp.class, args);
	}
	
	@PreDestroy
	public void performDestryTasks() {
		// To be used for any cleanup activities required
	}

}
