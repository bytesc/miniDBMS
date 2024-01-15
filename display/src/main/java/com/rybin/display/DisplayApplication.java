package com.rybin.display;

import org.dom4j.DocumentException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import function.Lg;

import java.io.IOException;

@SpringBootApplication
public class DisplayApplication {

	public static void main(String[] args) throws DocumentException, IOException {
		SpringApplication.run(DisplayApplication.class, args);
	}


}
