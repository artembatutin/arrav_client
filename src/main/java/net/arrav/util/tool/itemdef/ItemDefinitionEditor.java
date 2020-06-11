package net.arrav.util.tool.itemdef;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class ItemDefinitionEditor extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        URL location = ClassLoader.getSystemClassLoader().getResource("dev/ItemDefinitionEditorView.fxml");
        Parent root = FXMLLoader.load(location);
        stage.setTitle("Tamatea's item editor");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.getStylesheets().add("dev/style.css");
        stage.show();

    }
}
