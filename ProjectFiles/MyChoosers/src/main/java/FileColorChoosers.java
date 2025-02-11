import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.print.*;

/** An example class used to demonstrate the
  * use of the color and file choosers
  */
public class FileColorChoosers extends JFrame {
   private JTextArea text = new JTextArea();
   
   private JFileChooser fileChoose = new JFileChooser();
   private JDialog colorDlg;
   private JColorChooser colorChoose = new JColorChooser();
   private File currentFile = null;

// Class constructor
   public FileColorChoosers( String titleText ) {
      super( titleText );
      setJMenuBar( buildMenuBar() );
      text.setEditable( true );
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
      currentFile = file;
      setTitle(file.getName() + " - My Text Editor");
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
   
   public void newFile(){
      text.setText("");
      currentFile = null;
      setTitle("Untitled - My Text Editor");
   }
   
   public void saveFile(){
       if(currentFile == null){
           int result = fileChoose.showSaveDialog(this);
           if(result == JFileChooser.APPROVE_OPTION){
               currentFile = fileChoose.getSelectedFile();
               setTitle(currentFile.getName() + " - My Text Editor");
               
           }else{
               return; //cancelled
           }
       }
       try(BufferedWriter writer = new BufferedWriter(new FileWriter(currentFile))){
           writer.write(text.getText());
       }catch(IOException e){
           e.printStackTrace();
           JOptionPane.showMessageDialog(this, "Error saving file.", "Error", JOptionPane.ERROR_MESSAGE);
           
       }
   }
   
   public void printFile(){
       PrinterJob printer = PrinterJob.getPrinterJob();
       printer.setJobName("Print Text");
       
       printer.setPrintable(new Printable(){
          public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException{
              if(pageIndex > 0){
                  return NO_SUCH_PAGE;
              }
              g.translate((int) pf.getImageableX(), (int)pf.getImageableY());
              text.paint(g);
              return PAGE_EXISTS;
          } 
       });
       
       if(printer.printDialog()){
           try{
               printer.print();
           }catch(PrinterException e){
               e.printStackTrace();
           }
       }
   }
   
  

   // Build the menu bar, menus, and menu items for
   // the file browser
   public JMenuBar buildMenuBar() {
      JMenuBar menuBar = new JMenuBar();
      JMenu fileMenu = new JMenu( "File" );
      JMenu colorMenu = new JMenu( "Color" );
      JMenu editMenu = new JMenu("Edit");
      
      JMenuItem exitItem 
         = new JMenuItem( "Exit" );
      JMenuItem fileOpenItem
         = new JMenuItem( "File Open..." );
      JMenuItem newFileItem 
         = new JMenuItem("File New");
      JMenuItem saveFileItem 
         = new JMenuItem("File Save");
      JMenuItem printFileItem 
         = new JMenuItem("Print");
      
      JMenuItem colorsItem
         = new JMenuItem( "Change Color..." );
      
      JMenuItem selectAllItem
         = new JMenuItem("Select All");
      JMenuItem deleteItem
         = new JMenuItem("Delete");
      JMenuItem copyItem
         = new JMenuItem("Copy");
      JMenuItem cutItem
         = new JMenuItem("Cut");
      JMenuItem pasteItem
         = new JMenuItem("Paste");
      
      

      fileMenu.setMnemonic( KeyEvent.VK_F );
      colorMenu.setMnemonic( KeyEvent.VK_C );
      fileOpenItem.setMnemonic( KeyEvent.VK_O );
      exitItem.setMnemonic( KeyEvent.VK_X );
      colorsItem.setMnemonic( KeyEvent.VK_C );

      MenuListener menuListener = new MenuListener();
      fileOpenItem.addActionListener(menuListener);
      exitItem.addActionListener(menuListener);
      saveFileItem.addActionListener(menuListener);
      newFileItem.addActionListener(menuListener);
      printFileItem.addActionListener(menuListener);
      
      colorsItem.addActionListener(menuListener);
      
      selectAllItem.addActionListener(menuListener);
      deleteItem.addActionListener(menuListener);
      copyItem.addActionListener(menuListener);
      cutItem.addActionListener(menuListener);
      pasteItem.addActionListener(menuListener);

      
      fileMenu.add( fileOpenItem );
      fileMenu.add(newFileItem);
      fileMenu.add(saveFileItem);
      fileMenu.add(printFileItem);
      fileMenu.add( exitItem );
      
      colorMenu.add( colorsItem );
      
      editMenu.add(selectAllItem);
      editMenu.add(deleteItem);
      editMenu.add(copyItem);
      editMenu.add(cutItem);
      editMenu.add(pasteItem);
      
      menuBar.add( fileMenu );
      menuBar.add( colorMenu );
      menuBar.add(editMenu);

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
               handleColor();
               
           }else if(command.equals("Exit")){
               dispose();
               System.exit( 0 );
               
           }else if(command.equals("File New")){
               newFile();
           }else if(command.equals("File Save")){
               saveFile();
           }else if(command.equals("Print")){
               printFile();
           }else if(command.equals("Select All")){
               text.selectAll();
           }else if(command.equals("Delete")){
               text.replaceSelection("");
           }else if(command.equals("Copy")){
               text.copy();
           }else if(command.equals("Cut")){
               text.cut();
           }else if(command.equals("Paste")){
               text.paste();
           }
       }
   }
   
   public void handleColor(){
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
   }

   // The main method for the class
   public static void main( String[] args ) {
   	 FileColorChoosers app = new FileColorChoosers( "File and Color Choosers" );
     app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
}