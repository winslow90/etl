/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.time.DateFormatUtils;
import su90.etl.beans.Result;
import su90.etl.utils.DeviceTypeParser;
import su90.etl.utils.LoggerUtil;
import su90.etl.utils.TypeParser;

/**
 *
 * @author superman90
 */
public class SlaveController implements Runnable {
    
    private String INPUTPATHSTR = null;
    private String OUTPUTPATHSTR = null;
    private String FILENAME= null;
    private Integer STATUS =null;
    private HashMap<Integer,Result> results=new HashMap<>();
    
    public SlaveController(String inputpathstr,String outputpathstr, String filename, Integer status){
        INPUTPATHSTR=inputpathstr;
        OUTPUTPATHSTR=outputpathstr;
        FILENAME=filename;
        STATUS=status;
    }
    
    private Result parse_imps(Iterator<String> iterator){
        Result tmp = new Result();
        int i =0;
//        unix_timestamp,transaction_id,connection_type,device_type,count
        while (iterator.hasNext()){
            String tmpstr= iterator.next();
            switch(i){
                case 0:
//                    tmp.setIso8601_timestamp(new Timestamp(Long.parseLong(tmpstr)).toLocaleString());
                    tmp.setIso8601_timestamp(DateFormatUtils.format(Long.parseLong(tmpstr)*1000,"yyyy-MM-dd'T'HH:mm:ssZZ",TimeZone.getTimeZone("America/Los_Angeles")));
                    break;
                case 1:
                    tmp.setTransaction_id(tmpstr);
                    break;
                case 2:
                    tmp.setConnection_type(TypeParser.get(Integer.parseInt(tmpstr)));
                    break;
                case 3:
                    tmp.setDevice_type(DeviceTypeParser.get(Integer.parseInt(tmpstr)));
                    break;
                case 4:
                    tmp.setImps(Integer.parseInt(tmpstr));
                    break;
            }
            i++;
        }
        
        return tmp;
    }
    
    private void _do_imps(){
        Reader in=null;
        try {
            in = new FileReader(INPUTPATHSTR
                    +System.getProperty("file.separator")+"facts"
                    +System.getProperty("file.separator")+"imps"
                    +System.getProperty("file.separator")+FILENAME
                    );
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {                
                Result tmpresult = parse_imps(record.iterator());
                if (results.containsKey(tmpresult.hashCode())){
                    Result previousresult = results.get(tmpresult.hashCode());
                    previousresult.setImps(previousresult.getImps()+tmpresult.getImps());
                    previousresult.addOneDeviceType(tmpresult.getDevice_type());
                    results.put(previousresult.hashCode(),previousresult);                    
                }else{
                    results.put(tmpresult.hashCode(), tmpresult);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (in!=null) try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    private Result parse_clicks(Iterator<String> iterator){
        Result tmp = new Result();
        int i =0;
//        unix_timestamp,transaction_id,count
        while (iterator.hasNext()){
            String tmpstr= iterator.next();
            switch(i){
                case 0:
                    tmp.setIso8601_timestamp(DateFormatUtils.format(Long.parseLong(tmpstr)*1000,"yyyy-MM-dd'T'HH:mm:ssZZ",TimeZone.getTimeZone("America/Los_Angeles")));
                    break;
                case 1:
                    tmp.setTransaction_id(tmpstr);
                    break;
                case 2:
                    tmp.setClicks(Integer.parseInt(tmpstr));
                    break;
            }
            i++;
        }
        
        return tmp;
    }
    private void _do_clicks(){
        Reader in=null;
        try {
            in = new FileReader(INPUTPATHSTR
                    +System.getProperty("file.separator")+"facts"
                    +System.getProperty("file.separator")+"clicks"
                    +System.getProperty("file.separator")+FILENAME
                    );
            Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(in);
            for (CSVRecord record : records) {                
                Result tmpresult = parse_clicks(record.iterator());
                if (results.containsKey(tmpresult.hashCode())){
                    Result previousresult = results.get(tmpresult.hashCode());
                    previousresult.setClicks(previousresult.getClicks()+tmpresult.getClicks());
                    results.put(previousresult.hashCode(),previousresult);                    
                }else{
                    results.put(tmpresult.hashCode(), tmpresult);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (in!=null) try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void _do_write_result(){
        
        String outputfilename = FILENAME.substring(0,FILENAME.indexOf(".csv"))+".json";
        
        ObjectMapper mapper = new ObjectMapper();
        BufferedWriter writer = null;
        
        
        
        try
        {
            writer = new BufferedWriter( new FileWriter(
                    OUTPUTPATHSTR                    
                    +System.getProperty("file.separator")+outputfilename
            ));
            for (Map.Entry<Integer,Result> entry : results.entrySet()){
                try {
                    Result tmpresult = entry.getValue();
                    if (tmpresult.getImps()==0) tmpresult.setImps(null);
                    if (tmpresult.getClicks()==0) tmpresult.setClicks(null);
                    String tmpstr = mapper.writeValueAsString(entry.getValue());
                    writer.write(tmpstr);
                    writer.newLine();
                } catch (Exception ex) {
                    Logger.getLogger(SlaveController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            

        }
        catch ( IOException e)
        {
        }
        finally
        {
            try
            {
                if ( writer != null)
                writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
    }

    @Override
    public void run() {
        String idstr = FILENAME.substring(0,FILENAME.indexOf(".csv"));
        long timespan = System.currentTimeMillis();
        LoggerUtil.logInfo("Hour "+idstr + " ETL start.");
        _do_imps();
        _do_clicks();
        _do_write_result();
        timespan= System.currentTimeMillis()- timespan;
        LoggerUtil.logInfo("Hour "+idstr + " ETL complete, elapsed time:"+ timespan);
    }
    
}
