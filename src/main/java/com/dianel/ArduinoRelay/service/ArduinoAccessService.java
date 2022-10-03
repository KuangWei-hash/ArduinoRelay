package com.dianel.ArduinoRelay.service;

import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ArduinoAccessService {
    @Timed(value = "greeting.time", description = "Time taken to return greeting")
    public void getGreeting() {
    }
}
