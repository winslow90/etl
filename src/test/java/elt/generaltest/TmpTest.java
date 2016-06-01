/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elt.generaltest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;

/**
 *
 * @author superman90
 */
public class TmpTest {
    
    public TmpTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void hashmaptest(){
        Map mymap = new HashMap();
        mymap.put("1","one");
        mymap.put("1","not one");
        mymap.put("1","surely not one");
        // The following line was added:
        mymap.put("1","one");
        System.out.println(mymap.get("1"));

    }
    
    @Test
    public void timestamptest(){
        Long longval = 1463288494L;
        String str = DateFormatUtils.format(longval*1000,"yyyy-MM-dd'T'HH:mm:ssZZ",TimeZone.getTimeZone("America/Los_Angeles"));
        System.out.println(str);
    }
    
    @Test
    public void timestampreversetest(){
        SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
//        2016-05-14T22:01:34-07:00
//        2010-01-01T12:00:00+01:00
        String date = "2010-01-01T12:00:00+01:00".replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
        try {
            Date tmp = ISO8601DATEFORMAT.parse(date);
            System.out.println(tmp.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(TmpTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
