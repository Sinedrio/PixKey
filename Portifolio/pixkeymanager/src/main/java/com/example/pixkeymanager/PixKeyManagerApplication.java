package com.example.pixkeymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PixKeyManagerApplication {
//INICIAR O MONGODB: sudo systemctl start mongod
	public static void main(String[] args) {
		SpringApplication.run(PixKeyManagerApplication.class, args);
	}

}
