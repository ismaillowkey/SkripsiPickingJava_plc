/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static javafxappjlibmodbus.FXMLDocumentController.stage;
import javax.persistence.Table;
import org.hibernate.Query;
import org.hibernate.sql.Select;
import org.sql2o.*;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLPickStockController implements Initializable
{

    @FXML
    private TextField TxtBarcodeScan;
    @FXML
    private TableView<dao.partlistpicking> TblView2;
    @FXML
    private TextField TxtSelectedSeqNo;
    @FXML
    private TextField TxtSelectedPartName;
    @FXML
    private TextField TxtSelectedPartNo;
    @FXML
    private TextField TxtSelectedIDPicking;
    @FXML
    private TextField TxtSelectedQty;
    @FXML
    private Label LblStatusBarcode;
    @FXML
    private Button btnEnter;
    @FXML
    private AnchorPane formpickstock;
    
    private Timer timerloop;
    private TimerTask myTask;

    private Thread thread = null;
   // private listTable lsttable; // for get value of tableview
    private listTable bacaDGV; // for process
    private boolean isThreadRun = false;
    private boolean startPicking = false;
    //private boolean NetworkIsOK = false;
    private boolean checkData = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        bacaDGV = new listTable();
        // disable tableview
        TblView2.setEditable(false);
        TblView2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        

        //TableColumn kol_id = new TableColumn("id");
        //kol_id.setMaxWidth( 1f * Integer.MAX_VALUE * 10 ); // 10% width
        //kol_id.setCellValueFactory(new PropertyValueFactory<Table, String>("id"));
        TableColumn kol_seq = new TableColumn("seq");
        kol_seq.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kol_seq.setCellValueFactory(new PropertyValueFactory<Table, String>("seq"));
        kol_seq.setSortable(false);

        TableColumn kol_partno = new TableColumn("partNo");
        kol_partno.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partno.setCellValueFactory(new PropertyValueFactory<Table, String>("partNo"));
        kol_partno.setSortable(false);

        TableColumn kol_partname = new TableColumn("partName");
        kol_partname.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partname.setCellValueFactory(new PropertyValueFactory<Table, String>("partName"));
        kol_partname.setSortable(false);

        TableColumn kol_idpicking = new TableColumn("IDpicking");
        kol_idpicking.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 30% width
        kol_idpicking.setCellValueFactory(new PropertyValueFactory<Table, String>("IDpicking"));
        kol_idpicking.setSortable(false);

        TableColumn kol_qty = new TableColumn("qty");
        kol_qty.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 30% width
        kol_qty.setCellValueFactory(new PropertyValueFactory<Table, String>("qty"));
        kol_qty.setSortable(false);

        // add all column to tableview
        TblView2.getColumns().addAll(kol_seq, kol_partno, kol_partname, kol_idpicking, kol_qty);
        setFocusTxtBarcodeScan();
    }

    private void setFocusTxtBarcodeScan(){
        Platform.runLater(new Runnable(){
            @Override
            public void run()
            {
                TxtBarcodeScan.requestFocus();
            }
        });
    }
    
    @FXML
    private void TxtBarcodeScan_KeyPressed(KeyEvent event)
    {
        // Jika enter ditekan
        if (event.getCode().equals(KeyCode.ENTER) && (startPicking == false))
        {
            if (PLCModbus.StatusKoneksi)
            {
                MulaiPicking();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                stage = (Stage)formpickstock.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Connect to PLC first");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void btnEnter_click(ActionEvent event) throws ModbusProtocolException
    {
        // Jika enter ditekan
        if (startPicking == false)
        {
            if (PLCModbus.StatusKoneksi)
            {
                MulaiPicking();
            }
            else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                stage = (Stage)formpickstock.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Connect to PLC first");
                alert.showAndWait();
            }
        }
        //cance picking
        else{
            //Selesai picking
            setClearRowSelectionTable(); 
            startPicking = false;
            Timer1_tick(false);
            try { PLCModbus.master.writeSingleCoil(1, LastBacaIDPicking -1, false); } catch (Exception ex) { } 
            TxtBarcodeScan.setDisable(false);
            btnEnter.setText("Enter");
            //btnEnter.setDisable(false);

            setFocusTxtBarcodeScan();

            PLCModbus.pickingIsRunning = false;
            Platform.runLater(() ->
            {
                TxtBarcodeScan.setText("");
                LblStatusBarcode.setText("Canceled By User");
            });
        }
    }
    
    
    private void MulaiPicking()
    {
        //jika data barcode yang dimasukkan kosong
        if (TxtBarcodeScan.getText().isEmpty())
        {
            //System.out.println("Data barcode kosong");
            Platform.runLater(() ->
            {
                LblStatusBarcode.setText("Isi Barcode kosong");
            });
        }
        else
        {
            try
            {
                LblStatusBarcode.setText("...");

                TxtBarcodeScan.setDisable(true);
                btnEnter.setText("Cancel");
                //btnEnter.setDisable(true);

                
                Runnable task = () ->
                {
                    Platform.runLater(() ->
                        {
                            LblStatusBarcode.setText("Get data from database");
                        });
                    
                    List<dao.partlistpicking> lst = getAllData(TxtBarcodeScan.getText());
                    
                    
                    if (lst.size() == 0){               
                        Platform.runLater(() ->
                        {
                            LblStatusBarcode.setText("Part Kanban Tidak Ditemukan");             
                            btnEnter.setText("Enter");
                        });
                        TxtBarcodeScan.setDisable(false);
                        checkData = false;
                    }
                    else{
                        checkData = true;
                        ObservableList<dao.partlistpicking> data = FXCollections.observableArrayList(lst);

                        //binding to tableview
                        TblView2.setItems(data);
                        
                        //cek jika stok kurang
                        for (int i=0; i<= data.size() - 1 ; i++){
                            int cek = data.get(i).getQtyStock() - data.get(i).getQty();
                            int x = i;
                            if (cek < 0 ){
                                Platform.runLater(() ->
                                {
                                    LblStatusBarcode.setText("Error : PartNo " + data.get(x).getPartNo() + " Kurang " + cek);
                                    TxtBarcodeScan.setDisable(false);
                                    TxtBarcodeScan.setText("");
                                    //btnEnter.setDisable(false);
                                    btnEnter.setText("Enter");
                                });
                                
                                checkData = false;
                                return;
                            }
                        }

                        TxtSelectedSeqNo.setText(String.valueOf(bacaTable(0).getSeq()));
                        TxtSelectedPartNo.setText(String.valueOf(bacaTable(0).getPartNo()));
                        TxtSelectedPartName.setText(String.valueOf(bacaTable(0).getPartName()));
                        TxtSelectedIDPicking.setText(String.valueOf(bacaTable(0).getIdpicking()));
                        TxtSelectedQty.setText(String.valueOf(bacaTable(0).getQty()));

                        //highlight to row 0
                        setRowTable(0);
                        bacaDGV.seq = 1; //set to seq 1 (start from 1)
                        LastBacaIDPicking = 1;

                        seq = bacaDGV.seq;

                        bacaDGV.seq = bacaTable(seq - 1).seq; //column Seq
                        bacaDGV.IDpicking = bacaTable(seq - 1).IDpicking; //column id picking
                        seq = bacaDGV.seq;

                        startPicking = true;
                        StatusBaca = false;// isThreadRun = false;

                        try
                        {
                            //reset seq 4x1 = R0 to 1
                            PLCModbus.master.writeSingleRegister(1, 1 - 1, 1);
                            //set seq 4x2 = R1
                            PLCModbus.master.writeSingleRegister(1, 2 - 1, bacaDGV.IDpicking);
                            for (int i = 1; i <= 20; i++)
                            {
                                PLCModbus.master.writeSingleCoil(1, i - 1, false);
                            }

                        } catch (Exception ex)
                        {
                        }
                        startReading();
                        PLCModbus.pickingIsRunning = true;
                        Platform.runLater(() ->
                        {
                            LblStatusBarcode.setText("Running...");
                        });
                    }

                };
                new Thread(task).start();
                
                
                

            } catch (Exception ex)
            {
                System.out.println("error | " + ex);
            }
        }    
    }
         
    private List<dao.stock_picking> getStockByPartNo(String partno){
        String sql = "SELECT `PartNo`, `QtyStock` FROM stock_picking WHERE `PartNo`=:partno"; 
        
        try (Connection con = dao.conf.sql2o.open())
        {
            return con.createQuery(sql)
                    .addParameter("partno", partno)
                    .executeAndFetch(dao.stock_picking.class);
        }
    }
    
    private void updateStockByPartNo(String partno,int qtystock){
        String sql = "UPDATE stock_picking SET `QtyStock`=:qtystock  WHERE `PartNo`=:partno"; 
        
        try (Connection con = dao.conf.sql2o.open())
        {
            con.createQuery(sql)
                    .addParameter("qtystock", qtystock)
                    .addParameter("partno", partno)
                    .executeUpdate();
        }
    }
    
    
    
    private List<dao.partlistpicking> getAllData(String kodebarcode){
        String sql = "SELECT seq, PartNo, PartName, IDpicking,qty, QtyStock " + 
                     "FROM partlist_picking " +
                     "INNER JOIN partno_picking USING(IDpicking) " +
                     "INNER JOIN stock_picking using (partno) " +
                     "WHERE PartKanban = :kodebarcode ORDER BY seq";
        
        try(Connection con = dao.conf.sql2o.open()) {
        return con.createQuery(sql)
            .addParameter("kodebarcode", kodebarcode)
            .executeAndFetch(dao.partlistpicking.class);
        }
    }

    private int seq;
    private int LastBacaIDPicking = 0;
    private boolean StatusBaca = false;
    private Timer timer1;
    private boolean LastState = false;

    private void startReading()
    {
        // Start the background thread, periodically read the data in the plc, and then display in the curve control
        if (!isThreadRun)
        {
            isThreadRun = true;
            //loop
            timerloop = new Timer();
            myTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    //ThreadReadServer      
                    if (PLCModbus.StatusKoneksi == true)
                    {
                        while (isThreadRun)
                        {
                            try
                            {
                                Thread.sleep(200);
                                if (startPicking)
                                {
                                    // Read Seq and Write to Y output
                                    Runnable task = () ->
                                    {
                                        try
                                        {
                                            // Baca Seq di 4x1/R0
                                            int[] val = PLCModbus.master.readHoldingRegisters(1, 1 - 1, 1);
                                            seq = val[0];

                                            if ((seq > 0) && (StatusBaca == false))
                                            {
                                                StatusBaca = true;
                                                // start timer task to blink Y output
                                                Timer1_tick(true);
                                            }

                                            else if ((seq == 0) && (StatusBaca == true))
                                            {
                                                StatusBaca = false;
                                                // stop timer task to blink Y output
                                                Timer1_tick(false);
                                            }
                                        } catch (Exception ex)
                                        {

                                        }

                                        if (bacaDGV.IDpicking != LastBacaIDPicking)
                                        {
                                            try
                                            {
                                                //set off last id picking Y output
                                                PLCModbus.master.writeSingleCoil(1, LastBacaIDPicking -1, false);
                                            } catch (Exception ex)
                                            {
                                            }
                                        }
                                        LastBacaIDPicking = bacaDGV.IDpicking;

                                    };
                                    new Thread(task).start();

                                    // Read Seq and read X input
                                    Runnable task1 = () ->
                                    {
                                        try
                                        {
                                            // Baca seq (Register R0)
                                            int[] ReadR0 = PLCModbus.master.readHoldingRegisters(1, 1 - 1, 1);

                                            
                                            if (isThreadRun)
                                            {
                                                // Baca X input berdasarkan id picking
                                                boolean[] ReadXInput = PLCModbus.master.readCoils(1, (1000 + bacaDGV.IDpicking) -1, 1);
                                                //Store in variable value
                                                boolean value = ReadXInput[0];
                                                
                                                int valueR0 = ReadR0[0];
                                                
                                                //jika tombol ditekan maka increment 1 di 4x1/R0
                                                if (value)
                                                {
                                                    //Inc Seq + 1 4x1/R0
                                                    //seq = seq + 1;
                                                    valueR0 = valueR0 +1;
                                                    seq = valueR0;
                                                    
                                                    // Jika seq < jumlah row tableview maka lanjut seq
                                                    if (seq <= TblView2.getItems().size())
                                                    {
                                                        TxtSelectedSeqNo.setText(String.valueOf(bacaTable(seq - 1).getSeq()));
                                                        TxtSelectedPartNo.setText(String.valueOf(bacaTable(seq - 1).getPartNo()));
                                                        TxtSelectedPartName.setText(String.valueOf(bacaTable(seq - 1).getPartName()));
                                                        TxtSelectedIDPicking.setText(String.valueOf(bacaTable(seq - 1).getIdpicking()));
                                                        TxtSelectedQty.setText(String.valueOf(bacaTable(seq - 1).getQty()));

                                                        // inc and write to 4x1/R0
                                                        PLCModbus.master.writeSingleRegister(1, 1 - 1, seq);
                                                    
                                                        // write id picking to 4x2/R1
                                                        bacaDGV.IDpicking = bacaTable(seq - 1).IDpicking;
                                                        PLCModbus.master.writeSingleRegister(1, 2 - 1,bacaDGV.IDpicking);
                                                        
                                                        System.out.println("update stock");
                                                        
                                                        //update stock
                                                        String selectedPartNo = TxtSelectedPartNo.getText();
                                                        List<dao.stock_picking> lst = getStockByPartNo(selectedPartNo);
                                                        ObservableList<dao.stock_picking> data = FXCollections.observableArrayList(lst);
                                                        int lastStock = data.get(0).getQtyStock();
                                                        int kurangiStock = Integer.valueOf(TxtSelectedQty.getText());
                                                        lastStock = lastStock - kurangiStock;
                                                        updateStockByPartNo(selectedPartNo, lastStock);
                                                        
                                                        //highlight to row seq -1
                                                        setRowTable(seq - 1);
                                                    }
                                                    else
                                                    {
                                                        //Selesai picking
                                                        setClearRowSelectionTable();
                                                        startPicking = false;
                                                        Timer1_tick(false);
                                                        TxtBarcodeScan.setDisable(false);
                                                        
                                                        //btnEnter.setDisable(false);
                                                        
                                                        setFocusTxtBarcodeScan();
                                                        
                                                        PLCModbus.pickingIsRunning = false;
                                                        Platform.runLater(() ->
                                                        {
                                                            btnEnter.setText("Enter");
                                                            TxtBarcodeScan.setText("");
                                                            LblStatusBarcode.setText("Done");
                                                        });
                                                        
                                                        
                                                    }
                                                }
                                            }

                                        } catch (Exception ex)
                                        {
                                            System.err.println(ex.getMessage());
                                        }

                                    };
                                    new Thread(task1).start();

                                }
                            } catch (Exception e)
                            {
                            }
                        }
                    }
                }
            };
            timerloop.schedule(myTask, 100, 100);
        }
        /*
        else
        {
            isThreadRun = false;
        }
        */
    }

    private Timer timer1_loop;
    private TimerTask timer1_task;

    private void Timer1_tick(boolean start) throws ModbusProtocolException
    {
        if (start)
        {
            timer1_loop = new Timer();
            timer1_task = new TimerTask()
            {
                @Override
                public void run()
                {
                    if ((seq > 0) && (startPicking == true))
                    {
                        try
                        {
                            PLCModbus.master.writeSingleCoil(1, bacaDGV.IDpicking -1, LastState);
                        } catch (Exception ex)
                        {
                        }
                        LastState = !LastState;
                    }
                }
            };
            timer1_loop.schedule(timer1_task, 500,500);
        }
        else
        {
            try
            {
                PLCModbus.master.writeSingleCoil(1, LastBacaIDPicking -1, false);
            } catch (Exception ex)
            {
            } 
            timer1_loop.cancel();
        }

    }

    // get value from selected cell
    private listTable bacaTable(int row)
    {
        int setSeq = (Integer) TblView2.getColumns().get(0).getCellObservableValue(row).getValue();
        String setPartNo = ""; String setPartName = ""; int setQty = 1;
        try { setPartNo = TblView2.getColumns().get(1).getCellObservableValue(row).getValue().toString(); } catch(Exception ex){ }
        try { setPartName = TblView2.getColumns().get(2).getCellObservableValue(row).getValue().toString(); } catch(Exception ex){ }
        int setIdpicking = (Integer) TblView2.getColumns().get(3).getCellObservableValue(row).getValue();
        try { setQty = (Integer) TblView2.getColumns().get(4).getCellObservableValue(row).getValue(); } catch(Exception ex){ }
        listTable lst = new listTable(setSeq, setPartNo, setPartName, setIdpicking, setQty);
        return lst;
    }

    // Set row table selection
    private void setRowTable(Integer row)
    {
        Platform.runLater(() ->
        {
            TblView2.requestFocus();
            TblView2.getSelectionModel().select(row);
            TblView2.scrollTo(row);
        });
    }
    
    

    // Clear row table selection
    private void setClearRowSelectionTable()
    {
        Platform.runLater(() ->
        {
            TblView2.getSelectionModel().clearSelection();
        });
    }

    // get cell value by column and row
    public String getValueAt(int column, int row)
    {
        return TblView2.getColumns().get(column).getCellObservableValue(row).getValue().toString();
    }

    

    private class listTable
    {

        private int seq;
        private String partNo;
        private String partName;
        private int IDpicking;
        private int qty;

        public listTable(int seq, String partNo, String partName, int idpicking, int qty)
        {
            this.seq = seq;
            this.partNo = partNo;
            this.partName = partName;
            this.IDpicking = idpicking;
            this.qty = qty;
        }

        private listTable()
        {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        public Integer getSeq()
        {
            return this.seq;
        }

        public String getPartNo()
        {
            return this.partNo;
        }

        public String getPartName()
        {
            return this.partName;
        }

        public Integer getIdpicking()
        {
            return this.IDpicking;
        }

        public Integer getQty()
        {
            return this.qty;
        }
    }
}
