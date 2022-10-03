package com.dianel.ArduinoRelay.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "dianel")
@Data
public class Config {
    private String PATH;
    private String ArduinoIP;
    private int ArduinoPort;
}

