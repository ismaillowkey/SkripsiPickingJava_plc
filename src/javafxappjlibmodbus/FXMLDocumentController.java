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
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryJSSC;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;
import java.io.IOException;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
//import static javafxappjlibmodbus.FXMLDocumentController.sp;
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

    @FXML
    private void menuBar_Close_Clicked(ActionEvent event) {
        /*
        //get this stage 
        stage = (Stage)form1.getScene().getWindow();
        //minimize form
        stage.setIconified(true);
        */
        Platform.exit();
        System.exit(0);
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
        PLCModbus.LastOpenPane = "pickstock";
    }

    @FXML
    private void menuInStock_Clicked(ActionEvent event)  {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneInStock);
        PLCModbus.LastOpenPane = "instock";
                
        /*
        if ((PLCModbus.LastOpenPane).equals("home")){
            //try
            //{
                //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLInStock.fxml"));
                //newPane = FXMLLoader.load(getClass().getResource("FXMLInStock.fxml"));
                mainPane.getChildren().clear();
                mainPane.getChildren().add(PaneInStock);
                PLCModbus.LastOpenPane = "instock";
            //} catch (IOException ex)
            //{
            //    System.out.println(ex.getMessage());
            //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
        
        else if (!(PLCModbus.LastOpenPane).equals("instock")){
            //alert change pane
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            stage = (Stage)form1.getScene().getWindow();
            alert.initOwner(stage);
            String s = "Processing will exit. Are You Sure";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                // Change Pane
                //Pane newPane;
                //try {
                    //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLInStock.fxml"));
                    //newPane = FXMLLoader.load(getClass().getResource("FXMLInStock.fxml"));
                    
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(PaneInStock);
                    PLCModbus.LastOpenPane = "instock";
                //} catch (IOException ex) {
                //    System.out.println(ex.getMessage());
                //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                //}
            }
        }
        */
    }

    @FXML
    private void menuOutStock_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PanePickStock);
        PLCModbus.LastOpenPane = "pickstock";
              
        /*
        if ((PLCModbus.LastOpenPane).equals("home")){
            //try
            //{
                //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLPickStock.fxml"));
                //newPane = FXMLLoader.load(getClass().getResource("FXMLPickStock.fxml"));
                mainPane.getChildren().clear();
                mainPane.getChildren().add(PanePickStock);
                PLCModbus.LastOpenPane = "pickstock";
            //} 
            //catch (IOException ex)
            //{
            //    System.out.println(ex.getMessage());
            //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
        
        else if (!(PLCModbus.LastOpenPane).equals("pickstock")){
            //alert change pane
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            stage = (Stage)form1.getScene().getWindow();
            alert.initOwner(stage);
            String s = "Processing will exit. Are You Sure";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                // Change Pane
                //Pane newPane;
                //try {
                    //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLPickStock.fxml"));
                    //newPane = FXMLLoader.load(getClass().getResource("FXMLPickStock.fxml"));
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(PanePickStock);
                    PLCModbus.LastOpenPane = "pickstock";
                //} catch (IOException ex) {
                //    System.out.println(ex.getMessage());
                //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                //}
            }
        }
        */
    }

    @FXML
    private void menuLayout_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneLayout);
        PLCModbus.LastOpenPane = "instock";
            
        /*
        if ((PLCModbus.LastOpenPane).equals("home")){
            //try
            //{
                //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLLayout.fxml"));
                //newPane = FXMLLoader.load(getClass().getResource("FXMLLayout.fxml"));
                mainPane.getChildren().clear();
                mainPane.getChildren().add(PaneLayout);
                PLCModbus.LastOpenPane = "instock";
            //} catch (IOException ex)
            //{
            //    System.out.println(ex.getMessage());
            //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
        
        else if (!(PLCModbus.LastOpenPane).equals("layout")){
            //alert change pane
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            stage = (Stage)form1.getScene().getWindow();
            alert.initOwner(stage);
            String s = "Processing will exit. Are You Sure";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                // Change Pane
                //Pane newPane;
                //try {
                    //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLLayout.fxml"));
                    //newPane = FXMLLoader.load(getClass().getResource("FXMLLayout.fxml"));
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(PaneLayout);
                    PLCModbus.LastOpenPane = "layout";
                //} catch (IOException ex) {
                //    System.out.println(ex.getMessage());
                //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                //}
            }
        }
        */
    }

    @FXML
    private void menuSettings_Clicked(ActionEvent event) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(PaneSetting);
        PLCModbus.LastOpenPane = "instock";
           
        /*
        if ((PLCModbus.LastOpenPane).equals("home")){
             //try
             //{
                 //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLSettings.fxml"));
                 //newPane = FXMLLoader.load(getClass().getResource("FXMLSettings.fxml"));
                 mainPane.getChildren().clear();
                 mainPane.getChildren().add(PaneSetting);
                 PLCModbus.LastOpenPane = "instock";
             //} catch (IOException ex)
             //{
             //    System.out.println(ex.getMessage());
             //    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
             //}
        }
        
        else if (!(PLCModbus.LastOpenPane).equals("setting")){
            //alert change pane
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            stage = (Stage)form1.getScene().getWindow();
            alert.initOwner(stage);
            String s = "Processing will exit. Are You Sure";
            alert.setContentText(s);
            Optional<ButtonType> result = alert.showAndWait();
            
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                // Change Pane
                //Pane newPane;
                //try {
                    //Pane newPane = FXMLLoader.load(getClass().getResource("FXMLSettings.fxml"));
                    //newPane = FXMLLoader.load(getClass().getResource("FXMLSettings.fxml"));
                    mainPane.getChildren().clear();
                    mainPane.getChildren().add(PaneSetting);
                    PLCModbus.LastOpenPane = "setting";
            }
        }
        */
    }
    
    
}
