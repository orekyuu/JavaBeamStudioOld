package net.orekyuu.javatter.core.notification;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
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
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.core.config.ConfigPageBase;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationConfigPresenter extends ConfigPageBase {
    @FXML
    private Text volumeText;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Text notificationSoundFileName;
    @FXML
    private ListView<NotificationTypeManager.NotificationConfig> notificationList;

    private List<NotificationTypeManager.NotificationConfig> previousConfigList = new LinkedList<>();
    private NotificationTypeManager.NotificationSoundData previousSoundData = new NotificationTypeManager.NotificationSoundData();
    private NotificationTypeManager.NotificationSoundData currentSoundData = new NotificationTypeManager.NotificationSoundData();
    private NotificationTypeManager typeManager;

    @Override
    protected void initializeBackground() {
        typeManager = (NotificationTypeManager) API.getInstance().getNotificationTypeRegister();
        previousSoundData = typeManager.loadNotificationSoundData().orElse(new NotificationTypeManager.NotificationSoundData());
    }

    @Override
    protected void initializeUI() {
        Bindings.bindBidirectional(volumeText.textProperty(), volumeSlider.valueProperty(), new NumberStringConverter());
        NotificationTypeManager typeManager = (NotificationTypeManager) API.getInstance().getNotificationTypeRegister();
        previousSoundData = typeManager.loadNotificationSoundData().orElse(previousSoundData);
        if (typeManager.soundDataIsEmpty()) {
            notificationSoundFileName.textProperty().setValue("ファイルを選択してください");
        } else {
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
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Text text = new Text(item.getNotificationType());
                    ToggleButton toggleButton = new ToggleButton(item.isNotice() ? "通知する" : "通知しない");
                    toggleButton.setSelected(item.isNotice());
                    toggleButton.textProperty().bind(Bindings.when(toggleButton.selectedProperty()).then("通知する").otherwise("通知しない"));
                    toggleButton.selectedProperty().addListener(e -> {
                        item.setNotice(toggleButton.isSelected());
                    });
                    Pane grow = new Pane();
                    HBox.setHgrow(grow, Priority.ALWAYS);
                    setGraphic(new HBox(text, grow, toggleButton));
                }
            }
        });
        typeManager.getConfigs().forEach(notificationList.getItems()::add);
        previousConfigList = createCopyList(notificationList.getItems());
    }

    @FXML
    private void selectNotificationSound() {
        FileChooser chooser = new FileChooser();

        Stage stage = API.getInstance().getApplication().getPrimaryStage();

        chooser.setTitle("ファイル選択");

        List<String> mp3Extensions = new ArrayList<>();
        mp3Extensions.add("*.mp3");
        mp3Extensions.add("*.MP3");
        List<String> wavExtensions = new ArrayList<>();
        wavExtensions.add("*.wav");
        wavExtensions.add("*.WAV");

        FileChooser.ExtensionFilter mp3Filter = new FileChooser.ExtensionFilter("MP3 形式サウンド", mp3Extensions);
        FileChooser.ExtensionFilter wavFilter = new FileChooser.ExtensionFilter("WAV 形式サウンド", wavExtensions);
        chooser.getExtensionFilters().add(mp3Filter);
        chooser.getExtensionFilters().add(wavFilter);
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            currentSoundData.setNotificationSoundPath(file.getPath());
            currentSoundData.setNotificationSoundName(file.getName());
            notificationSoundFileName.textProperty().set(file.getName());
        }

    }

    @FXML
    private void save() {
        NotificationTypeManager typeManager = (NotificationTypeManager) API.getInstance().getNotificationTypeRegister();
        typeManager.saveNotificationConfigs(notificationList.getItems());

        currentSoundData.setNotificationSoundVolume(Double.parseDouble(volumeText.getText()));
        typeManager.saveNotificationSound(currentSoundData);
        previousSoundData = currentSoundData;
        previousConfigList = createCopyList(notificationList.getItems());
    }

    private List<NotificationTypeManager.NotificationConfig> createCopyList(List<NotificationTypeManager.NotificationConfig> list) {
        return list.stream().map(conf -> {
            NotificationTypeManager.NotificationConfig copy = new NotificationTypeManager.NotificationConfig();
            copy.setNotice(conf.isNotice());
            copy.setNotificationType(conf.getNotificationType());
            return copy;
        }).collect(Collectors.toList());
    }

    @FXML
    private void cancel() {
        notificationList.getItems().setAll(createCopyList(previousConfigList));
        notificationSoundFileName.textProperty().setValue(previousSoundData.getNotificationSoundName());
        volumeText.textProperty().set(String.valueOf(previousSoundData.getNotificationSoundVolume()));
    }
}
