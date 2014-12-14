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

public class FollowTab extends UserProfileTabBase {
    @FXML
    private ListView<UserModel> listView;
    private ClientUser clientUser;
    private static final int FRIENDS_LIMIT = 30;
    private ResponseList<User> users;

    @Override
    protected void initializeBackground(UserModel user) {
        this.clientUser = GlobalAccess.getInstance().getApplication().getCurrentWindow().getCurrentUserProperty().getValue();
        try {
            users = clientUser.getTwitter().getFriendsList(user.getId(),-1L,FRIENDS_LIMIT);
        } catch (TwitterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initializeUI(UserModel user) {
        listView.setCellFactory(e -> new UserCell(clientUser));
        users.stream().map(UserModel.Builder::build).forEach(listView.getItems()::add);
        StackPane stack = (StackPane) listView.getParent();
        listView.prefWidthProperty().bind(stack.widthProperty());
        listView.prefHeightProperty().bind(stack.heightProperty());
    }

    @Override
    public String getTabName() {
        return "フォロー";
    }
}
