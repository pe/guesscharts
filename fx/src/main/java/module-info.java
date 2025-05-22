module guesscharts.fx {
    requires javafx.controls;
    requires javafx.media;
    requires guesscharts.domain;
    opens guesscharts.java_fx to javafx.graphics;
}