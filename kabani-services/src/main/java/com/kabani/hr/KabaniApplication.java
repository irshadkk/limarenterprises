package com.kabani.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication(scanBasePackages = {"com.kabani"})
public class KabaniApplication /*extends SpringBootServletInitializer*/ {
	 private static final Logger logger = LogManager.getLogger(KabaniApplication.class);
	 
	/*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KabaniApplication.class);
    }*/
	/*@Bean
	  public ServletRegistrationBean servletRegistrationBean() {
	    ServletRegistrationBean bean = new ServletRegistrationBean(
	        new MyServlet(), "/myServlet");
	    return bean;
	  }*/
 
    public static void main(String[] args) {
    	logger.info("--***---KABANI APP STARTING UP--***---");
        SpringApplication.run(KabaniApplication.class, args); 
    }
}
