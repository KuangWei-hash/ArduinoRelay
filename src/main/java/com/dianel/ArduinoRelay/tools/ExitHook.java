package com.dianel.ArduinoRelay.tools;

import com.dianel.ArduinoRelay.Shared;

public class ExitHook {
    public void destroy() {
        System.out.println("結束程式");
        Shared.arduinoConnector.dispose();
        Shared.videoManager.dispose();
    }
}