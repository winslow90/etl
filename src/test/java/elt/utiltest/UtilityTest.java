/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elt.utiltest;

import org.junit.Test;
import static org.junit.Assert.*;
import su90.etl.utils.TypeParser;

/**
 *
 * @author superman90
 */
public class UtilityTest {

    public UtilityTest() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testTypeParse() {
        TypeParser.initializer("/home/superman90/Documents/duangyujia/in/dimensions/connection_type.json");
        System.out.println(TypeParser.get(0));
        System.out.println(TypeParser.get(1));
        System.out.println(TypeParser.get(2));
        System.out.println(TypeParser.get(3));
        System.out.println(TypeParser.get(4));
        System.out.println(TypeParser.get(5));
        System.out.println(TypeParser.get(6));
    }

    @Test
    public void testDeviceTypeParse() {
        TypeParser.initializer("/home/superman90/Documents/duangyujia/in/dimensions/device_type.json");
        System.out.println(TypeParser.get(0));
        System.out.println(TypeParser.get(1));
        System.out.println(TypeParser.get(2));
        System.out.println(TypeParser.get(3));
        System.out.println(TypeParser.get(4));
        System.out.println(TypeParser.get(5));
        System.out.println(TypeParser.get(6));
    }
}
