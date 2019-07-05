/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import static javafxappjlibmodbus.FXMLDocumentController.timer1;
import javax.persistence.Table;
import org.hibernate.Query;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLPickStockController implements Initializable {
    @FXML
    private TextField TxtBarcodeScan;
    @FXML
    private TableView<pojos.Partlist> TblView2;
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

    private Timer timerloop;
    private TimerTask myTask;
    
    private Thread thread = null;
    private listTable lsttable;
    private boolean isThreadRun = false;
    private boolean startPicking = false;
    private boolean NetworkIsOK = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        TblView2.setEditable(false);
        TblView2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //disable row selection
        //TblView2.setSelectionModel(null);
        //TblView2.setDisable(true);

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

        TableColumn kol_idpicking = new TableColumn("idpicking");
        kol_idpicking.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 30% width
        kol_idpicking.setCellValueFactory(new PropertyValueFactory<Table, String>("idpicking"));
        kol_idpicking.setSortable(false);

        TableColumn kol_qty = new TableColumn("qty");
        kol_qty.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 30% width
        kol_qty.setCellValueFactory(new PropertyValueFactory<Table, String>("qty"));
        kol_qty.setSortable(false);

        //TblView2.getColumns().addAll(kol_id,kol_seq,kol_partno,kol_partname,kol_idpicking,kol_qty);
        TblView2.getColumns().addAll(kol_seq, kol_partno, kol_partname, kol_idpicking, kol_qty);
    }

    @FXML
    private void TxtBarcodeScan_KeyPressed(KeyEvent event) {
        // Jika enter ditekan
        if (event.getCode().equals(KeyCode.ENTER)) {
            if (TxtBarcodeScan.getText().isEmpty()) {
                System.out.println("Data barcode kosong");
                Platform.runLater(() -> {
                    LblStatusBarcode.setText("Isi Barcode kosong");
                });
            } else {
                try {
                    LblStatusBarcode.setText("...");

                    
                    Runnable task = () -> {
                        //open session
                        PLCModbus.session_mysql = connection.Controller.getSessionFactory().openSession();
                        // create hql
                        String hql = "from Partlist";
                        Query q = PLCModbus.session_mysql.createQuery(hql);
                        // fill to pojo
                        List<pojos.Partlist> lst = q.list();
                        ObservableList<pojos.Partlist> data = FXCollections.observableArrayList(lst);

                        //binding to tableview
                        TblView2.setItems(data);
                        //highlight to row 0
                        setRowTable(0);

                        TxtSelectedSeqNo.setText(String.valueOf(bacaTable(0).getSeq()));
                        TxtSelectedPartNo.setText(String.valueOf(bacaTable(0).getPartNo()));
                        TxtSelectedPartName.setText(String.valueOf(bacaTable(0).getPartName()));
                        TxtSelectedIDPicking.setText(String.valueOf(bacaTable(0).getIdpicking()));
                        TxtSelectedQty.setText(String.valueOf(bacaTable(0).getQty()));
                    };
                    new Thread(task).start();
                    startReading();
                    
                } catch (Exception ex) {
                    System.out.println("error | " + ex);
                }
            }
        }
    }

    private void startReading(){
        // Start the background thread, periodically read the data in the plc, and then display in the curve control
        if (!isThreadRun) {
            isThreadRun = true;
            startPicking = true;
            //loop
            timerloop = new Timer();
            myTask = new TimerTask() {
                @Override
                public void run() {
                    //ThreadReadServer      
                    if(PLCModbus.StatusKoneksi == true){
                        while(isThreadRun){
                            try{
                                Thread.sleep(200);
                                if(startPicking){
                                    
                                    
                                    Runnable task = () -> {
                                        System.out.println("working 1...");
                                        //handleRequest(connection);
                                    };
                                    new Thread(task).start();
                                    
                                    Runnable task1 = () -> {
                                        System.out.println("working 2...");
                                        //handleRequest(connection);
                                    };
                                    new Thread(task1).start();
                                    
                                }
                            }
                            catch(Exception e){
                                
                            }
                        }
                    }
                    
                    
                }
            };
            timerloop.schedule(myTask, 100, 100);
            
            
        } else {
            isThreadRun = false;
        }
    }
    
        
    
    // get value from selected cell
    private listTable bacaTable(int row) {
        int setSeq = (Integer) TblView2.getColumns().get(0).getCellObservableValue(row).getValue();
        String setPartNo = TblView2.getColumns().get(1).getCellObservableValue(row).getValue().toString();
        String setPartName = TblView2.getColumns().get(2).getCellObservableValue(row).getValue().toString();
        int setIdpicking = (Integer) TblView2.getColumns().get(3).getCellObservableValue(row).getValue();
        int setQty = (Integer) TblView2.getColumns().get(4).getCellObservableValue(row).getValue();
        listTable lst = new listTable(setSeq, setPartNo, setPartName, setIdpicking, setQty);
        return lst;
    }

    // Set row table selection
    private void setRowTable(Integer row) {
        Platform.runLater(()
                -> {
            TblView2.requestFocus();
            TblView2.getSelectionModel().select(row);
            TblView2.scrollTo(row);
        });
    }

    // Clear row table selection
    private void setClearRowSelectionTable() {
        Platform.runLater(() -> {
            TblView2.getSelectionModel().clearSelection();
        });
    }

    // get cell value by column and row
    public String getValueAt(int column, int row) {
        return TblView2.getColumns().get(column).getCellObservableValue(row).getValue().toString();
    }

    private class listTable {

        private final int seq;
        private final String partNo;
        private final String partName;
        private final int idpicking;
        private final int qty;

        public listTable(int seq, String partNo, String partName, int idpicking, int qty) {
            this.seq = seq;
            this.partNo = partNo;
            this.partName = partName;
            this.idpicking = idpicking;
            this.qty = qty;
        }

        public Integer getSeq() {
            return this.seq;
        }

        public String getPartNo() {
            return this.partNo;
        }

        public String getPartName() {
            return this.partName;
        }

        public Integer getIdpicking() {
            return this.idpicking;
        }

        public Integer getQty() {
            return this.qty;
        }
    }
}
