/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;


/**
 *
 * @author lupa-nama
 */
public class JavaFXAppJlibModbus extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
     
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
       
        Scene scene = new Scene(root);
        
        stage.setScene(scene);        
        //stage.setMaximized(true); // set maximize form
        stage.setFullScreen(true); // set fullscreen form
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
