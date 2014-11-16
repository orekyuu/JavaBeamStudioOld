package net.orekyuu.javatter.core.column;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.column.TweetCell;
import net.orekyuu.javatter.core.models.StatusModel;
import twitter4j.Status;

/**
 * userstreamのコントローラです。
 */
public class UserStreamController implements JavatterColumn {
    @FXML
    private ListView<StatusModel> userStreamList;

    @Override
    public void setClientUser(ClientUser clientUser) {
        userStreamList.setCellFactory(cell -> new TweetCell(clientUser));
        clientUser.getStream().addOnStatus(status -> {
            Platform.runLater(() -> {
                userStreamList.getItems().add(0, StatusModel.Builder.build(status));
                if(userStreamList.getItems().size() > 10)
                    userStreamList.getItems().remove(10);
            });
        });
    }
}
