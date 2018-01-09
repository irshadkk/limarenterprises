package com.kabani.hr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class KabaniApplication extends SpringBootServletInitializer {

	 
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(KabaniApplication.class);
    }
 
    public static void main(String[] args) {
        SpringApplication.run(KabaniApplication.class, args);
    }
}
