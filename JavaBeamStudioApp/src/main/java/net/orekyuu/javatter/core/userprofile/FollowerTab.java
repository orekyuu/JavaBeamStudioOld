package net.orekyuu.javatter.core.userprofile;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;
import net.orekyuu.javatter.api.GlobalAccess;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.core.column.UserCell;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

public class FollowerTab extends UserProfileTabBase {
    @FXML
    private ListView<UserModel> listView;
    private ClientUser clientUser;

    @Override
    protected void initializeBackground(UserModel user) {
        this.clientUser = GlobalAccess.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            ResponseList<User> users = clientUser.getTwitter().getFollowersList(user.getId(),-1L,30);
            users.stream().map(UserModel.Builder::build).forEach(listView.getItems()::add);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeUI(UserModel user) {
        listView.setCellFactory(e -> new UserCell(clientUser));
        StackPane stack = (StackPane) listView.getParent();
        listView.prefWidthProperty().bind(stack.widthProperty());
        listView.prefHeightProperty().bind(stack.heightProperty());
    }

    @Override
    public String getTabName() {
        return "フォロワー";
    }
}

