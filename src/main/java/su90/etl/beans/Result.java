/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package su90.etl.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author superman90
 */
@JsonInclude(Include.NON_NULL)
public class Result {
    String iso8601_timestamp = new Date().toLocaleString();
    String transaction_id = new String();
    String connection_type = new String();
    String device_type= new String();
    Integer imps = 0;
    Integer clicks = 0;

    public String getIso8601_timestamp() {
        return iso8601_timestamp;
    }

    public void setIso8601_timestamp(String iso8601_timestamp) {
        this.iso8601_timestamp = iso8601_timestamp;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getConnection_type() {
        return connection_type;
    }

    public void setConnection_type(String connection_type) {
        this.connection_type = connection_type;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public Integer getImps() {
        return imps;
    }

    public void setImps(Integer imps) {
        this.imps = imps;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }
    
    // self define code below
    
    public void addOneDeviceType(String newtypestr){
        if (!this.device_type.toLowerCase().contains(newtypestr.toLowerCase())){
            this.device_type = this.device_type+'|'+newtypestr;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[')
            .append(iso8601_timestamp).append(',')
            .append(transaction_id).append(',')
            .append(connection_type).append(',')
            .append(device_type).append(',')
            .append(imps).append(',')
            .append(clicks).append(']')
        ;
        
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        return transaction_id.hashCode();
    }   
}
