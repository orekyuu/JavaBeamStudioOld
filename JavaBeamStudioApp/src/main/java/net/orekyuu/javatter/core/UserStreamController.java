package net.orekyuu.javatter.core;

import java.net.URL;
import java.util.ResourceBundle;



import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import twitter4j.Status;

/**
 * userstreamのコントローラです。
 *
 */
public class UserStreamController implements JavatterColumn,Initializable {
	@FXML
	private ListView<Status> userStreamList;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userStreamList.setCellFactory(cell->new TweetCell());
	}
	@Override
	public void setClientUser(ClientUser clientUser) {
			
		clientUser.getStream().addOnStatus(status ->{
            Platform.runLater(() -> {
				userStreamList.getItems().add(status);
            });
        });
	}
	

}
