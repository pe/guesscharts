module guesscharts.fx {
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	requires guesscharts.domain;
	opens guesscharts.java_fx to javafx.graphics;
}