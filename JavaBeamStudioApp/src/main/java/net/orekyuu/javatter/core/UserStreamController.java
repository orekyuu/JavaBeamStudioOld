package net.orekyuu.javatter.core;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.Status;

/**
 * userstreamのコントローラです。
 *
 */
public class UserStreamController implements JavatterColumn {
	@FXML
	private ListView<Status> userStreamList;

	@Override
	public void setClientUser(ClientUser clientUser) {
		userStreamList.setCellFactory(cell -> new TweetCell(clientUser));
		clientUser.getStream().addOnStatus(status -> {
			Platform.runLater(() -> {
				userStreamList.getItems().add(0, status);
			});
		});
	}
}
