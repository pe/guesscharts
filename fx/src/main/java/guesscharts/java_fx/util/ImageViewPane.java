package guesscharts.java_fx.util;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

/**
 * https://bugs.openjdk.java.net/browse/JDK-8091216
 */
public class ImageViewPane extends Region {
    private final ObjectProperty<ImageView> imageViewProperty = new SimpleObjectProperty<>();

    public ImageViewPane(ImageView imageView) {
        imageViewProperty.addListener((observable, oldIV, newIV) -> {
            if (oldIV != null) {
                getChildren().remove(oldIV);
            }
            if (newIV != null) {
                getChildren().add(newIV);
            }
        });
        this.imageViewProperty.set(imageView);
    }

    @Override
    protected void layoutChildren() {
        ImageView imageView = imageViewProperty.get();
        if (imageView != null) {
            imageView.setFitWidth(getWidth());
            imageView.setFitHeight(getHeight());
            layoutInArea(imageView, 0, 0, getWidth(), getHeight(), 0, HPos.CENTER, VPos.CENTER);
        }
        super.layoutChildren();
    }
}