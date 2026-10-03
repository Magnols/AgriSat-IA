package br.com.fiap.energia_esg_api;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EnergiaEsgApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EnergiaEsgApiApplication.class, args);
	}

}
