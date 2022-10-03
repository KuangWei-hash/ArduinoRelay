package com.dianel.ArduinoRelay.arduino;

public interface Logic {
    //what would you like to do with current status
    public void execute(ArduinoConnector arduino,int[] analogPings,boolean[] digitalPings);
}
