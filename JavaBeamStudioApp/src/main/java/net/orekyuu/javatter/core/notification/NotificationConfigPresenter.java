package net.orekyuu.javatter.core.notification;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
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
import javafx.util.converter.NumberStringConverter;
import net.orekyuu.javatter.api.GlobalAccess;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
    
    private ListProperty<NotificationTypeManager.NotificationConfig> currentConfigProperty = new SimpleListProperty<>();
    private List<NotificationTypeManager.NotificationConfig> previousConfig = new ArrayList<>();
    private BooleanProperty toggleButtonDisableProperty = new SimpleBooleanProperty(false);
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Bindings.bindBidirectional(volumeText.textProperty(), volumeSlider.valueProperty(), new NumberStringConverter());
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        previousConfig = notificationList.getItems();
        currentConfigProperty.bind(notificationList.itemsProperty());
        notificationList.setCellFactory(param -> new ListCell<NotificationTypeManager.NotificationConfig>() {
            @Override
            protected void updateItem(NotificationTypeManager.NotificationConfig item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setGraphic(null);
                } else {
                    Text text = new Text(item.getNotificationType());
                    ToggleButton toggleButton = new ToggleButton(item.isNotice() ? "通知する" : "通知しない");
                    toggleButton.disableProperty().bind(toggleButtonDisableProperty);
                    toggleButton.setSelected(item.isNotice());
                    toggleButton.textProperty().bind(Bindings.when(toggleButton.selectedProperty()).then("通知する").otherwise("通知しない"));
                    toggleButton.selectedProperty().addListener(e ->{ 
                        item.setNotice(toggleButton.isSelected());
                    });
                    
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
        toggleButtonDisableProperty.set(true);
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        typeManager.saveNotificationConfigs(currentConfigProperty);
        toggleButtonDisableProperty.set(false);
    }

    public void cancel() {
        
    }
}
