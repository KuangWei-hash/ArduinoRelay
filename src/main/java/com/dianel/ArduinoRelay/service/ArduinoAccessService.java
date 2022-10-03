package com.dianel.ArduinoRelay.service;

import com.dianel.ArduinoRelay.arduino.ArduinoConnector;
import com.dianel.ArduinoRelay.configure.Config;
import io.micrometer.core.annotation.Timed;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
@Log4j2
@Service
public class ArduinoAccessService {

    @Autowired
    Config config;
    ArduinoConnector arduinoConnector;
    @PostConstruct
    public void init() {
        arduinoConnector=new ArduinoConnector(config.getArduinoIP(),config.getArduinoPort());
    }
    public boolean setPin(int pin,int value){
        return arduinoConnector.setPin(pin,value);
    }
    public boolean setPinPWM(int pin, int value){
        return arduinoConnector.setPinPWM(pin,value);
    }
    public boolean getDigitalPin(int pin){
        return arduinoConnector.digitalPings[pin];
    }
    public int getAnalogPin(int pin){
        return arduinoConnector.analogPings[pin];
    }

}
