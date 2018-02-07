package com.kabani.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.kabani"})
public class KabaniApplication /*extends SpringBootServletInitializer*/ {

	 
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
        SpringApplication.run(KabaniApplication.class, args); 
    }
}
