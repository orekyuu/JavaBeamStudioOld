package net.orekyuu.javatter.core.column;

import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.models.StatusModel;
import twitter4j.Status;
import twitter4j.TwitterException;

public class MentionsController implements JavatterColumn {
    @FXML
    ListView<StatusModel> mentionsList;
    private final int LIMIT_VALUE = 30;

    @Override
    public void setClientUser(ClientUser clientUser) {
        long userId = getUserId(clientUser);
        mentionsList.setCellFactory(cell -> new TweetCell(clientUser));
        Platform.runLater(() -> {
            List<Status> homeTimeline = null;
            try {
                homeTimeline = clientUser.getTwitter().getHomeTimeline();
            } catch (Exception e) {
                e.printStackTrace();
            }
            homeTimeline.stream().limit(LIMIT_VALUE).forEachOrdered(status -> {
                mentionsList.getItems().add(StatusModel.Builder.build(status));
            });
        });
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
