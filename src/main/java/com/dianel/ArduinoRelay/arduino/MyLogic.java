package com.dianel.ArduinoRelay.arduino;

public class MyLogic implements Logic{
    //analog 0  is for battery voltage, range 0~30v map to 0~1023
    //digital 7 is for battery relay
    float batteryVoltage;
    float batteryCurrent;
    @Override
    public void execute(ArduinoConnector arduino, int[] analogPings, boolean[] digitalPings) {
        //voltage below 11v will damage the battery, if below 11v, send battery relay to off
        batteryVoltage=30.0f*(analogPings[0]/1023.0f);
        batteryCurrent=0;
        if(digitalPings[7]) {//if relay is on
            if (batteryVoltage < 11.0f||batteryCurrent > 45)
                arduino.setPin(7, 0);//turn off
        }
    }
}
