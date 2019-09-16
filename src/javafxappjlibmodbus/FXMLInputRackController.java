/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
public class FXMLInputRackController implements Initializable
{

    @FXML
    private TableView<dao.partno_picking> TblRack;
    @FXML
    private Button BtnLoad;
    @FXML
    private TextField txtIDpicking;
    @FXML
    private TextField txtPartNo;
    @FXML
    private TextField txtPartName;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    static int getRow = 0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        TblRack.setEditable(false);
        TblRack.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn kol_idpicking = new TableColumn("IDpicking");
        kol_idpicking.setMaxWidth(1f * Integer.MAX_VALUE * 10); // 10% width
        kol_idpicking.setCellValueFactory(new PropertyValueFactory<Table, Integer>("IDpicking"));
        kol_idpicking.setSortable(false);

        TableColumn kol_partno = new TableColumn("PartNo");
        kol_partno.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partno.setCellValueFactory(new PropertyValueFactory<Table, String>("PartNo"));
        kol_partno.setSortable(false);

        TableColumn kol_partname = new TableColumn("PartName");
        kol_partname.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partname.setCellValueFactory(new PropertyValueFactory<Table, String>("PartName"));
        kol_partname.setSortable(false);

        // add all column to tableview
        TblRack.getColumns().addAll(kol_idpicking, kol_partno, kol_partname);
        
    }    
    
    
    @FXML
    private void BtnLoad_clicked(ActionEvent event)
    {
        List<dao.partno_picking> lst = getAllData();
        ObservableList<dao.partno_picking> data = FXCollections.observableArrayList(lst);
        TblRack.setItems(data);
        setRowTable(0); getValueData(0);
    }
    
    private dao.partno_picking bacaTable(int row)
    {
        int Seq = (Integer) TblRack.getColumns().get(0).getCellObservableValue(row).getValue();
        String PartNo = TblRack.getColumns().get(1).getCellObservableValue(row).getValue().toString(); 
        String PartName = TblRack.getColumns().get(2).getCellObservableValue(row).getValue().toString(); 
        
        dao.partno_picking lst = new dao.partno_picking(Seq,PartNo,PartName);
        return lst;
    }
    
    
    
    private List<dao.partno_picking> getAllData(){
        String sql = "select IDpicking,PartNo,PartName from partno_picking";
        
        try(Connection con = dao.conf.sql2o.open()) {
        return con.createQuery(sql)
            .executeAndFetch(dao.partno_picking.class);
        }
    }

     private void setRowTable(Integer row)
    {
        Platform.runLater(() ->
        {
            TblRack.requestFocus();
            TblRack.getSelectionModel().select(row);
            TblRack.scrollTo(row);
        });
    }
    
    

    // Clear row table selection
    private void setClearRowSelectionTable()
    {
        Platform.runLater(() ->
        {
            TblRack.getSelectionModel().clearSelection();
        });
    }
    
    // get cell value by column and row
    public String getValueAt(int column, int row)
    {
        return TblRack.getColumns().get(column).getCellObservableValue(row).getValue().toString();
    }

    
    @FXML
    private void TblRack_mouseclicked(MouseEvent event)
    {
        getValueData();
    }
    
    private void getValueData(){
         getRow =  TblRack.getSelectionModel().getSelectedIndex();
         txtIDpicking.setText(String.valueOf(bacaTable(getRow).getIDpicking()));
         txtPartNo.setText(String.valueOf(bacaTable(getRow).getPartNo()));
         txtPartName.setText(String.valueOf(bacaTable(getRow).getPartName()));
    }
        
    private void getValueData(int row){
         txtIDpicking.setText(String.valueOf(bacaTable(row).getIDpicking()));
         txtPartNo.setText(String.valueOf(bacaTable(row).getPartNo()));
         txtPartName.setText(String.valueOf(bacaTable(row).getPartName()));
    }

    @FXML
    private void btnAdd_clicked(ActionEvent event)
    {
    }

    @FXML
    private void btnUpdate_clicked(ActionEvent event)
    {
    }

    @FXML
    private void btnDelete_clicked(ActionEvent event)
    {
    }
    
}

