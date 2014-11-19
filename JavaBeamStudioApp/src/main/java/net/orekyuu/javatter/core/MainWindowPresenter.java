package net.orekyuu.javatter.core;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.core.dialog.ExceptionDialogBuilder;
import net.orekyuu.javatter.core.twitter.LocalClientUser;
import net.orekyuu.javatter.api.EditText;
import net.orekyuu.javatter.api.twitter.ClientUser;
import java.util.List;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainWindowPresenter implements Initializable, EditText, OnDropListener {
	@FXML
	private HBox hbox;
	@FXML
	private CustomTextArea tweetTextArea;
	@FXML
	private Text remaining;
	@FXML
	private ImageView clientUserImage;
	@FXML
	private ImageView tweetImage1;
	@FXML
	private ImageView tweetImage2;
	@FXML
	private ImageView tweetImage3;

	private static int imageCount = 0;
	private static int nowUserIndex = 0;
	private List<Image> myProfileImage = new ArrayList<>();
	private List<ClientUser> users = new ArrayList<>();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (LocalClientUser.loadClientUsers().size() > 0) {
			users.addAll(LocalClientUser.loadClientUsers());
			Platform.runLater(() -> {
				try {
					for (int i = 0; i < users.size(); i++) {
						myProfileImage
								.add(new Image(users.get(i).getTwitter().verifyCredentials().getProfileImageURL()));
					}
					clientUserImage.setImage(myProfileImage.get(nowUser));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		tweetTextArea.addChangeTextListener(this);
		tweetTextArea.addOnDropListener(this);
	}

	public void addColumn(ActionEvent actionEvent) {
		FXMLLoader loader = new FXMLLoader();
		try {
			Node node = loader.load(getClass().getResourceAsStream("column.fxml"));
			HBox.setHgrow(node, Priority.ALWAYS);
			hbox.getChildren().add(node);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionDialogBuilder.create(e);
		}
	}

	public void removeColumn(ActionEvent actionEvent) {
		if (hbox.getChildren().size() != 0)
			hbox.getChildren().remove(hbox.getChildren().size() - 1);
	}

	public void openConfig() {
		FXMLLoader loader = new FXMLLoader();
		try {
			Parent parent = loader.load(getClass().getResourceAsStream("config.fxml"));
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(Main.class.getResource("javabeamstudio.css").toExternalForm());
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("設定");
			stage.centerOnScreen();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionDialogBuilder.create(e);
		}
	}

	// ツイートの実行
	public void tweet() {
		if (getText().length() > 0) {
			new TweetUtil().sendTweet(users.get(nowUserIndex), getText());
		}
		setText("");
	}

	// Javaビームです。
	public void javaBeam() {
		new TweetUtil().sendTweet(users.get(nowUserIndex), "Javaﾋﾞｰﾑｗｗｗｗｗｗｗｗｗｗﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞﾋﾞｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗｗ");
	}

	// ユーザーアイコンのクリックによりツイートを行うユーザーを変更します。
	public void changeUser() {
		if (!(users.size() == 1 && users.size() == 0)) {
			if (users.size() - 1 > nowUserIndex) {
				nowUserIndex++;
			} else {
				nowUserIndex = 0;
			}

			clientUserImage.setImage(myProfileImage.get(nowUserIndex));
		}
	}

	public void getOnDropImage1() {

	}

	public void getOnDropImage2() {

	}

	public void getOnDropImage3() {

	}

	@Override
	public void setText(String s) {
		tweetTextArea.setText(s);
	}

	@Override
	public String getText() {
		return tweetTextArea.getText();
	}

	// tweetTextAreaに変更があると自動で呼ばれます。
	@Override
	public void onChanged(CharSequence cs, int count) {
		remaining.setText("" + (140 - count));
	}

	@Override
	public void onDrop(DragEvent e) {
		Dragboard dragboard = e.getDragboard();
		if (dragboard.hasFiles() && imageCount < 3) {
			e.acceptTransferModes(TransferMode.COPY);
		}
	}

	@Override
	public void onDroped(DragEvent e) {
		Dragboard dragboard = e.getDragboard();
		if (dragboard.hasFiles() && imageCount < 3) {
			switch (imageCount) {
			case 0:
				tweetImage1.setImage(new Image(dragboard.getFiles().get(0).toURI().toString()));
				imageCount = 1;
				imageCount = 1;
				break;
			case 1:
				tweetImage2.setImage(new Image(dragboard.getFiles().get(0).toURI().toString()));
				imageCount = 2;
				break;
			case 2:
				tweetImage1.setImage(new Image(dragboard.getFiles().get(0).toURI().toString()));
				imageCount = 3;
				break;
			}
			e.setDropCompleted(true);

		} else {
			e.setDropCompleted(false);
		}
	}
}
