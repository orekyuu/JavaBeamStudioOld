package net.orekyuu.javatter.core.column;

import twitter4j.TwitterException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.models.StatusModel;

public class MentionsController implements JavatterColumn {
    @FXML
    ListView<StatusModel> mentionsList;

    @Override
    public void setClientUser(ClientUser clientUser) {
        long userId = getUserId(clientUser);
        mentionsList.setCellFactory(cell -> new TweetCell(clientUser));
        clientUser.getStream().addOnStatus(
                status -> {
                    if (status.getInReplyToUserId() == userId) {
                        Platform.runLater(() -> {
                            mentionsList.getItems().add(0,
                                    StatusModel.Builder.build(status));
                            if (mentionsList.getItems().size() > 100)
                                mentionsList.getItems().remove(100);
                        });
                    }
                });

    }

    private long getUserId(ClientUser clientUser) {
        long userId = 0;
        try {
            userId = clientUser.getTwitter().getId();
            return userId;
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
