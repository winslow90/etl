/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elt.controllertest;

import su90.etl.controller.SlaveController;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import su90.etl.utils.TypeParser;

/**
 *
 * @author superman90
 */
public class SlaveControllerTest {
    private String INPUTPATHSTR = "/home/superman90/Documents/duangyujia/in";
    private String OUTPUTPATHSTR = "/home/superman90/Documents/duangyujia/out";
    private String FILENAME= "2016-05-15-05.csv";
    private Integer STATUS = 3;
    
    @Before
    public void init(){
        //TODO: do not forgot to append to the main entry
        TypeParser.initializer(INPUTPATHSTR+"/dimensions/type.json");
        TypeParser.initializer(INPUTPATHSTR+"/dimensions/device_type.json");
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void debug0515(){
        SlaveController slave = new SlaveController(INPUTPATHSTR,OUTPUTPATHSTR,FILENAME,STATUS);
        slave.run();
    }
}
