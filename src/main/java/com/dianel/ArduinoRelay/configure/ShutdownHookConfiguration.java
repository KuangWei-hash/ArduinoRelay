package com.dianel.ArduinoRelay.configure;

import com.dianel.ArduinoRelay.tools.ExitHook;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShutdownHookConfiguration {

    @Bean(destroyMethod = "destroy")
    public ExitHook initializeBean3() {
        return new ExitHook();
    }
}
