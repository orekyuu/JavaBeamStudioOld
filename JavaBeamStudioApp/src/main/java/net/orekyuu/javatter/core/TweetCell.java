package net.orekyuu.javatter.core;
import javafx.scene.control.ListCell;
import twitter4j.Status;

public class TweetCell extends ListCell<Status>{
	@Override
	protected void updateItem(Status status,boolean empty){
		super.updateItem(status, empty);
	}
}
