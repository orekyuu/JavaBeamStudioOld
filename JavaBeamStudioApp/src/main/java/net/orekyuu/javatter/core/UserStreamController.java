package net.orekyuu.javatter.core;

import net.orekyuu.javatter.api.JavatterColumn;
import net.orekyuu.javatter.api.twitter.ClientUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * userstreamのコントローラです。
 * 
 */
public class UserStreamController implements JavatterColumn {
	// セルがまだわからないので仮組みです。
	@FXML
	private ListView<String> userStreamList;
	private ObservableList<String> observableList = 
			FXCollections.observableArrayList();
	private ClientUser clientUser;
	
	 void setStreamList(){
		 clientUser.getStream().addOnStatus(t->{
			observableList.add(null);
			userStreamList.setItems(observableList);
			userStreamList.setCellFactory(null);
		 });
	 }
	
	
	@Override
	public void setClientUser(ClientUser clientUser) {
		this.clientUser=clientUser;
	}

}
