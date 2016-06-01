/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.beans;

import java.sql.Timestamp;

/**
 *  unix_timestamp,transaction_id,count
 * @author superman90
 */
public class Click {
    Timestamp unix_timestamp;
    String transaction_id;
    Integer count;    
}
