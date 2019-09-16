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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import static javafxappjlibmodbus.FXMLDocumentController.stage;
import static javafxappjlibmodbus.FXMLInputRackController.getRow;
import javax.persistence.Table;
import org.sql2o.Connection;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLInputStockController implements Initializable
{

    @FXML
    private Button BtnLoad;
    @FXML
    private TableView<dao.stock_picking> TblStock;
    @FXML
    private TextField txtPartNo;
    @FXML
    private TextField txtQtyStock;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

    private static int getRow = 0;
    @FXML
    private AnchorPane fxInputstock;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
        // TODO
        TblStock.setEditable(false);
        TblStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn kol_partno = new TableColumn("PartNo");
        kol_partno.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_partno.setCellValueFactory(new PropertyValueFactory<Table, String>("PartNo"));
        kol_partno.setSortable(false);

        TableColumn kol_qtystock = new TableColumn("QtyStock");
        kol_qtystock.setMaxWidth(1f * Integer.MAX_VALUE * 15); // 30% width
        kol_qtystock.setCellValueFactory(new PropertyValueFactory<Table, Integer>("QtyStock"));
        kol_qtystock.setSortable(false);

        // add all column to tableview
        TblStock.getColumns().addAll(kol_partno,kol_qtystock);
    }    

    @FXML
    private void BtnLoad_clicked(ActionEvent event)
    {
        loadAll();
    }

    private void loadAll(){
        List<dao.stock_picking> lst = getAllData();
        ObservableList<dao.stock_picking> data = FXCollections.observableArrayList(lst);
        TblStock.setItems(data);
        setRowTable(0); getValueData(0);
    }
    
    private List<dao.stock_picking> getAllData(){
        String sql = "select PartNo,QtyStock from stock_picking";
        
        try(Connection con = dao.conf.sql2o.open()) {
        return con.createQuery(sql)
            .executeAndFetch(dao.stock_picking.class);
        }
    }
    
    private void setRowTable(Integer row)
    {
        Platform.runLater(() ->
        {
            TblStock.requestFocus();
            TblStock.getSelectionModel().select(row);
            TblStock.scrollTo(row);
        });
    }
    
    

    // Clear row table selection
    private void setClearRowSelectionTable()
    {
        Platform.runLater(() ->
        {
            TblStock.getSelectionModel().clearSelection();
        });
    }
    
    // get cell value by column and row
    public String getValueAt(int column, int row)
    {
        return TblStock.getColumns().get(column).getCellObservableValue(row).getValue().toString();
    }

    
    @FXML
    private void btnAdd_clicked(ActionEvent event)
    {
        try
        {
            if (txtPartNo.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputstock.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data PartNo tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            if (txtQtyStock.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputstock.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Qty tidak boleh kosong");  alert.showAndWait();
                return;
            }
            
            dao.stock_picking lst = new dao.stock_picking(txtPartNo.getText(), Integer.valueOf(txtQtyStock.getText()));
            insertPartStock(lst);
            loadAll();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputstock.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }

    @FXML
    private void btnUpdate_clicked(ActionEvent event)
    {
        try
        {
            if (txtPartNo.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputstock.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data PartNo tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            if (txtQtyStock.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputstock.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data Qty tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            updatePartStock(txtPartNo.getText(), Integer.valueOf(txtQtyStock.getText()));
            loadAll();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputstock.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }

    @FXML
    private void btnDelete_clicked(ActionEvent event)
    {
        try
        {
            if (txtPartNo.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
                Stage stage = (Stage) fxInputstock.getScene().getWindow();alert.initOwner(stage);
                alert.setHeaderText("Data PartNo tidak boleh kosong"); alert.showAndWait();
                return;
            }
            
            getRow = TblStock.getSelectionModel().getSelectedIndex();
            
            deletePartStock(String.valueOf(bacaTable(getRow).getPartNo()));
            loadAll();
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR); alert.setTitle("Error");
            Stage stage = (Stage) fxInputstock.getScene().getWindow(); alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage()); alert.showAndWait();
        }
    }

    @FXML
    private void TblStock_mouseclicked(MouseEvent event)
    {
        getValueData();
    }
    
    // insert
    public void insertPartStock(dao.stock_picking stockpick)
    {
        final String insertQuery
                = "INSERT INTO `stock_picking`(`PartNo`, `QtyStock`) VALUES (:partno, :qty)";
       

        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(insertQuery)
                    .addParameter("partno", stockpick.getPartNo())
                    .addParameter("qty", stockpick.getQtyStock())
                    .executeUpdate();
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Done");
            alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    // update
    public void updatePartStock(String IpartNo,int Iqtystock)
    {
        final String updateQuery
                = "UPDATE `stock_picking` SET `QtyStock` = :qtystock WHERE `PartNo` = :partno ";
       

        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(updateQuery)
                    .addParameter("qtystock", Iqtystock)
                    .addParameter("partno", IpartNo)
                    .executeUpdate();
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Done");
            alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    // delete
    public void deletePartStock(String IpartNo)
    {
        final String deleteQuery
                = "DELETE from stock_picking WHERE `PartNo` = :partno ";
       
        try (Connection con = dao.conf.sql2o.beginTransaction())
        {
            con.createQuery(deleteQuery)
                    .addParameter("partno", IpartNo)
                    .executeUpdate();
            // Remember to call commit() when a transaction is opened,
            // default is to roll back.
            con.commit();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText("Done");
            alert.showAndWait();
        }
        catch(Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            Stage stage;
            stage = (Stage) fxInputstock.getScene().getWindow();
            alert.initOwner(stage);
            alert.setHeaderText(ex.getMessage());
            alert.showAndWait();
        }
    }
    
    private void getValueData(){
         getRow =  TblStock.getSelectionModel().getSelectedIndex();
         txtPartNo.setText(String.valueOf(bacaTable(getRow).getPartNo()));
         txtQtyStock.setText(String.valueOf(bacaTable(getRow).getQtyStock()));
    }
    
    private void getValueData(int row){
         txtPartNo.setText(String.valueOf(bacaTable(row).getPartNo()));
         txtQtyStock.setText(String.valueOf(bacaTable(row).getQtyStock()));
    }
    
    private dao.stock_picking bacaTable(int row)
    {
        String PartNo = TblStock.getColumns().get(0).getCellObservableValue(row).getValue().toString();
        int qtystock = (Integer) TblStock.getColumns().get(1).getCellObservableValue(row).getValue();

        dao.stock_picking lst = new dao.stock_picking(PartNo,qtystock);
        return lst;
    }
}
