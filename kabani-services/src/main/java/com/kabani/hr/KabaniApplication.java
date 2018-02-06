package com.kabani.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

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
