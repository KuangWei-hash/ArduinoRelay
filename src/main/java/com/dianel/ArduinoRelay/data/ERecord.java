package com.dianel.ArduinoRelay.data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Date;
@Getter
@Setter
@ToString
public class ERecord {
    public Date time;
    public long kwh;
}
