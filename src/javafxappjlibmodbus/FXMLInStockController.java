/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author lupa-nama
 */
public class FXMLInStockController implements Initializable {

    @FXML
    private Button BtnInputPartKanban;
    @FXML
    private Button BtnInputRack;
    @FXML
    private Button BtnInputStock;
    @FXML
    private Pane PaneInput;

    static Pane PaneAddPartKanban,PaneInputPartKanban,PaneInputRack,PaneInputStock;
    @FXML
    private Button BtnAddPartKanban;
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try
        {
            // TODO
            PaneAddPartKanban = FXMLLoader.load(getClass().getResource("FXMLAddPartKanban.fxml"));
            PaneInputPartKanban = FXMLLoader.load(getClass().getResource("FXMLInputPartKanban.fxml"));
            PaneInputRack = FXMLLoader.load(getClass().getResource("FXMLInputRack.fxml"));
            PaneInputStock = FXMLLoader.load(getClass().getResource("FXMLInputStock.fxml"));
        } catch (IOException ex)
        {
            Logger.getLogger(FXMLInStockController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void BtnInputPartKanban_Clicked(ActionEvent event)
    {
        PaneInput.getChildren().clear();
        PaneInput.getChildren().add(PaneInputPartKanban);
    }

    @FXML
    private void BtnInputRack_Clicked(ActionEvent event)
    {
        PaneInput.getChildren().clear();
        PaneInput.getChildren().add(PaneInputRack);
    }

    @FXML
    private void BtnInputStock_Clicked(ActionEvent event)
    {
        PaneInput.getChildren().clear();
        PaneInput.getChildren().add(PaneInputStock);
    }

    @FXML
    private void BtnAddPartKanban_Clicked(ActionEvent event)
    {
        PaneInput.getChildren().clear();
        PaneInput.getChildren().add(PaneAddPartKanban);
    }
    
}
