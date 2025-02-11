package client;
/**
 * Implement graphics on JPanel to provide smoothier and better graphics
 *
 **/

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class DateQuoteClient {
   // private static Image logo = new Image("okc.png");

    public static void main(String[] args) throws IOException {
                
 		JFrame app = new JFrame("DateQuoteClient");
 		Image icon = Toolkit.getDefaultToolkit().getImage("okc.png");
 		app.setIconImage(icon);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		app.getContentPane().add(new DateQuoteClientJPanel());
		app.pack();
		app.setBounds(450,400, 300, 300);
		app.setVisible(true);
    }
}
