import org.jspecify.annotations.NullMarked;

@NullMarked
module guesscharts.fx {
    requires javafx.controls;
    requires javafx.media;
    requires guesscharts.domain;
    requires org.jspecify;
    opens guesscharts.java_fx to javafx.graphics;
}