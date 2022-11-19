package com.dianel.ArduinoRelay.repository;
import com.dianel.ArduinoRelay.data.ERecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class Floor1Accumulation {
    @Autowired
    JdbcTemplate jdbcTemplate;
    //這邊是抄表,自動作時間轉換
    public String printAll(){
        String sql = "SELECT * FROM POWER";
        List<ERecord> result = jdbcTemplate.query(sql, new BeanPropertyRowMapper<ERecord>(ERecord.class), new Object[] {});
        StringBuffer stringBuffer=new StringBuffer();
        for(ERecord rr:result){
            stringBuffer.append(rr);
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }
}
