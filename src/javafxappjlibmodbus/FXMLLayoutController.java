/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLLayoutController implements Initializable {

    @FXML
    private ToggleButton ToggID1;
    @FXML
    private ToggleButton ToggID2;
    @FXML
    private ToggleButton ToggID3;
    @FXML
    private ToggleButton ToggID4;
    @FXML
    private ToggleButton ToggID5;
    @FXML
    private ToggleButton ToggID6;
    @FXML
    private ToggleButton ToggID7;
    @FXML
    private ToggleButton ToggID8;
    @FXML
    private Circle LampID1;
    @FXML
    private Circle LampID2;
    @FXML
    private Circle LampID3;
    @FXML
    private Circle LampID4;
    @FXML
    private Circle LampID5;
    @FXML
    private Circle LampID6;
    @FXML
    private Circle LampID7;
    @FXML
    private Circle LampID8;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        LampID1.setVisible(false);
        LampID2.setVisible(false);
        LampID3.setVisible(false);
        LampID4.setVisible(false);
        LampID5.setVisible(false);
        LampID6.setVisible(false);
        LampID7.setVisible(false);
        LampID8.setVisible(false);
        
        Timer t = new Timer();
        t.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if(PLCModbus.StatusKoneksi){
                    try
                    {
                        boolean coil[] =  PLCModbus.master.readCoils(1, 1-1, 8);
                        
                        if (coil[0])
                            LampID1.setVisible(true);
                        else
                            LampID1.setVisible(false);
                        if (coil[1])
                            LampID2.setVisible(true);
                        else
                            LampID2.setVisible(false);
                        if (coil[2])
                            LampID3.setVisible(true);
                        else
                            LampID3.setVisible(false);
                        if (coil[3])
                            LampID4.setVisible(true);
                        else
                            LampID4.setVisible(false);
                        if (coil[4])
                            LampID5.setVisible(true);
                        else
                            LampID5.setVisible(false);
                        if (coil[5])
                            LampID6.setVisible(true);
                        else
                            LampID6.setVisible(false);
                        if (coil[6])
                            LampID7.setVisible(true);
                        else
                            LampID7.setVisible(false);
                        if (coil[7])
                            LampID8.setVisible(true);
                        else
                            LampID8.setVisible(false);
                        
                        
                    } catch (ModbusProtocolException ex)
                    {
                        Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ModbusNumberException ex)
                    {
                        Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ModbusIOException ex)
                    {
                        Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                       
                }
            }
        }, 0, 500);
    }    

    @FXML
    private void ToggID1_Changed(ActionEvent event) 
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID1.isSelected() == true)
            {
                System.out.println("true");

                try
                {
                    PLCModbus.master.writeSingleCoil(1, 1 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 1 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID2_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID2.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 2 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 2 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID3_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID3.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 3 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 3 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID4_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID4.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 4 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 4 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID5_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID5.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 5 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 5 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID6_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID6.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 6 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 6 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID7_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID7.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 7 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 7 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @FXML
    private void ToggID8_Changed(ActionEvent event)
    {
        if (PLCModbus.StatusKoneksi)
        {
            if (ToggID8.isSelected() == true)
            {
                System.out.println("true");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 8 - 1, true);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                System.out.println("false");
                try
                {
                    PLCModbus.master.writeSingleCoil(1, 8 - 1, false);
                } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex)
                {
                    Logger.getLogger(FXMLLayoutController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
