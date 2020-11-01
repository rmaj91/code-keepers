package com.rmaj91;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class CodeKeepersApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeKeepersApplication.class, args);
	}

}
