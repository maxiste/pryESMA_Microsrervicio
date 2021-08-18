package com.esma.generacionXML.lanzador;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.context.annotation.PropertySource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.esma.generacionXML.dao")
@EntityScan(basePackages = "com.esma.generacionXML.modelo")
@SpringBootApplication(scanBasePackages = {"com.esma.generacionXML.controller","com.esma.generacionXML.service"})
//@PropertySource(value="classpath:aplication.properties") //no es ncesario porque toma por defecto la ruta donde existe archivo de propiedades
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
