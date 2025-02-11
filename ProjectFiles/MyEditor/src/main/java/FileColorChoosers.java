import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/** An example class used to demonstrate the
  * use of the color and file choosers
  */
public class FileColorChoosers extends JFrame {
   private JTextArea text = new JTextArea();
   private JFileChooser fileChoose
      = new JFileChooser();
   private JDialog colorDlg;
   private JColorChooser colorChoose
      = new JColorChooser();

// Class constructor
   public FileColorChoosers( String titleText ) {
      super( titleText );
      setJMenuBar( buildMenuBar() );
      text.setEditable( false );
      Container cp = getContentPane();
      cp.add( new JScrollPane( text ),
              BorderLayout.CENTER );
      setBounds(350,350, 500, 400 );
      setVisible( true );
   }

   // Present a dialog box to have the user select
   //the file for browsing
   public void loadFile() {
      int result = fileChoose.showOpenDialog(
         this );
      File file = fileChoose.getSelectedFile();
      if ( file != null
           && result == JFileChooser.APPROVE_OPTION ) try {
         FileReader fr = new FileReader( file );
         text.setText( "" );
         char[] charBuffer = new char[4096];
         int charsRead = fr.read( charBuffer, 0,
                                  charBuffer.length );
         while ( charsRead != -1 ) {
            text.append( new String( charBuffer, 0,
                                     charsRead ) );
            charsRead = fr.read( charBuffer, 0,
                                 charBuffer.length );
         }
      } catch( IOException ioe ) {
         ioe.printStackTrace();
      }
   }

   // Build the menu bar, menus, and menu items for
   // the file browser
   public JMenuBar buildMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      JMenu fileMenu = new JMenu( "File" );
      JMenu colorMenu = new JMenu( "Color" );
      JMenuItem exitItem = new JMenuItem( "Exit" );
      JMenuItem fileOpenItem
         = new JMenuItem( "File Open..." );
      JMenuItem colorsItem
         = new JMenuItem( "Change Color..." );

      fileMenu.setMnemonic( KeyEvent.VK_F );
      colorMenu.setMnemonic( KeyEvent.VK_C );
      fileOpenItem.setMnemonic( KeyEvent.VK_O );
      exitItem.setMnemonic( KeyEvent.VK_X );
      colorsItem.setMnemonic( KeyEvent.VK_C );

      MenuListener menuListener = new MenuListener();
      fileOpenItem.addActionListener(menuListener);
      exitItem.addActionListener(menuListener);
      colorsItem.addActionListener(menuListener);

      menuBar.add( fileMenu );
      menuBar.add( colorMenu );
      fileMenu.add( fileOpenItem );
      fileMenu.add( exitItem );
      colorMenu.add( colorsItem );

      return menuBar;
   }

   class ColorOKListener implements ActionListener {
      public void actionPerformed( ActionEvent e ) {
         Color c = colorChoose.getColor();
         text.setForeground( c );
         text.repaint();
      }
   }
   
   class MenuListener implements ActionListener{
       public void actionPerformed(ActionEvent e){
           JMenuItem source = (JMenuItem)(e.getSource());
           String command = source.getText();
           
           if(command.equals("File Open...")){
               loadFile();
           }else if(command.equals("Change Color...")){
               if ( colorDlg == null ) {
                  colorDlg = JColorChooser.createDialog(
                     FileColorChoosers.this,
                     "Select Text Color",
                     true,
                     colorChoose,
                     new ColorOKListener(),
                     null
                  );
               }
               colorChoose.setColor(
                  text.getForeground() );
               colorDlg.setVisible( true );
           }else if(command.equals("Exit")){
               dispose();
               System.exit( 0 );
           }
       }
   }

   // The main method for the class
   public static void main( String[] args ) {
   	 FileColorChoosers app = new FileColorChoosers( "File and Color Choosers" );
     app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}