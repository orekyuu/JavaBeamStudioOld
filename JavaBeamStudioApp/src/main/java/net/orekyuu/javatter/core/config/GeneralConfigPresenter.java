package net.orekyuu.javatter.core.config;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;

import java.util.Arrays;

public class GeneralConfigPresenter extends ConfigPageBase {

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
    protected void initializeBackground() {
        nameDisplayType.getItems().addAll(NameDisplayType.ID.configName(), NameDisplayType.NAME.configName(), NameDisplayType.ID_NAME.configName(), NameDisplayType.NAME_ID.configName());
        lasted = GeneralConfigHelper.loadConfigFromDB();
        if (lasted == null) lasted = new GeneralConfigModel();
    }

    @Override
    protected void initializeUI() {
        setSelect(lasted);
    }
    @FXML
    private void save() {
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
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
                return null;
            }
        };
        bindTask(task);
        TaskUtil.startTask(task);
    }
    @FXML
    private void cancel() {
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
