//QuoteServer.java
import java.io.*;

public class DateQuoteServer {
    public static void main(String[] args) throws IOException {
        new DateQuoteServerThread().start();
    }
}
