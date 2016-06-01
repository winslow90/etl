/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import su90.etl.utils.DeviceTypeParser;
import su90.etl.utils.LoggerUtil;
import su90.etl.utils.TypeParser;

/**
 *
 * @author superman90
 */
public class MainEntry {
    
    public static Integer THREAD_NUM = null;
    public static String INPUT_DIR_STR = null;
    public static String OUTPUT_DIR_STR = null;
    public static String LOG_DIR_STR = null;
    
    private ExecutorService pool = null;
    
    public MainEntry(String[] args) throws Exception{
                      
        THREAD_NUM = 5 ;
        INPUT_DIR_STR = System.getProperty("user.dir")+System.getProperty("file.separator")+"in";
        OUTPUT_DIR_STR = System.getProperty("user.dir")+System.getProperty("file.separator")+"out";
        LOG_DIR_STR = System.getProperty("user.dir")+System.getProperty("file.separator")+"logs";
        
        int argsnum = args.length;
        int i=0;
        while(i <argsnum){
            switch(args[i]){
                case "-h":
                    throw new Exception("Need help exception");
                case "-p":
                    THREAD_NUM = Integer.parseInt(args[i+1]);
                    i+=2;
                    break;
                case "-i":
                    INPUT_DIR_STR = args[i+1];
                    i+=2;
                    break;
                case "-o":
                    OUTPUT_DIR_STR = args[i+1];
                    i+=2;
                    break;
                case "-g":
                    LOG_DIR_STR = args[i+1];
                    i+=2;
                    break;
                default:
                    i++;
                    break;
                    
            }
        }
        validate_state();
        TypeParser.initializer(INPUT_DIR_STR+"/dimensions/connection_type.json");
        DeviceTypeParser.initializer(INPUT_DIR_STR+"/dimensions/device_type.json");
        
//        #log4j.appender.file=org.apache.log4j.RollingFileAppender
//        ##log4j.appender.file.File=C:\\log4j-application.log
//        #log4j.appender.file.MaxFileSize=5MB
//        #log4j.appender.file.MaxBackupIndex=10
//        #log4j.appender.file.layout=org.apache.log4j.PatternLayout
//        #log4j.appender.file.layout.ConversionPattern=%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n
        
        FileAppender fa = new FileAppender();
        fa.setName("FILE");
        fa.setFile(LOG_DIR_STR+System.getProperty("file.separator")+"etl.log");
        fa.setLayout(new PatternLayout("%d %-5p %m%n"));
        fa.setThreshold(Level.DEBUG);
        fa.setAppend(true);
        fa.activateOptions();

        //add appender to any Logger (here is root)
        //  Logger.getRootLogger().addAppender(fa)
        LoggerUtil.logger.addAppender(fa);
    }
    private static void validate_state(){
        File output_dir = new File(OUTPUT_DIR_STR);
        output_dir.mkdir();
        File log_dir = new File(LOG_DIR_STR);
        log_dir.mkdir();
    }
    
    public void do_work(){
        
        // 1 for contain imps 2 for clicks 3 for both
        HashMap<String,Integer> resultitems = new HashMap<>();
        File tmpnode = new File(INPUT_DIR_STR
                +System.getProperty("file.separator")+"facts"
                +System.getProperty("file.separator")+"imps"
        );
        if (tmpnode.isDirectory()){
            String[] subNote = tmpnode.list();
            for(String filename : subNote){
                resultitems.put(filename, 1);
            }
        }
        tmpnode = new File(INPUT_DIR_STR
                +System.getProperty("file.separator")+"facts"
                +System.getProperty("file.separator")+"clicks");
        if (tmpnode.isDirectory()){
            String[] subNote = tmpnode.list();
            for(String filename : subNote){
                if (resultitems.containsKey(filename)){
                    resultitems.put(filename, resultitems.get(filename)+2);
                }else{
                    resultitems.put(filename, 2);
                }
            }
        }
        
        pool = Executors.newFixedThreadPool(THREAD_NUM);
        for (Map.Entry<String,Integer> entry : resultitems.entrySet()){
            SlaveController slave = new SlaveController(INPUT_DIR_STR,OUTPUT_DIR_STR,entry.getKey(),entry.getValue());            
            pool.submit(slave);            
        }
        pool.shutdown();   
    }
    
    public static void main(String[] args) {
        MainEntry entry=null;
        try{
            entry = new MainEntry(args);            
        }catch(Exception e){
            System.out.println(e.toString());            
        }
        
        if (entry!=null){
            entry.do_work();
        }
    }
    
}
