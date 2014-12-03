package net.orekyuu.javatter.core.notification;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.notification.NotificationType;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationConfigPresenter implements Initializable {
    @FXML
    private Text volumeText;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Text notificationSoundFileName;
    @FXML
    private ListView<NotificationTypeManager.NotificationConfig> notificationList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Bindings.bindBidirectional(volumeText.textProperty(), volumeSlider.valueProperty(), new NumberStringConverter());
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        notificationList.setCellFactory(param -> new ListCell<NotificationTypeManager.NotificationConfig>() {
            @Override
            protected void updateItem(NotificationTypeManager.NotificationConfig item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setGraphic(null);
                } else {
                    Text text = new Text(item.getNotificationType());
                    ToggleButton toggleButton = new ToggleButton(item.isNotice() ? "通知する" : "通知しない");
                    toggleButton.setSelected(item.isNotice());
                    toggleButton.textProperty().bind(Bindings.when(toggleButton.selectedProperty()).then("通知する").otherwise("通知しない"));
                    toggleButton.selectedProperty().addListener(e -> item.setNotice(toggleButton.isSelected()));
                    Pane grow = new Pane();
                    HBox.setHgrow(grow, Priority.ALWAYS);
                    setGraphic(new HBox(text, grow, toggleButton));
                }
            }
        });

        typeManager.getConfigs().forEach(notificationList.getItems()::add);
        typeManager.getConfigs().forEach(conf -> System.out.println(conf.getNotificationType()));

    }

    public void selectNotificationSound() {

    }

    public void save() {

    }

    public void cancel() {

    }
}
