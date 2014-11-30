package net.orekyuu.javatter.core.column;

import twitter4j.TwitterException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.models.StatusModel;

public class MentionsController implements JavatterColumn {
    @FXML
    ListView<StatusModel> mentionsList;

    @Override
    public void setClientUser(ClientUser clientUser) {

        try {
            long userId = clientUser.getTwitter().getId();

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
        } catch (IllegalStateException | TwitterException e) {
            e.printStackTrace();
        }
    }
}
