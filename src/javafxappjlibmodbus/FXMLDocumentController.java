/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jssc.SerialPortList;




/**
 *
 * @author lupa-nama
 */
public class FXMLDocumentController implements Initializable {
    @FXML
    private Label label;
    @FXML
    private Button btnConnect;
    @FXML
    private ComboBox cmbPort;
    @FXML
    private Button btnRefresh;
    private TextField txt4x1;
    @FXML
    private Button btnDisconnect;
    @FXML
    private Label lblStatus;
    @FXML
    private AnchorPane form1;
    @FXML
    private Button menuBar_Close;
    private TabPane menuTab1;
    @FXML
    private Button menuHome; 
    @FXML
    private Button menuInStock;
    @FXML
    private Button menuOutStock;
    @FXML
    private Button menuLayout;
    @FXML
    private Button menuSettings;
    @FXML
    private Pane mainPane;
    @FXML
    private Pane paneCOM;
    
    
    static Pane PaneHome,PaneInStock,PanePickStock,PaneLayout,PaneSetting;
    
    static Timer timer1;
    static TimerTask myTask;
    static Stage stage;
    static Pane newPane;
    //public static Pane newPane;
    private Label lblStatusBarcode;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //stage = (Stage)form1.getScene().getWindow();
        getSerialPort(true);
        lblStatus.setText("...");
        try
        {
            PaneHome = FXMLLoader.load(getClass().getResource("FXMLHome.fxml"));
            PaneInStock = FXMLLoader.load(getClass().getResource("FXMLInStock.fxml"));
            PanePickStock = FXMLLoader.load(getClass().getResource("FXMLPickStock.fxml"));
            PaneLayout = FXMLLoader.load(getClass().getResource("FXMLLayout.fxml"));
            PaneSetting = FXMLLoader.load(getClass().getResource("FXMLSettings.fxml"));
        } catch (IOException ex)
        {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadHome();
    }    

    public void setLabelStatus(String lbl){
        this.lblStatusBarcode.setText(lbl);
    }
    
    
    @FXML
    private void btnConnect_clicked(ActionEvent event){
        //Modbus.setLogLevel(Modbus.LogLevel.LEVEL_DEBUG);
        if (PLCModbus.dev_list.length > 0) {
            SerialUtils.setSerialPortFactory(new SerialPortFactoryJSSC());
             PLCModbus.sp.setDevice((String) cmbPort.getValue());
             // these parameters are set by default 9600 8N1
             PLCModbus.sp.setBaudRate(SerialPort.BaudRate.BAUD_RATE_115200);
             PLCModbus.sp.setDataBits(8);
             PLCModbus.sp.setParity(SerialPort.Parity.NONE);
             PLCModbus.sp.setStopBits(1);
             
             try
             {  
                // Connect to the PLC
                PLCModbus.ConnectToSlave(PLCModbus.sp);             
                
                cmbPort.setDisable(true);
                btnConnect.setDisable(true);
                btnDisconnect.setDisable(false);
                btnRefresh.setDisable(true);
                              
                timer1 = new Timer();
                myTask = new TimerTask(){
                    @Override
                    public void run() {                      
                        // at next string we receive ten registers from a slave with id of 1 at offset of 0.
                        int[] registerValues;
                        try {
                            Thread.sleep(200);
                            registerValues = PLCModbus.master.readHoldingRegisters(1, 1-1, 1);
                            //System.out.println("4x1 : " + registerValues[0]);
                            showStatus("Connected");
                            //txt4x1.setText(String.valueOf(registerValues[0]));
                        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException ex ) {
                            showStatus("Reconnecting...");
                            //Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }             
                };
                timer1.schedule(myTask, 100,100);
                
                
             }
             catch(Exception e){
                 System.out.println("set : " + e);
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.setTitle("Error");
                 stage = (Stage)form1.getScene().getWindow();
                 alert.initOwner(stage);
                 alert.setHeaderText("Cannot Connect to " + (String) cmbPort.getValue() + "\r\n" + e.getMessage() );
                 alert.showAndWait();
             }
             
        }
        
    }

    private void showStatus(String s){
        Platform.runLater( () -> {
          lblStatus.setText(s);
        });
    }
    
    @FXML
    private void btnRefresh_clicked(ActionEvent event) {
        getSerialPort(false);
    }
    
    private void getSerialPort(boolean startup){
        PLCModbus.dev_list = SerialPortList.getPortNames();
        if(cmbPort.getItems().size() > 0){
            cmbPort.getItems().clear();
        }
        
        int indexCmb = 0;
        
        for (int i=0; i<= PLCModbus.dev_list.length -1 ; i++){
            cmbPort.getItems().add(PLCModbus.dev_list[i]);
            
            if (cmbPort.getItems().size() > 0)
            {
                if (startup)
                {
                    System.out.println(cmbPort.getItems().get(i).toString());
                    if (PLCModbus.dev_list[i].equals(String.valueOf("/dev/ttyUSB0")))
                    {
                        indexCmb = i;
                        cmbPort.getSelectionModel().select(indexCmb);
                        btnConnect.fire();
                    }
                    else{
                        cmbPort.setValue(PLCModbus.dev_list[0]);
                    }
                }
                else{
                    cmbPort.setValue(PLCModbus.dev_list[0]);
                }
            }
        }
    }

    @FXML
    private void btnDisconnect_clicked(ActionEvent event) {
        if (PLCModbus.pickingIsRunning)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            stage = (Stage) form1.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Cannot Disconnect while picking is running");
            alert.showAndWait();
        }
        else
        {
            try
            {
                timer1.cancel();
                myTask.cancel();
                PLCModbus.DisconnectToSlave();
            } catch (Exception ex)
            {

            }
            cmbPort.setDisable(false);
            btnConnect.setDisable(false);
            btnDisconnect.setDisable(true);
            btnRefresh.setDisable(false);
            showStatus("...");
        }
        
    }

    
    // Menu left
    @FXML
    private void menuHome_Clicked(ActionEvent event)
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneHome);
    }
    
    public void loadHome()
    {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PanePickStock); //show pane viewstock first
        paneCOM.getParent().toFront();
    }

    @FXML
    private void menuInStock_Clicked(ActionEvent event)  {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneInStock);
    }

    @FXML
    private void menuOutStock_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PanePickStock);
    }

    @FXML
    private void menuLayout_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneLayout);
    }

    @FXML
    private void menuSettings_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneSetting);
    }
    
    @FXML
    private void menuBar_Close_Clicked(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Are you sure to Exit?");
        alert.setContentText("Yes = Exit, No = Minimize");
        stage = (Stage) form1.getScene().getWindow();
        alert.initOwner(stage);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
        alert.showAndWait().ifPresent(type ->
        {
            if (type.getText() == "Yes")
            {
                // exit app
                Platform.exit();
                System.exit(0);
            }
            else if (type.getText() == "No")
            {
                // hide app
                //get this stage 
                stage = (Stage) form1.getScene().getWindow();
                //minimize form
                stage.setIconified(true);
            }
            else
            {
            }
        });
    }

    
    
}
