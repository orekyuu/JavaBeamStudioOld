package net.orekyuu.javatter.core;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
	// セルがまだわからないので仮組みです。
	@FXML
	private ListView<Status> userStreamList;
	/*
	private ObservableList<String> observableList =
			FXCollections.observableArrayList();
	*/
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO 自動生成されたメソッド・スタブ
		userStreamList.setCellFactory(cell->new TweetCell());
	}
	@Override
	public void setClientUser(ClientUser clientUser) {
		// UI側で非同期処理
		Platform.runLater(() -> {
			// Streamを取得し、ステータスを追加する
			clientUser.getStream().addOnStatus(userStreamList.getItems()::add);
		});
	}


}
