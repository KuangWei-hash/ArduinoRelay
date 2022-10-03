package com.dianel.ArduinoRelay.arduino;

import com.dianel.ArduinoRelay.controll.ArduinoAccess;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ArduinoConnector {
    //只會有一條連線,單獨連向一個板子, 工作就是有機會就連向板子,並且維持連線
    //如果斷線的話, 自己想辦法連線回板子
    //因為我們不會有太多設備, 讀,寫 都獨立執行續
    private Socket client=null;
    private DataOutputStream dataOutputStream = null;
    private DataInputStream dataInputStream = null;
    private String host;
    private int port;
    private boolean end;
    private int[] readBuffer=new int[16];
    private int readIndex;
    private ScheduledExecutorService executorService;

    public ArduinoConnector(String host,int port)
    {
        this.host=host;
        this.port=port;
        new Thread(()->threadRead()).start();

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(()->dig(), 0, 250, TimeUnit.MILLISECONDS);
    }
    private int readByte;
    private void threadRead()
    {
        while(!end)
        {
            if(onLine())
            {
                //現在的線路是好的,進行不斷的讀取, 我們和arduino溝通的封包長度固定就是13個封包
                try{
                    while(true){
                        readBuffer[readIndex]=dataInputStream.read();//沒資料的話會卡住這裡
                        //System.out.println(readBuffer[readIndex]);
                        if(readBuffer[readIndex]<0){
                            System.out.println("串流被關閉:"+readBuffer[readIndex]);
                            disconnect();
                            break;
                        }
                        readIndex++;
                        if(readIndex==16){
                            readIndex=0;
                            //你現在可以解析,這批是完整的資料
                            parse();
                        }
                    }
                }catch (Exception e){
                    System.out.println("讀取遇到問題:"+e.toString());
                    disconnect();
                }
            }
            else
            {
                try{Thread.sleep(5000);}catch (Exception e2){}
                System.out.println("嘗試進行連線");
                try{
                    client=new Socket(host,port);
                    dataOutputStream=new DataOutputStream(client.getOutputStream());
                    dataInputStream=new DataInputStream(client.getInputStream());
                    dataOutputStream.write(new byte[]{3,0,0});
                    dataOutputStream.flush();
                    readIndex=0;
                    System.out.println("連線成功");
                }catch (Exception e){
                    System.out.println("嘗試連線失敗:"+e.toString());
                    disconnect();
                    try{Thread.sleep(3000);}catch (Exception e2){}
                }
            }
        }
    }

    //這邊就是板子的全部狀況需要的人自己拿去判讀, 以決定這是甚麼樣的資料
    public boolean[] digitalPings=new boolean[14];//14個數位腳位目前的情況
    public int[] analogPings=new int[6];          //6個類比腳位目前的情況

    public int pingsInNumber;
    private int up;
    private int down;
    private int index;
    private void parse()
    {
        switch (readBuffer[0])
        {
            case 3:
                for(index=0;index<6;index++)
                {
                    up=readBuffer[1+(index*2)];
                    down=readBuffer[1+(index*2)+1];
                    analogPings[index]=up<<8|down;
                }
                digitalPings[0] = ((readBuffer[13] & 1) != 0);
                digitalPings[1] = ((readBuffer[13] & 2) != 0);
                digitalPings[2] = ((readBuffer[13] & 4) != 0);
                digitalPings[3] = ((readBuffer[13] & 8) != 0);
                digitalPings[4] = ((readBuffer[13] & 16) != 0);
                digitalPings[5] = ((readBuffer[13] & 32) != 0);
                digitalPings[6] = ((readBuffer[13] & 64) != 0);
                digitalPings[7] = ((readBuffer[13] & 128) != 0);

                digitalPings[8] = ((readBuffer[14] & 1) != 0);
                digitalPings[9] = ((readBuffer[14] & 2) != 0);
                digitalPings[10] = ((readBuffer[14] & 4) != 0);
                digitalPings[11] = ((readBuffer[14] & 8) != 0);
                digitalPings[12] = ((readBuffer[14] & 16) != 0);
                digitalPings[13] = ((readBuffer[14] & 32) != 0);

                pingsInNumber=readBuffer[13]<<16|readBuffer[14]<<8|readBuffer[15];
                if(ArduinoAccess.analogRecords!=null) {
                    for (index = 0; index < analogPings.length; index++) {
                        if(ArduinoAccess.analogRecords[index]!=null)
                            ArduinoAccess.analogRecords[index].set(analogPings[index]);
                    }
                }
                if(ArduinoAccess.pinMap!=null)
                    ArduinoAccess.pinMap.set(pingsInNumber);
                logicCheck();
                break;
        }
    }
    private void logicCheck()
    {
        //板子的數字已經刷新, 你可以檢查一下當前數字, 看看你有沒有什麼行動想要做的
        //System.out.println("類比腳位數值:"+analogPings[0]+","+analogPings[1]+","+analogPings[2]+","+analogPings[3]+","+analogPings[4]+","+analogPings[5]);
        String dd="";
        for(int a=0;a<digitalPings.length;a++)
        {
            if(digitalPings[a])
                dd+="1,";
            else
                dd+="0,";
        }
        //System.out.println("數位腳位:"+dd);
        //System.out.println("全局針狀態:"+pingsInNumber);
    }

    private byte[] digCommand=new byte[]{3,0,0};
    private void dig()
    {
        if(onLine())
        {
            //往arduino下令挖資料
            //1.所有analog目前數值
            write(digCommand);
            //2.所有digital pin目前數值
        }
    }


    public boolean write(byte[] data)
    {
        if(onLine())
        {
            try{
                dataOutputStream.write(data);
                dataOutputStream.flush();
                return true;
            }catch (Exception e){
                System.out.println("線壞掉幫忙切線:"+e.toString());
                disconnect();
                return false;
            }
        }
        else
            return false;
    }
    public boolean onLine()
    {
        return (client!=null&&dataInputStream!=null&&dataOutputStream!=null&&client.isConnected());
    }
    private void disconnect()
    {//無論如何強制斷線
        if(dataInputStream!=null)
        {try{dataInputStream.close();}catch (Exception e){}}
        if(dataOutputStream!=null)
        {try{dataOutputStream.close();}catch (Exception e){}}
        if(client!=null)
        {try{client.close();}catch (Exception e){}}
        dataInputStream=null;
        dataOutputStream=null;
        client=null;
    }
    public void dispose()
    {
        disconnect();
        end=true;
    }
}
