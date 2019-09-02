package com.appsdeveloperblog.app.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
//this is to globally configure our project to cross origin request services	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
	registry
	.addMapping("/**")  // we can give users/email-verification
	.allowedMethods("*") //"GET","PUT","POST"
	.allowedOrigins("*");//"http://localhost:8083","http://localhost:8084" --we ("*") give permission to all 
	}

}
