package com.dianel.ArduinoRelay;

import com.dianel.ArduinoRelay.arduino.ArduinoConnector;
import com.dianel.ArduinoRelay.videoManager.VideoManager;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ArduinoRelayApplication {

	public static void main(String[] args) {
		Shared.arduinoConnector=new ArduinoConnector("192.168.1.137",1984);
		Shared.videoManager=new VideoManager("Z:\\record");
		SpringApplication.run(ArduinoRelayApplication.class, args);

	}
}
