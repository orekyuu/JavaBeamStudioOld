package net.orekyuu.javatter.core;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowPresenter implements Initializable {

    @FXML
    private HBox hbox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void addColumn(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        try {
            Node node = loader.load(getClass().getResourceAsStream("column.fxml"));
            HBox.setHgrow(node, Priority.ALWAYS);
            hbox.getChildren().add(node);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionDialogBuilder.create(e);
        }
    }

    public void removeColumn(ActionEvent actionEvent) {
        if (hbox.getChildren().size() != 0)
            hbox.getChildren().remove(hbox.getChildren().size() - 1);
    }
}
