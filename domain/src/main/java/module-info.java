module guesscharts.domain {
   requires jdk.crypto.ec; // Needed for jlinked build
   requires org.json;
   requires org.jsoup;

   exports guesscharts.parser;
   exports guesscharts.parser.german;
   exports guesscharts.parser.swiss;
   exports guesscharts.parser.uk;
}