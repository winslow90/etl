/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.utils;



import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author superman90
 */
public class DeviceTypeParser {
    static HashMap<Integer,String> hashmapper = null;
    
    /*
    *@path "/home/superman90/Documents/duangyujia/in/dimensions/device_type.json"
    */    
    public static void initializer(String path) {
        
        hashmapper=new HashMap<>();
        
        ObjectMapper mapper = new ObjectMapper();
        try {
 
            Object[][] objs =mapper.readValue(new FileReader(
                    path), Object[][].class);
            
            for (Object[] subobjs:objs){
                hashmapper.put((Integer)subobjs[0], (String) subobjs[1]);
            }           
 
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    public static String get(Integer index){
        if (hashmapper!=null&&hashmapper.containsKey(index)){
            return hashmapper.get(index);
        }        
        return null;
        
    }
}
