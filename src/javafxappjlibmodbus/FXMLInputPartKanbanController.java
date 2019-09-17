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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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
    @FXML
    private AnchorPane fxInputPartKanban;
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
    
    private void loadAllKanban(){
        getRowPartKanban =  TblPartKanban.getSelectionModel().getSelectedIndex();
        SelectedPartKanban = TblPartKanban.getColumns().get(0).getCellObservableValue(getRowPartKanban).getValue().toString();  
        
        List<tblAllKanban> lst = getDataByPartKanban(SelectedPartKanban);
        ObservableList<tblAllKanban> data = FXCollections.observableArrayList(lst);
        TblAllKanban.setItems(data);
        setRowTableAllKanban(0);
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
    private void btnAdd_clicked(ActionEvent event)
    {
        try
        {
            if (txtSeq.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Seq tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            if (txtIDpicking.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data ID Picking tidak boleh kosong");  alert.showAndWait();
                return;
            }
            
            if (txtQty.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Qty tidak boleh kosong");  alert.showAndWait();
                return;
            }
            
            dao.partlistpicking lst = new dao.partlistpicking(SelectedPartKanban, Integer.valueOf(txtSeq.getText()),Integer.valueOf(txtIDpicking.getText()),Integer.valueOf(txtQty.getText()));
            insertAllPartKanban(lst);
            loadAllKanban();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }
    
    @FXML
    private void btnUpdate_clicked(ActionEvent event)
    {
        try
        {
             if (txtSeq.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Seq tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            if (txtQty.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Qty tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            dao.partlistpicking lst = new dao.partlistpicking(SelectedPartKanban, Integer.valueOf(txtSeq.getText()), Integer.valueOf(txtIDpicking.getText()), Integer.valueOf(txtQty.getText()));
            
            updateAllPartKanban(lst);
            loadAllKanban();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }
    
    @FXML
    private void btnDelete_clicked(ActionEvent event)
    {
        try
        {
            if (txtSeq.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputPartKanban.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Id picking tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            getRowAllKanban = TblAllKanban.getSelectionModel().getSelectedIndex();
            
            deleteAllPartKanban(SelectedPartKanban, Integer.valueOf(bacaTable(getRowAllKanban).getSeq()));
            loadAllKanban();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
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
    
    
    //insert
    public void insertAllPartKanban(dao.partlistpicking partlistpicking)
    {
        final String insertQuery
                = "INSERT INTO`partlist_picking`(`PartKanban`, `Seq`, `IDpicking`, `Qty`) VALUES (:partkanban, :seq,:idpicking, :qty);";
       
        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(insertQuery)
                    .addParameter("partkanban", partlistpicking.getPartKanban())
                    .addParameter("seq", partlistpicking.getSeq())
                    .addParameter("idpicking", partlistpicking.getIDpicking())
                    .addParameter("qty", partlistpicking.getQty())
                    .executeUpdate();
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle("Information");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText("Done"); alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }
    
    // update
    public void updateAllPartKanban(dao.partlistpicking partlistpicking)
    {
        final String updateQuery
                = "UPDATE `partlist_picking` SET `idpicking` = :idpicking , `qty` = :qty WHERE `PartKanban` = :partkanban and `Seq` = :seq";
       
        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(updateQuery)
                    .addParameter("idpicking", partlistpicking.getIDpicking())
                    .addParameter("qty", partlistpicking.getQty())
                    .addParameter("partkanban", partlistpicking.getPartKanban())
                    .addParameter("seq", partlistpicking.getSeq())
                    .executeUpdate();
            
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle("Information");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText("Done"); alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }
    
    // delete
    public void deleteAllPartKanban(String IPartkanban,int ISeq)
    {
        final String deleteQuery
                = "DELETE from `partlist_picking` WHERE `PartKanban` = :partkanban and `Seq` = :seq ";
       
        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(deleteQuery)
                    .addParameter("partkanban", IPartkanban)
                    .addParameter("seq", ISeq)
                    .executeUpdate();
            
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle("Information");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText("Done"); alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputPartKanban.getScene().getWindow(); alert.initOwner(stage); 
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
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
