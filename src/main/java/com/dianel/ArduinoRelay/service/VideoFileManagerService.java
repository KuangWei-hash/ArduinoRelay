package com.dianel.ArduinoRelay.service;
import com.dianel.ArduinoRelay.configure.Config;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Objects;

@Service
@Log4j2
public class VideoFileManagerService{
    @Autowired
    public Config config;
    public boolean end;
    File folder;
    @PostConstruct
    public void init() {
        folder=new File(config.getPATH());
        new Thread(this::run).start();
    }
    private void run()
    {
        while(!end&&folder.exists())
        {
            try{Thread.sleep(10000);}catch (Exception ignored){}
            for (final File fileEntry : Objects.requireNonNull(folder.listFiles())) {
                if (!fileEntry.isDirectory()) {
                    String name=fileEntry.getName();
                    if(name.contains(".mp4"))
                    {
                        name=name.replace(".mp4","");
                        String [] fields=name.split("_");
                        if(fields[0].contains("25005478"))
                        {//正門
                            File dateFolder=new File(config.getPATH()+"\\正門\\"+fields[1]);
                            if (!dateFolder.exists()){
                                dateFolder.mkdirs();
                            }
                            //System.out.println("移動:"+fileEntry.getAbsolutePath()+ "到達:"+dateFolder.getAbsolutePath());
                            fileEntry.renameTo(new File(dateFolder.getAbsolutePath()+"\\"+fileEntry.getName()));
                        }
                        else
                        {//側邊
                            File dateFolder=new File(config.getPATH()+"\\側邊\\"+fields[1]);
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
        log.info("放棄整理資料");
    }
    public void dispose()
    {
        end=true;
    }
}
