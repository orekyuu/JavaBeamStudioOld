package net.orekyuu.javatter.core.notification;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import net.orekyuu.javatter.api.GlobalAccess;
import java.io.File;
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
    private List<BooleanProperty> toggleButtonSelectedProperties = new ArrayList<>();
    private List<Boolean> previousConfig = new ArrayList<>();
    private NotificationTypeManager.NotificationSoundData previousSoundData = new NotificationTypeManager.NotificationSoundData();
    private NotificationTypeManager.NotificationSoundData currentSoundData = new NotificationTypeManager.NotificationSoundData();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Bindings.bindBidirectional(volumeText.textProperty(), volumeSlider.valueProperty(), new NumberStringConverter());
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        previousSoundData = typeManager.loadNotificationSoundData().orElse(previousSoundData);
        if(typeManager.soundDataIsEmpty()){
            notificationSoundFileName.textProperty().setValue("ファイルを選択してください");
        }else{
            notificationSoundFileName.textProperty().setValue(previousSoundData.getNotificationSoundName());
        }
        volumeText.textProperty().set(String.valueOf(previousSoundData.getNotificationSoundVolume()));
        currentSoundData.setNotificationSoundName(previousSoundData.getNotificationSoundName());
        currentSoundData.setNotificationSoundPath(previousSoundData.getNotificationSoundPath());
        currentSoundData.setNotificationSoundVolume(previousSoundData.getNotificationSoundVolume());
        
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
                    BooleanProperty toggleButtonSelectedProperty = toggleButton.selectedProperty();
                    toggleButtonSelectedProperties.add(toggleButtonSelectedProperty);
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
    }

    public void selectNotificationSound() {
        FileChooser chooser = new FileChooser();
        
        Stage stage = GlobalAccess.getInstance().getApplication().getPrimaryStage();
        
        chooser.setTitle("ファイル選択");
        
        List<String> mp3Extensions = new ArrayList<>();
        mp3Extensions.add("*.mp3");
        mp3Extensions.add("*.MP3");
        List<String> wavExtensions = new ArrayList<>();
        wavExtensions.add("*.wav");
        wavExtensions.add("*.WAV");
        
        FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("MP3 形式サウンド",mp3Extensions);
        FileChooser.ExtensionFilter wavFilter = new FileChooser.ExtensionFilter("WAV 形式サウンド",wavExtensions);
        chooser.getExtensionFilters().add(mp3Filter);
        chooser.getExtensionFilters().add(wavFilter);
        File file = chooser.showOpenDialog(stage);
        
        if(file != null){
            currentSoundData.setNotificationSoundPath(file.getPath());
            currentSoundData.setNotificationSoundName(file.getName());
            notificationSoundFileName.textProperty().set(file.getName());
        }
        
    }

    public void save() {
        NotificationTypeManager typeManager = (NotificationTypeManager) GlobalAccess.getInstance().getNotificationTypeRegister();
        typeManager.saveNotificationConfigs(currentConfigProperty);
        
        currentSoundData.setNotificationSoundVolume(Double.parseDouble(volumeText.getText()));
        typeManager.saveNotificationSound(currentSoundData);
    }

    public void cancel() {
        for(int i = 1;i < previousConfig.size();i++){
            toggleButtonSelectedProperties.get(i).setValue(previousConfig.get(i));
        }
        notificationSoundFileName.textProperty().setValue(previousSoundData.getNotificationSoundName());
        volumeText.textProperty().set(""+previousSoundData.getNotificationSoundVolume());
    }
}
