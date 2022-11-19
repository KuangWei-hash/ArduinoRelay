package com.dianel.ArduinoRelay.controll;

import com.dianel.ArduinoRelay.service.DBService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Log4j2
@RestController
public class DBAccess {
    @Autowired
    DBService dbService;

    @GetMapping("/test")
    public String test(){
        return dbService.test();
    }
}
