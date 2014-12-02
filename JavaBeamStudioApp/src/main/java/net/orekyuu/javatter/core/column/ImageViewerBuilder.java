package net.orekyuu.javatter.core.column;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ImageViewerBuilder {
    private String title;
    private Image image;
    private final double WIDTH = 768;
    private final double HEIGHT = 432;
    private ImageView imageView;

    public ImageViewerBuilder setTitle(String text) {
        title = text;
        return this;
    }

    public ImageViewerBuilder setMedia(Image image) {
        this.image = image;
        return this;
    }

    public void show() {
        imageView = new ImageView();
        Stage stage = new Stage();
        StackPane pane = new StackPane();
        imageView.maxWidth(WIDTH);
        imageView.maxHeight(HEIGHT);
        fitImage();
        imageView.setImage(image);
        
        pane.getChildren().add(imageView);
        pane.setMaxHeight(WIDTH);
        pane.setMaxWidth(HEIGHT);
        pane.setMinHeight(WIDTH);
        pane.setMinWidth(HEIGHT);
        
        Scene scene = new Scene(pane, 768, 432);

        if (title == null) {
            stage.setTitle("プレビュー");
        } else {
            stage.setTitle(title);
        }

        stage.setScene(scene);
        stage.show();

        stage.focusedProperty().addListener(e -> stage.close());

    }

    private void fitImage() {
        if (image.getHeight() > HEIGHT) {
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(image.getWidth() * (HEIGHT / image.getHeight()));
        }
        if (image.getWidth() > WIDTH) {
            imageView.setFitWidth(WIDTH);
            imageView.setFitHeight(image.getHeight() * (WIDTH / image.getWidth()));
        }
    }
}
