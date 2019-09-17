/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import com.sun.javafx.iio.ImageStorage;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.persistence.Table;
import org.sql2o.Connection;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLInputPartKanbanController implements Initializable
{

    @FXML
    private Button BtnLoad;
    @FXML
    private TableView<tblPartKanban> TblPartKanban;
    @FXML
    private TableView<tblAllKanban> TblAllKanban;
    @FXML
    private TextField txtSeq;
    @FXML
    private TextField txtIDpicking;
    @FXML
    private TextField txtQty;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;

    private static int getRowPartKanban = 0;
    private static String SelectedPartKanban ="";
    private static int getRowAllKanban = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        
        TblPartKanban.setEditable(false);
        TblPartKanban.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn kol_partkanban = new TableColumn("PartKanban");
        kol_partkanban.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partkanban.setCellValueFactory(new PropertyValueFactory<Table, String>("PartKanban"));
        kol_partkanban.setSortable(false);

        // add all column to tableview
        TblPartKanban.getColumns().addAll(kol_partkanban);
        
        
        TblAllKanban.setEditable(false);
        TblAllKanban.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn kol_seq = new TableColumn("Seq");
        kol_seq.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_seq.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Seq"));
        kol_seq.setSortable(false);
        
        TableColumn kol_idpicking = new TableColumn("IDpicking");
        kol_idpicking.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_idpicking.setCellValueFactory(new PropertyValueFactory<Table, Integer>("IDpicking"));
        kol_idpicking.setSortable(false);
        
        TableColumn kol_qty = new TableColumn("Qty");
        kol_qty.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_qty.setCellValueFactory(new PropertyValueFactory<Table, Integer>("Qty"));
        kol_qty.setSortable(false);
        
        // add all column to tableview
        TblAllKanban.getColumns().addAll(kol_seq,kol_idpicking,kol_qty);
    }    

    @FXML
    private void BtnLoad_clicked(ActionEvent event)
    {
        loadAll();
        
        SelectedPartKanban = TblPartKanban.getColumns().get(0).getCellObservableValue(0).getValue().toString();  
        List<tblAllKanban> lst = getDataByPartKanban(SelectedPartKanban);
        ObservableList<tblAllKanban> data = FXCollections.observableArrayList(lst);
        TblAllKanban.setItems(data);
        setRowTableAllKanban(0);
    }
    
    private void loadAll(){
        List<tblPartKanban> lst = getAllDataPartKanban();
        ObservableList<tblPartKanban> data = FXCollections.observableArrayList(lst);
        TblPartKanban.setItems(data);
        setRowTablePartKanban(0); getValueDataPartKanban();
    }
    


    @FXML
    private void TblPartKanban_mouseclicked(MouseEvent event)
    {
        getRowPartKanban =  TblPartKanban.getSelectionModel().getSelectedIndex();
        SelectedPartKanban = TblPartKanban.getColumns().get(0).getCellObservableValue(getRowPartKanban).getValue().toString();  
        
        List<tblAllKanban> lst = getDataByPartKanban(SelectedPartKanban);
        ObservableList<tblAllKanban> data = FXCollections.observableArrayList(lst);
        TblAllKanban.setItems(data);
        setRowTableAllKanban(0);
    }


    @FXML
    private void TblAllKanban_mouseclicked(MouseEvent event)
    {
        getRowAllKanban = TblAllKanban.getSelectionModel().getSelectedIndex();
        getValueData(getRowAllKanban);
    }

    @FXML
    private void btnDelete_clicked(ActionEvent event)
    {
    }

    @FXML
    private void btnUpdate_clicked(ActionEvent event)
    {
    }

    @FXML
    private void btnAdd_clicked(ActionEvent event)
    {
    }
    
    private void getValueData(int row){
         txtSeq.setText(String.valueOf(bacaTable(row).getSeq()));
         txtIDpicking.setText(String.valueOf(bacaTable(row).getIDpicking()));
         txtQty.setText(String.valueOf(bacaTable(row).getQty()));
    }
    
    private tblAllKanban bacaTable(int row)
    {
        int Seq = (Integer) TblAllKanban.getColumns().get(0).getCellObservableValue(row).getValue();
        int idpicking = (Integer) TblAllKanban.getColumns().get(1).getCellObservableValue(row).getValue(); 
        int qty = (Integer) TblAllKanban.getColumns().get(2).getCellObservableValue(row).getValue(); 
        
        tblAllKanban lst = new tblAllKanban(Seq,idpicking,qty);
        return lst;
    }
    
    private void setRowTablePartKanban(Integer row)
    {
        Platform.runLater(() ->
        {
            TblPartKanban.requestFocus();
            TblPartKanban.getSelectionModel().select(row);
            TblPartKanban.scrollTo(row);
        });
    }
      
    private void getValueDataPartKanban(){
         getRowPartKanban =  TblPartKanban.getSelectionModel().getSelectedIndex();
    }
    
    private void setRowTableAllKanban(Integer row)
    {
        Platform.runLater(() ->
        {
            TblAllKanban.requestFocus();
            TblAllKanban.getSelectionModel().select(row);
            TblAllKanban.scrollTo(row);
        });
    }
    
    private void getValueDataAllKanban(){
         getRowAllKanban  = TblAllKanban.getSelectionModel().getSelectedIndex();
    }
    
        
    private List<tblPartKanban> getAllDataPartKanban(){
        //String sql = "SELECT `Seq`, `IDpicking`, `Qty` from `partlist_picking`";
        String sql = "SELECT DISTINCT(`PartKanban`) from `partlist_picking`";
        
        try(Connection con = dao.conf.sql2o.open()) {
            return con.createQuery(sql)
                .executeAndFetch(tblPartKanban.class);
        }
    }
    
    private List<tblAllKanban> getDataByPartKanban(String IPartKanban){
        String sql = "SELECT `Seq`, `IDpicking`, `Qty` from `partlist_picking` WHERE `PartKanban` = :partkanban";
        
        try(Connection con = dao.conf.sql2o.open()) {
            return con.createQuery(sql)
                .addParameter("partkanban", IPartKanban)
                .executeAndFetch(tblAllKanban.class);
        }
    }
    
    public class tblAllKanban{
        private int Seq;
        private int IDpicking;
        private int Qty;
        
        public tblAllKanban()
        {
        }

        public tblAllKanban(int Seq, int IDpicking, int Qty)
        {
            this.Seq = Seq;
            this.IDpicking = IDpicking;
            this.Qty = Qty;
        }

        public int getIDpicking()
        {
            return IDpicking;
        }

        public void setIDpicking(int IDpicking)
        {
            this.IDpicking = IDpicking;
        }

        public int getQty()
        {
            return Qty;
        }

        public void setQty(int Qty)
        {
            this.Qty = Qty;
        }

        public int getSeq()
        {
            return Seq;
        }

        public void setSeq(int Seq)
        {
            this.Seq = Seq;
        }        
    }
             
    
    public class tblPartKanban{
        private String PartKanban;

        public tblPartKanban()
        {
        }

        public tblPartKanban(String PartKanban)
        {
            this.PartKanban = PartKanban;
        }

        public String getPartKanban()
        {
            return PartKanban;
        }

        public void setPartKanban(String PartKanban)
        {
            this.PartKanban = PartKanban;
        }
    }
}
