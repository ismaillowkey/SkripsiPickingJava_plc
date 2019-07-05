/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import org.hibernate.Session;

/**
 *
 * @author lupa-nama
 */
public class PLCModbus {
    // for serial port
    public static SerialParameters sp = new SerialParameters();
    public static String[] dev_list;
    
    public static ModbusMaster master;
    public static boolean mprocess;

    public static String LastOpenPane;
    
    // hibernate
    public static Session session_mysql;
    
    public static boolean StatusKoneksi;
    private static String ErrorConnect;
    
    //private static PLCModbus plcmodbus;

    
    
    public static void ConnectToSlave(SerialParameters sp) {
        try{
            master = ModbusMasterFactory.createModbusMasterRTU(sp);
            //master.setResponseTimeout(1);            
            master.connect();
            StatusKoneksi = true;
            
        }
        catch(Exception ex){
            StatusKoneksi = false;
            ErrorConnect = ex.getMessage();
        }
    }
    
    public static void DisconnectToSlave(){
        try{
            master.disconnect();
            StatusKoneksi = false;
        }
        catch(Exception ex){
            StatusKoneksi = true;
            ErrorConnect = ex.getMessage();
        }
    }
    
    
}
