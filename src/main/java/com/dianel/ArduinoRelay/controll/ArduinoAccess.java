package com.dianel.ArduinoRelay.controll;
import com.dianel.ArduinoRelay.configure.Config;
import com.dianel.ArduinoRelay.service.ArduinoAccessService;
import io.micrometer.core.instrument.MeterRegistry;
import io.prometheus.client.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ArduinoAccess {
    static final Counter REQUESTS = Counter.build().name("requests_total")
            .help("Total requests.").labelNames("label").register();
    @Autowired
    Config config;
    @Autowired
    ArduinoAccessService arduinoAccessService;

    public static AtomicInteger[] analogRecords=new AtomicInteger[6];
    public static AtomicInteger pinMap;
    public ArduinoAccess(MeterRegistry registry)
    {
        for(int a=0;a<6;a++)
            analogRecords[a] = registry.gauge("analog"+a, new AtomicInteger(0));
        pinMap=registry.gauge("pinMap",new AtomicInteger(0));
    }

    @GetMapping("/getAll")
    String getAll() {
        //arduinoAccessService.getGreeting();
        String rr="";
        for(int a=0;a<6;a++)
            rr+= ("<p>A"+a+":"+arduinoAccessService.getAnalogPin(a)+"</p>");
        for(int a=0;a<14;a++)
            rr+=("<p>D"+a+":"+arduinoAccessService.getDigitalPin(a)+"</p>");
        return rr;
    }
    @GetMapping("/setPin/{pin}/{value}")
    String setPin(@PathVariable int pin,@PathVariable int value) {
        if(value<0||value>1)
            return "數值錯誤";
        if(pin>=10||pin==4||pin<0||pin==0||pin==1)
            return "腳位不能使用";
        arduinoAccessService.setPin(pin,value);
        return "設定腳位:"+pin+" 值為:"+value;
    }
    @GetMapping("/setPinPWM/{pin}/{value}")
    String setPinPWM(@PathVariable int pin,@PathVariable int value) {
        if(value<0||value>255)
            return "數值錯誤";
        if(pin>=10||pin==4||pin<0||pin==0||pin==1||pin==2||pin==8||pin==7)
            return "腳位不能使用";
        arduinoAccessService.setPinPWM(pin,value);
        return "設定腳位PWM:"+pin+" 值為:"+value;
    }
}
