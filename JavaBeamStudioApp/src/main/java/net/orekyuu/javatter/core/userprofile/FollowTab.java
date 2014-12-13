package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;

public class FollowTab extends UserProfileTabBase {
    @FXML
    private ListView<StatusModel> listView;

    @Override
    protected void initializeBackground(UserModel user) {
    }

    @Override
    protected void initializeUI(UserModel user) {
    }

    @Override
    public String getTabName() {
        return "フォロー";
    }
}
