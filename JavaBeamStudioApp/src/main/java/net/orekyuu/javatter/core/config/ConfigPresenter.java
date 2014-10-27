package net.orekyuu.javatter.core.config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 設定ウィンドウのイベントを受け取るPresenter
 */
public class ConfigPresenter implements Initializable {

    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
