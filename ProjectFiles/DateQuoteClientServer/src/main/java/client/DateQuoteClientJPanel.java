package client;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class DateQuoteClientJPanel extends JPanel{

 	private JTextField textField = new JTextField("localhost",20);
	private	JRadioButton dateRadioButton = new JRadioButton("Date", true);
	private	JRadioButton quoteRadioButton = new JRadioButton("Quote", false);
	private	JTextArea textDisplay = new JTextArea(10,25);
	private JLabel ip = new JLabel("Hostname:");
        private JLabel choice = new JLabel("Choice:");
	private	JButton send = new JButton("Send");
        private JButton clear = new JButton("Clear");
	private	SendButtonListener sendButtonHandler = new SendButtonListener ();
        private ClearButtonListener clearButtonHandler = new ClearButtonListener();
	private RadioItemListener itemHandler = new RadioItemListener();
        
	private String data = "date";

	public DateQuoteClientJPanel() {

		setLayout(new FlowLayout());

		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(dateRadioButton);
		radioGroup.add(quoteRadioButton);

		add(ip);
		add(textField);
                add(choice);
		add(dateRadioButton);
		add(quoteRadioButton);
		add(new JScrollPane(textDisplay));
		add(send);
                add(clear);


		send.addActionListener(sendButtonHandler);
                clear.addActionListener(clearButtonHandler);
		dateRadioButton.addItemListener(itemHandler);
		quoteRadioButton.addItemListener(itemHandler);


	}

	private class RadioItemListener implements ItemListener {
	  public void itemStateChanged (ItemEvent e) {
	  	    //clear textDisplay
           	//textDisplay.setText("");

	  	if (e.getItem() == dateRadioButton)
	  	    data = "date";
	  	 else data = "quote";
	 }
   }



	private class SendButtonListener implements ActionListener {
	  public void actionPerformed (ActionEvent e) {

           BufferedReader fromServer = null;
           PrintWriter toServer = null;
           DatagramSocket sock = null;
	   try {
                String hostname = textField.getText();
                System.out.println("hostname is " + hostname);
                int port = 4445;

                sock = new DatagramSocket();
               
                InetAddress address = InetAddress.getByName(hostname);
                
//                fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//                toServer = new PrintWriter(sock.getOutputStream(), true);
                
                
                byte[] sendBuf = data.getBytes();
                DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length, address, port);
                sock.send(packet);
                
                
                byte[] receiveBuf = new byte[256];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuf, receiveBuf.length);
                sock.receive(receivePacket);
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                
                textDisplay.append(response + "\n");
                
                sock.close();
    
                    
          } catch (SocketException se) {
          	textDisplay.append("Socket Exception!");
          } catch (UnknownHostException uhe) {
          	textDisplay.append("Unknown Host Exception!");
          } catch (IOException ioe) {
          	textDisplay.append("IO Exception!");
          }finally{
//               if(fromServer != null){
//                   fromServer.close();
//               }
//               if(toServer != null) toServer.close();
//               if(sock != null) sock.close();
           }
	  }
	}
        
        private class ClearButtonListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                textDisplay.setText("");
            }
        }


}