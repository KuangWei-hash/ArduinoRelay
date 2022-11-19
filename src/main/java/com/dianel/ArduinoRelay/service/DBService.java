package com.dianel.ArduinoRelay.service;

import com.dianel.ArduinoRelay.repository.Floor1Accumulation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class DBService {
    @Autowired
    Floor1Accumulation floor1Accumulation;
    public String test(){
        return floor1Accumulation.printAll();
    }
    public String checkAllInHistory(){
        //先去看一下counted
        return null;
    }
}
