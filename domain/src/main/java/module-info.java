import org.jspecify.annotations.NullMarked;

@NullMarked
module guesscharts.domain {
    requires org.json;
    requires org.jsoup;
    requires org.jspecify;

    exports guesscharts.parser;
    exports guesscharts.parser.german;
    exports guesscharts.parser.swiss;
    exports guesscharts.parser.uk;
}