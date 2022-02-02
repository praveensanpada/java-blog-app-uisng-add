package com.blog.app;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyappApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyappApplication.class, args);
		System.out.print("\n\n.........Application Is Running.............\n\n");
	}
	
	@Bean
	public ModelMapper modelMapper(){
	    return new ModelMapper();
	}

}
