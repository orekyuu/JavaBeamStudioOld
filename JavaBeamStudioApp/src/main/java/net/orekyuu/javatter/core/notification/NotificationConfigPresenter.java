package net.orekyuu.javatter.core.notification;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
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
    private List<BooleanProperty> toggleButtonSelectedproperties = new ArrayList<>();
    private List<Boolean> previousConfig = new ArrayList<>();
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
                    previousConfig.add(item.isNotice());
                    System.out.println(item.getNotificationType()+":"+item.isNotice());
                    BooleanProperty toggleButtonSelectedProperty = toggleButton.selectedProperty();
                    toggleButtonSelectedproperties.add(toggleButtonSelectedProperty);
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
        currentConfigProperty.bind(notificationList.itemsProperty());
        typeManager.getConfigs().forEach(conf -> System.out.println(conf.getNotificationType()));
    }

    public void selectNotificationSound() {

    }

    public void save() {
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        typeManager.saveNotificationConfigs(currentConfigProperty);
        currentConfigProperty.forEach(p -> System.out.println(p.getNotificationType()+":"+p.isNotice()));
    }

    public void cancel() {
        System.out.println(""+previousConfig.size());
        for(int i = 1;i < previousConfig.size();i++){
            toggleButtonSelectedproperties.get(i).setValue(previousConfig.get(i));
            System.out.println("toggleButton"+i+":"+previousConfig.get(i));
        }
        save();
    }
}
