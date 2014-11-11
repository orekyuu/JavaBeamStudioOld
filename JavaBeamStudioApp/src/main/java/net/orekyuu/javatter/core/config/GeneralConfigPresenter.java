package net.orekyuu.javatter.core.config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GeneralConfigPresenter implements Initializable {

    @FXML
    private CheckBox checkTweet;
    @FXML
    private CheckBox checkReply;
    @FXML
    private CheckBox checkRT;
    @FXML
    private CheckBox checkFav;
    @FXML
    private ChoiceBox<String> nameDisplayType;
    @FXML
    private CheckBox isExpandURL;
    private GeneralConfigModel lasted;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameDisplayType.getItems().addAll(NameDisplayType.ID.configName(), NameDisplayType.NAME.configName(), NameDisplayType.ID_NAME.configName(), NameDisplayType.NAME_ID.configName());

        lasted = GeneralConfigHelper.loadConfigFromDB();
        if (lasted == null) lasted = new GeneralConfigModel();
        setSelect(lasted);

    }

    public void save() {
        GeneralConfigModel config = new GeneralConfigModel();
        config.setCheckFav(checkFav.isSelected());
        config.setCheckReply(checkReply.isSelected());
        config.setCheckTweet(checkTweet.isSelected());
        config.setCheckRT(checkRT.isSelected());
        config.setExpandURL(isExpandURL.isSelected());
        NameDisplayType name = Arrays.stream(NameDisplayType.values())
                .filter(type -> type.configName().equals(nameDisplayType.getValue()))
                .findFirst()
                .get();
        config.setNameDisplayType(name.name());
        GeneralConfigHelper.saveToDB(config);
        lasted = config;
    }

    public void cancel() {
        setSelect(lasted);
    }

    private void setSelect(GeneralConfigModel model) {
        checkTweet.setSelected(model.isCheckTweet());
        checkReply.setSelected(model.isCheckReply());
        checkRT.setSelected(model.isCheckRT());
        checkFav.setSelected(model.isCheckFav());
        nameDisplayType.getSelectionModel().select(NameDisplayType.valueOf(model.getNameDisplayType()).configName());
        isExpandURL.setSelected(model.isExpandURL());
    }
}
