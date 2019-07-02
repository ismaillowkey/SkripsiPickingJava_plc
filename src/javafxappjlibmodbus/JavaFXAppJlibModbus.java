/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxappjlibmodbus;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jfxtras.styles.jmetro8.JMetro;


/**
 *
 * @author lupa-nama
 */
public class JavaFXAppJlibModbus extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        final File file = new File("flag");
        final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
        final FileLock fileLock = randomAccessFile.getChannel().tryLock();

        System.out.print(fileLock == null);
        if (fileLock == null) {
            Platform.exit();
        }

        
        stage.initStyle(StageStyle.UNDECORATED);
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);  
        Scene scene = new Scene(root); 
        stage.setScene(scene); 
        
        //Single instance application
        stage.setOnCloseRequest(new EventHandler(){
            @Override
            public void handle(Event event) {
                try {
                    fileLock.release();
                    randomAccessFile.close();
                    System.out.println("Closing");
                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }
            }
        });
        
        
        //stage.setMaximized(true); // set maximize form
        stage.setFullScreen(true); // set fullscreen form
        PLCModbus.LastOpenPane = "home";
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
