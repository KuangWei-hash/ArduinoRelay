package com.dianel.ArduinoRelay.arduino;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class MyLogic implements Logic{
    //analog 0  is for battery voltage, range 0~30v map to 0~1023
    //digital 7 is for battery relay
    float batteryVoltage;
    float batteryCurrent;
    @Override
    public void execute(ArduinoConnector arduino, int[] analogPings, boolean[] digitalPings) {
        //voltage below 11v will damage the battery, if below 11v, send battery relay to off
        batteryVoltage=25.0f*(analogPings[2]/1024.0f);
        batteryCurrent=0;

        if(digitalPings[9]) {//if relay is on
            if (batteryVoltage < 11.0f||batteryCurrent > 45) {
                log.warn("close the relay, protect battery");
                arduino.setPin(9, 0);//turn off
            }
        }
    }
}
