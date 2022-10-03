package com.dianel.ArduinoRelay;

import com.dianel.ArduinoRelay.arduino.ArduinoConnector;
import com.dianel.ArduinoRelay.configure.Config;
import com.dianel.ArduinoRelay.service.VideoFileManagerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class ArduinoRelayApplication {

	public static void main(String[] args) {
		//Shared.arduinoConnector=new ArduinoConnector("192.168.1.137",1984);
		SpringApplication.run(ArduinoRelayApplication.class, args);

	}
}
