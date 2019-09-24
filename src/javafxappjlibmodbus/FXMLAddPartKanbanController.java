/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
public class FXMLAddPartKanbanController implements Initializable
{

    @FXML
    private AnchorPane fxAddPartKanban;
    @FXML
    private TableView<dao.partlistpicking> TblAddPartKanban;
    @FXML
    private TextField txtPartKanban;
    @FXML
    private TextField txtSeq;
    @FXML
    private TextField txtIDPicking;
    @FXML
    private TextField txtQty;
    @FXML
    private Button btnInsert;

    private String SelectedPartKanban;
    private int SelectedSeq,getRow;
    private String AddPartKanban;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        TblAddPartKanban.setEditable(false);
        TblAddPartKanban.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn kol_partkanban = new TableColumn("PartKanban");
        kol_partkanban.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partkanban.setCellValueFactory(new PropertyValueFactory<Table, String>("PartKanban"));
        kol_partkanban.setSortable(false);
        
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
        TblAddPartKanban.getColumns().addAll(kol_partkanban,kol_seq,kol_idpicking,kol_qty);
        
        DisableEdited(true);
    }    

    @FXML
    private void txtPartKanban_KeyPressed(KeyEvent event)
    {
        // Jika enter ditekan
        if (event.getCode().equals(KeyCode.ENTER))
        {
            DisableEdited(true);

            if (txtPartKanban.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Data tidak Boleh Kosong");
                alert.showAndWait();
                txtPartKanban.setText("");
                return;
            }
            
            List<dao.partlistpicking> lst = getCheckPartKanban(txtPartKanban.getText());
            
            if (lst.size() > 0)
            {
                Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
                    alert.initOwner(stage);
                    alert.setHeaderText("Data Sudah Ada");
                    alert.showAndWait();
                    txtPartKanban.setText("");
                    return;
                });
            }
            else
            {
                AddPartKanban = txtPartKanban.getText();
                DisableEdited(false);
            }
        }
    }
    
    @FXML
    private void TblAddPartKanban_mouseclicked(MouseEvent event)
    {
        getRow = TblAddPartKanban.getSelectionModel().getSelectedIndex();
        SelectedPartKanban = String.valueOf(TblAddPartKanban.getColumns().get(0).getCellObservableValue(getRow).getValue());
        SelectedSeq = (Integer) TblAddPartKanban.getColumns().get(1).getCellObservableValue(getRow).getValue();
    }

    private void DisableEdited(boolean status){
        txtSeq.setDisable(status);
        txtIDPicking.setDisable(status);
        txtQty.setDisable(status);
        
        btnInsert.setDisable(status);
    }
    
    @FXML
    private void btnInsert_clicked(ActionEvent event)
    {
        if(CheckAllData()){
            String partkanban = txtPartKanban.getText();
            int seq = Integer.valueOf(txtSeq.getText());
            int idpicking = Integer.valueOf(txtIDPicking.getText());
            int qty = Integer.valueOf(txtQty.getText());
            
            dao.partlistpicking lst = new dao.partlistpicking(partkanban, seq,idpicking , qty);
            AddPartKanban(lst);
            
            List<dao.partlistpicking> getLst = getCheckPartKanban(AddPartKanban);
            System.out.println(getLst.size());
            ObservableList<dao.partlistpicking> data = FXCollections.observableArrayList(getLst);
            TblAddPartKanban.setItems(data);
            
            setRowTableAllKanban(0);
        }
    }

    private void setRowTableAllKanban(Integer row)
    {
        Platform.runLater(() ->
        {
            TblAddPartKanban.requestFocus();
            TblAddPartKanban.getSelectionModel().select(row);
            TblAddPartKanban.scrollTo(row);
        });
    }
    
    
    private boolean CheckAllData(){
        if (txtSeq.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Data Seq tidak boleh kosong");
            alert.showAndWait();
            return false;
        }
        else{
            try {
                int value = Integer.valueOf(txtSeq.getText());
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Data Seq harus berisi Angka");
                alert.showAndWait();
                return false;
            }
        }
        
        if (txtIDPicking.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Data ID Picking tidak boleh kosong");
            alert.showAndWait();
            return false;
        }
        else{
            try {
                int value = Integer.valueOf(txtIDPicking.getText());
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Data ID Picking harus berisi Angka");
                alert.showAndWait();
                return false;
            }
        }
        
        if (txtQty.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Data Qty tidak boleh kosong");
            alert.showAndWait();
            return false;
        }
        else{
            try {
                int value = Integer.valueOf(txtQty.getText());
            }
            catch(Exception ex){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                Stage stage = (Stage) fxAddPartKanban.getScene().getWindow();
                alert.initOwner(stage);
                alert.setHeaderText("Data Qty harus berisi Angka");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
    
    
    private List<dao.partlistpicking> getCheckPartKanban(String partkanban){
        String sql = "Select PartKanban,seq,IDpicking,Qty from partlist_picking Where PartKanban= :partkanban";
        
        try(Connection con = dao.conf.sql2o.open()) {
            return con.createQuery(sql)
                .addParameter("partkanban", partkanban)
                .executeAndFetch(dao.partlistpicking.class);
        }
    }
    
    private void AddPartKanban(dao.partlistpicking lst){
        final String insertQuery
                = "INSERT INTO `partlist_picking`(`PartKanban`, `Seq`, `IDpicking`, `Qty`) VALUES (:partkanban, :seq, :idpicking, :qty);";
       
        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(insertQuery)
                    .addParameter("partkanban", lst.getPartKanban())
                    .addParameter("seq", lst.getSeq())
                    .addParameter("idpicking", lst.getIDpicking())
                    .addParameter("qty", lst.getQty())
                    .executeUpdate();
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION); alert.setTitle("Information");
            Stage stage = (Stage) fxAddPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText("Done"); alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxAddPartKanban.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }
    
}
