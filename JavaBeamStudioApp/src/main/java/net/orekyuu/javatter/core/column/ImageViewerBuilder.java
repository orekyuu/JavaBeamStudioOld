package net.orekyuu.javatter.core.column;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ImageViewerBuilder {
    private String title;
    private Image image;
    private final static double WIDTH = 921.6;
    private final static double HEIGHT = 518.4;
    private ImageView imageView;

    public ImageViewerBuilder setTitle(String text) {
        title = text;
        return this;
    }

    public ImageViewerBuilder setMedia(Image image) {
        if (image == null)
            throw new NullPointerException("ImageViewerBuilder:image == null");
        this.image = image;
        return this;
    }

    public void show() {
        imageView = new ImageView();
        if (image == null)
            throw new NullPointerException("ImageViewerBuilder:image == null");
        Stage stage = new Stage();
        StackPane pane = new StackPane();

        fitImage();

        imageView.setImage(image);

        pane.getChildren().add(imageView);
        pane.setMaxHeight(WIDTH);
        pane.setMaxWidth(HEIGHT);
        pane.setMinHeight(0);
        pane.setMaxWidth(0);
        Scene scene = new Scene(pane);

        String title = this.title != null ? this.title : "プレビュー";
        stage.setTitle(title);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();

        stage.focusedProperty().addListener(e -> stage.close());

    }

    private void fitImage() {
        if (image.getHeight() > HEIGHT) {
            imageView.setFitHeight(HEIGHT);
            imageView.setFitWidth(image.getWidth()
                    * (HEIGHT / image.getHeight()));
        }
        if (image.getWidth() > WIDTH) {
            imageView.setFitWidth(WIDTH);
            imageView.setFitHeight(image.getHeight()
                    * (WIDTH / image.getWidth()));
        }
    }
}
