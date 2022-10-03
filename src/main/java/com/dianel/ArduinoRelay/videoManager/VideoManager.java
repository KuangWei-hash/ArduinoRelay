package com.dianel.ArduinoRelay.videoManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class VideoManager extends Thread{
    public boolean end;
    String path;
    File folder;
    public VideoManager(String path)
    {
        this.path=path;
        folder=new File(path);
        this.start();
    }
    public void run()
    {
        while(!end)
        {
            try{Thread.sleep(10000);}catch (Exception e){}
            for (final File fileEntry : folder.listFiles()) {
                if (!fileEntry.isDirectory()) {
                    String name=fileEntry.getName();
                    if(name.contains(".mp4"))
                    {
                        name=name.replace(".mp4","");
                        String [] fields=name.split("_");
                        if(fields[0].contains("25005478"))
                        {//正門
                            File dateFolder=new File(path+"\\正門\\"+fields[1]);
                            if (!dateFolder.exists()){
                                dateFolder.mkdirs();
                            }
                            //System.out.println("移動:"+fileEntry.getAbsolutePath()+ "到達:"+dateFolder.getAbsolutePath());
                            fileEntry.renameTo(new File(dateFolder.getAbsolutePath()+"\\"+fileEntry.getName()));
                        }
                        else
                        {//側邊
                            File dateFolder=new File(path+"\\側邊\\"+fields[1]);
                            if (!dateFolder.exists()){
                                dateFolder.mkdirs();
                            }
                            //System.out.println("移動:"+fileEntry.getAbsolutePath()+ "到達:"+dateFolder.getAbsolutePath());
                            fileEntry.renameTo(new File(dateFolder.getAbsolutePath()+"\\"+fileEntry.getName()));
                        }
                    }
                }
            }


        }
    }
    public void dispose()
    {
        end=true;
    }
}
