/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.utils;


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.log4j.Logger;

/**
 *
 * @author superman90
 */
public class LoggerUtil {
    final public static Logger logger = Logger.getLogger(LoggerUtil.class);    
    final static Lock logLock = new ReentrantLock();
    
    public static void logInfo(String str){
        logLock.lock();
        try{
            logger.info(str);
        }finally{
            logLock.unlock();
        }
    }
    
}
