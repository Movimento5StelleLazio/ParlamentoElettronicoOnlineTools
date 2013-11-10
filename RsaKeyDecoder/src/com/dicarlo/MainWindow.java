package com.dicarlo;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.LinkedBlockingQueue;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;




/*
 AUTHOR
 MARCO DI CARLO
 
 Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
*/

//VS4E -- DO NOT REMOVE THIS LINE!
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	private ResourceBundle rb = ResourceBundle.getBundle("MyResources",	Locale.getDefault());
	String iconApplication = "/keyspair.png";
	String iconApplicationsmall = "/keyspairsmall.png";
	String iconLoading = "/loading.gif";
	
	String iconFlagItaly="/flag_italy.gif";
	String iconFlagUk="/flag_uk.gif";
	//String iconFlagChina="/flag_china.gif";
	
	String iconOk="/ok.png";
	String iconKo="/ko.png";
	
    private byte[] privateKeyBytes = null;
    private byte[] halfkey1=null;
    private byte[] halfkey2=null;


    private PrivateKey privateKey = null;
    
	private JPanel panelTitle;
	private JLabel labelTitle;
	private JPanel panelAction;
	private JLabel labelIcon;
	private JButton buttonClose;
	private JPanel panelButtons;
	private JFrame parent=null;
	private JMenu menuAbout;
	private JMenu menuLanguage;	
	private JMenuBar menuBar;
	
	javax.swing.JMenuItem menuVersion=null;
	
	javax.swing.JMenuItem menuItalian=null;
	javax.swing.JMenuItem menuEnglish=null;
	
	
	private boolean statusPrivateKey1=false;
	private boolean statusPrivateKey2=false;
	
	// START COMPONENT FOR DECRYPTION
	private JPanel panelDecrypt;
	private JTextField textFileInD;
	private JTextField textFileOutD;
	private JTextField textPrivateKey1;
	private JLabel labelFileInD;
	private JLabel labelFileOutD;
	private JLabel labelPrivateKey1;
	private JButton buttonPrivateKey1;
	private JLabel labelStatusPrivateKey1;
	// END COMPONENT FOR DECRYPTION
	
	private JButton buttonExecute;
	private JLabel labelPrivateKey2;
	private JTextField textPrivateKey2;
	private JButton buttonPrivateKey2;
	private JLabel labelStatusPrivateKey2;

	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";

	public MainWindow() {
		initComponents();
	}

	private void initComponents() {
		setResizable(false);
		setLayout(new GroupLayout());
		add(getPanelTitle(), new Constraints(new Bilateral(12, 12, 0), new Leading(7, 57, 10, 10)));
		add(getPanelButtons(), new Constraints(new Leading(12, 584, 12, 12), new Leading(230, 65, 10, 10)));
		add(getPanelAction(), new Constraints(new Bilateral(12, 12, 0), new Leading(70, 140, 10, 10)));
		setJMenuBar(getJMenuBar());
		setSize(608, 324);
	}

	// START COMPONENT FOR DECRYPTION
	private JLabel getLabelPrivateKey2() {
		if (labelPrivateKey2 == null) {
			labelPrivateKey2 = new JLabel();
			labelPrivateKey2.setText(rb.getString("title.privatekey2"));
		}
		return labelPrivateKey2;
	}
	private JTextField getTextPrivateKey2() {
		if (textPrivateKey2 == null) {
			textPrivateKey2 = new JTextField();
			textPrivateKey2.setEditable(false);
			textPrivateKey2.setText("");
		}
		return textPrivateKey2;
	}
	private JButton getButtonPrivateKey2() {
		if (buttonPrivateKey2 == null) {
			buttonPrivateKey2 = new JButton();
			buttonPrivateKey2.setText("....");
			
			buttonPrivateKey2.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileFilter() {						
						@Override
						public String getDescription() {							
							return "key file";
						}
						
						@Override
						public boolean accept(File file) {
							if (file.isDirectory()) {
							      return true;
							    } else {
							      String path = file.getAbsolutePath().toLowerCase();
							      return path.toLowerCase().endsWith(".key");
							    }
						}
					});
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(file.exists()&&file.getAbsolutePath().toLowerCase().endsWith(".key")&&file.isFile()){
							  textPrivateKey2.setText(file.getAbsolutePath());
							  String filePK1=textPrivateKey1.getText();
							  String filePK2=textPrivateKey2.getText();
							  if(!"".equals(filePK1)&&!"".equals(filePK2)){
								  FileInputStream fis1=null;
								  FileInputStream fis2=null;
								  try{
									fis1 = new FileInputStream(new File(filePK1));
									fis2 = new FileInputStream(new File(filePK2));
									halfkey1 = new byte[(int)new File(filePK1).length()];
									halfkey2 = new byte[(int)new File(filePK2).length()];
									fis1.read(halfkey1);
									fis2.read(halfkey2);
							        privateKeyBytes = new byte[halfkey1.length+halfkey2.length];
								    for(int i=0;i<halfkey1.length;i++){
								    	privateKeyBytes[i]=halfkey1[i];
								    }
								    for(int i=0;i<halfkey2.length;i++){
								    	privateKeyBytes[i+halfkey1.length]=halfkey2[i];
								    }
								    privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
								    fis1.close();
								    fis2.close();
								    fis1=null;
								    fis2=null;
								    labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
								    labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
								  }
								  catch(Exception e){
									e.printStackTrace();
									privateKey=null;
									privateKeyBytes=null;
									textPrivateKey1.setText("");
									textPrivateKey2.setText("");
									labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
									labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
									JOptionPane.showMessageDialog(parent,
											rb.getString("msg.invalidprivatekey"), rb.getString("title.error") ,
											JOptionPane.ERROR_MESSAGE);
								  }
								  finally{
									try{
									  if(fis1!=null)
										  fis1.close();
									}
									catch(Exception e){
									  e.printStackTrace();
									}
									try{
									  if(fis2!=null)
										 fis2.close();
									}
									catch(Exception e){
										e.printStackTrace();
									}
								  }
								  
								  statusPrivateKey1=(privateKey!=null);
								  statusPrivateKey2=statusPrivateKey1;
								  if(!textFileInD.getText().equals("")&&statusPrivateKey1&&statusPrivateKey2){
									  buttonExecute.setEnabled(true);  
								  }
								  else{
									  buttonExecute.setEnabled(false);  
								  }

								  
							  }
							  else{
								  statusPrivateKey1=false;
								  statusPrivateKey2=false;
								  labelStatusPrivateKey1.setIcon(null);
								  labelStatusPrivateKey2.setIcon(null);
							  }
							  
							  
							  
							  if(!textFileInD.getText().equals("")&&statusPrivateKey1&&statusPrivateKey2){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textPrivateKey1.setText("");
							  statusPrivateKey1=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
				}
			}));
		}
		return buttonPrivateKey2;
	}
	private JLabel getLabelStatusPrivateKey2() {
		if (labelStatusPrivateKey2 == null) {
			labelStatusPrivateKey2 = new JLabel();
			labelStatusPrivateKey2.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
		}
		return labelStatusPrivateKey2;
	}
	
	
	private JLabel getLabelPrivateKey1() {
		if (labelPrivateKey1 == null) {
			labelPrivateKey1 = new JLabel();
			labelPrivateKey1.setText(rb.getString("title.privatekey1"));
		}
		return labelPrivateKey1;
	}
	private JTextField getTextPrivateKey1() {
		if (textPrivateKey1 == null) {
			textPrivateKey1 = new JTextField();
			textPrivateKey1.setEditable(false);
		}
		return textPrivateKey1;
	}
	private JButton getButtonPrivateKey1() {
		if (buttonPrivateKey1== null) {
			buttonPrivateKey1 = new JButton();
			buttonPrivateKey1.setText("....");
			buttonPrivateKey1.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileFilter() {						
						@Override
						public String getDescription() {							
							return "key file";
						}
						
						@Override
						public boolean accept(File file) {
							if (file.isDirectory()) {
							      return true;
							    } else {
							      String path = file.getAbsolutePath().toLowerCase();
							      return path.toLowerCase().endsWith(".key");
							    }
						}
					});
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(file.exists()&&file.getAbsolutePath().toLowerCase().endsWith(".key")&&file.isFile()){
							  textPrivateKey1.setText(file.getAbsolutePath());
							  String filePK1=textPrivateKey1.getText();
							  String filePK2=textPrivateKey2.getText();
							  if(!"".equals(filePK1)&&!"".equals(filePK2)){
								  FileInputStream fis1=null;
								  FileInputStream fis2=null;
								  try{
									fis1 = new FileInputStream(new File(filePK1));
									fis2 = new FileInputStream(new File(filePK2));
									halfkey1 = new byte[(int)new File(filePK1).length()];
									halfkey2 = new byte[(int)new File(filePK2).length()];
									fis1.read(halfkey1);
									fis2.read(halfkey2);
							        privateKeyBytes = new byte[halfkey1.length+halfkey2.length];
								    for(int i=0;i<halfkey1.length;i++){
								    	privateKeyBytes[i]=halfkey1[i];
								    }
								    for(int i=0;i<halfkey2.length;i++){
								    	privateKeyBytes[i+halfkey1.length]=halfkey2[i];
								    }
								    privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
								    fis1.close();
								    fis2.close();
								    fis1=null;
								    fis2=null;
								    labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
								    labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
								  }
								  catch(Exception e){
									e.printStackTrace();
									privateKey=null;
									privateKeyBytes=null;
									textPrivateKey1.setText("");
									textPrivateKey2.setText("");
									labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
									labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
									JOptionPane.showMessageDialog(parent,
											rb.getString("msg.invalidprivatekey"), rb.getString("title.error") ,
											JOptionPane.ERROR_MESSAGE);
								  }
								  finally{
									try{
									  if(fis1!=null)
										  fis1.close();
									}
									catch(Exception e){
									  e.printStackTrace();
									}
									try{
									  if(fis2!=null)
										 fis2.close();
									}
									catch(Exception e){
										e.printStackTrace();
									}
								  }
								  
								  statusPrivateKey1=(privateKey!=null);
								  statusPrivateKey2=statusPrivateKey1;
								  if(!textFileInD.getText().equals("")&&statusPrivateKey1&&statusPrivateKey2){
									  buttonExecute.setEnabled(true);  
								  }
								  else{
									  buttonExecute.setEnabled(false);  
								  }

							  }
							  else{
								  statusPrivateKey1=false;
								  statusPrivateKey2=false;
								  labelStatusPrivateKey1.setIcon(null);
								  labelStatusPrivateKey2.setIcon(null);
							  }
							  
							  
							  
							  if(!textFileInD.getText().equals("")&&statusPrivateKey1&&statusPrivateKey2){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textPrivateKey1.setText("");
							  statusPrivateKey1=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
				}
			}));

		}
		return buttonPrivateKey1;
	}
	private JLabel getLabelStatusPrivateKey1() {
		if (labelStatusPrivateKey1 == null) {
			labelStatusPrivateKey1 = new JLabel();
			labelStatusPrivateKey1.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
			statusPrivateKey1=false;
		}
		return labelStatusPrivateKey1;
	}
	
	private JLabel getLabelFileOutD() {
		if (labelFileOutD == null) {
			labelFileOutD = new JLabel();
			labelFileOutD.setText(rb.getString("title.fileout"));
		}
		return labelFileOutD;
	}
	private JTextField getTextFileOutD() {
		if (textFileOutD == null) {
			textFileOutD = new JTextField();
			textFileOutD.setEditable(false);
		}
		return textFileOutD;
	}
	private JLabel getLabelFileInD() {
		if (labelFileInD == null) {
			labelFileInD = new JLabel();
			labelFileInD.setText(rb.getString("title.filein"));
		}
		return labelFileInD;
	}
	private JTextField getTextFileInD() {
		if (textFileInD == null) {
			textFileInD = new JTextField();
			textFileInD.setEditable(true);
		}
		return textFileInD;
	}
	private JPanel getPanelDecrypt() {
		if (panelDecrypt == null) {
			panelDecrypt = new JPanel();
			panelDecrypt.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelDecrypt.setLayout(new GroupLayout());
			panelDecrypt.add(getLabelStatusPrivateKey1(), new Constraints(new Leading(471, 12, 12), new Leading(63, 12, 12)));
			panelDecrypt.add(getLabelStatusPrivateKey2(), new Constraints(new Leading(471, 12, 12), new Leading(89, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey1(), new Constraints(new Leading(422, 12, 12), new Leading(61, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey2(), new Constraints(new Leading(422, 12, 12), new Leading(89, 12, 12)));
			panelDecrypt.add(getTextPrivateKey1(), new Constraints(new Leading(102, 316, 12, 12), new Leading(62, 24, 44, 48)));
			panelDecrypt.add(getTextPrivateKey2(), new Constraints(new Leading(102, 315, 12, 12), new Leading(88, 24, 12, 12)));
			panelDecrypt.add(getTextFileOutD(), new Constraints(new Leading(114, 380, 12, 12), new Leading(30, 24, 12, 12)));
			panelDecrypt.add(getLabelFileOutD(), new Constraints(new Leading(4, 108, 10, 10), new Leading(36, 12, 12)));
			panelDecrypt.add(getTextFileInD(), new Constraints(new Leading(114, 380, 12, 12), new Leading(4, 24, 12, 12)));
			panelDecrypt.add(getLabelFileInD(), new Constraints(new Leading(4, 12, 12), new Leading(8, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey1(), new Constraints(new Leading(4, 76, 12, 12), new Leading(66, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey2(), new Constraints(new Leading(4, 76, 12, 12), new Leading(88, 12, 12)));
		}
		return panelDecrypt;
	}

	public JMenuBar getJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMenuLanguage());
			menuBar.add(getMenuAbout());
		}
		return menuBar;
	}
	private JMenu getMenuLanguage() {
		if (menuLanguage == null) {
			menuLanguage = new JMenu();
			menuLanguage.setText(rb.getString("title.language"));
			menuLanguage.setMnemonic('L');
			menuLanguage.add(getMenuItalian());
			menuLanguage.add(getMenuEnglish());
		}
		return menuLanguage;
	}
	
	private JMenuItem getMenuItalian() {
		if (menuItalian == null) {
			menuItalian = new javax.swing.JMenuItem("Italiano",new ImageIcon(this.getClass().getResource(iconFlagItaly)));
			menuItalian.setMnemonic('I');
			menuItalian.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Locale.setDefault(Locale.ITALIAN);
					reloadLocale();
				}
			}));
		}
		return menuItalian;
	}
	private JMenuItem getMenuEnglish() {
		if (menuEnglish == null) {
			menuEnglish = new javax.swing.JMenuItem("English",new ImageIcon(this.getClass().getResource(iconFlagUk)));
			menuEnglish.setMnemonic('E');
			menuEnglish.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Locale.setDefault(Locale.ENGLISH);
					reloadLocale();
				}
			}));
		}
		return menuEnglish;
	}

	public void reloadLocale(){
		rb = ResourceBundle.getBundle("MyResources", Locale.getDefault());

		menuLanguage.setText(rb.getString("title.language"));
		menuAbout.setText(rb.getString("title.about"));
		menuVersion.setText(rb.getString("title.version"));
		buttonClose.setText(rb.getString("button.close"));
		labelTitle.setText(rb.getString("title"));
		labelFileInD.setText(rb.getString("title.filein"));
		labelFileOutD.setText(rb.getString("title.fileout"));

		  parent.setTitle(rb.getString("title")+ " - ["+rb.getString("title.decrypt")+"]");		
		  buttonExecute.setText(rb.getString("title.decrypt"));
		  buttonExecute.setMnemonic('D');

        repaint();
	}




	private JMenu getMenuAbout() {
		if (menuAbout == null) {
			menuAbout = new JMenu();
			menuAbout.setText(rb.getString("title.about"));
			menuAbout.setMnemonic('A');
			menuAbout.add(getMenuVersion());			
			
			
		}
		return menuAbout;
	}

	private JMenuItem getMenuVersion() {
		if(menuVersion==null){
			menuVersion = new javax.swing.JMenuItem(rb.getString("title.version"));
			menuVersion.setMnemonic('V');
			menuVersion.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					AboutWindow frame = new AboutWindow(parent,true);
					frame.setIcon();
					frame.setVisible(true);				
				}
			}));
		}
		return menuVersion;
	}

	private JPanel getPanelButtons() {
		if (panelButtons == null) {
			panelButtons = new JPanel();
			panelButtons.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelButtons.setLayout(new GroupLayout());
			panelButtons.add(getButtonExecute(), new Constraints(new Leading(67, 115, 10, 10), new Leading(5, 53, 12, 12)));
			panelButtons.add(getButtonClose(), new Constraints(new Leading(389, 115, 10, 10), new Leading(5, 53, 12, 12)));
		}
		return panelButtons;
	}

	private JButton getButtonExecute() {
		if (buttonExecute == null) {
			buttonExecute = new JButton();
			buttonExecute.setFont(new Font("Serif", Font.BOLD, 16));
			buttonExecute.setText(rb.getString("title.decrypt"));
			buttonExecute.setMnemonic('C');
			buttonExecute.setEnabled(false);
			buttonExecute.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					
						decryptFile();
						
					
					
				}
			}));
			
		}
		return buttonExecute;
	}

	private JButton getButtonClose() {
		if (buttonClose == null) {
			buttonClose = new JButton();
			buttonClose.setText(rb.getString("button.close"));
			buttonClose.setMnemonic('C');
			buttonClose.setFont(new Font("Serif", Font.BOLD, 16));
			buttonClose.setHorizontalAlignment(SwingConstants.CENTER);
			buttonClose.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					System.exit(0);
				}
			}));
			
		}
		return buttonClose;
	}

	private JLabel getLabelIcon() {
		if (labelIcon == null) {
			labelIcon = new JLabel();
			//jLabel0.setText("jLabel0");
			labelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconApplicationsmall)));
		}
		return labelIcon;
	}

	private JPanel getPanelAction() {
		if (panelAction == null) {
			panelAction = new JPanel();
			panelAction.setBorder(new LineBorder(Color.black, 1, false));
			panelAction.setLayout(new GroupLayout());
			panelAction.add(getLabelIcon(), new Constraints(new Leading(8, 44, 10, 10), new Leading(30, 46, 10, 10)));
			panelAction.add(getPanelDecrypt(), new Constraints(new Trailing(12, 508, 10, 10), new Leading(5, 126, 10, 10)));
		}
		return panelAction;
	}

	private JLabel getLabelTitle() {
		if (labelTitle == null) {
			labelTitle = new JLabel();
			labelTitle.setBackground(Color.white);
			labelTitle.setFont(new Font("Serif", Font.BOLD, 30));
			labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
			labelTitle.setText("RSA Keys Decoder");
			labelTitle.setAlignmentY(0.0f);
		}
		return labelTitle;
	}

	private JPanel getPanelTitle() {
		if (panelTitle == null) {
			panelTitle = new JPanel();
			panelTitle.setBackground(Color.white);
			panelTitle.setBorder(new LineBorder(Color.black, 1, false));
			panelTitle.setLayout(new GroupLayout());
			panelTitle.add(getLabelTitle(), new Constraints(new Bilateral(12, 12, 0), new Leading(7, 44, 10, 10)));
		}
		return panelTitle;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	public void setIcon(){
		setTitle(rb.getString("title")+ " - ["+rb.getString("title.decrypt")+"]");
		Dimension dim = this.getToolkit().getScreenSize();
		int screenWidth = dim.width;
		int screenHeight = dim.height;


		setLocation((screenWidth - this.getWidth()) / 2,
				(screenHeight - this.getHeight()) / 2);
		InputStream imgStream = null;
		try {
			imgStream = this.getClass().getResourceAsStream(iconApplication);
			BufferedImage bi = ImageIO.read(imgStream);
			ImageIcon myImg = new ImageIcon(bi);
			this.setIconImage(myImg.getImage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (imgStream != null) {
					imgStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		repaint();
		parent=this;
	}
	
	private void decryptFile(){
		  new Thread() {
				@Override public void run () {
				  String cipherText = textFileInD.getText().trim();
				  
                try{				  
				  System.out.println(cipherText);
			        // decrypt the text using the private key
				  Cipher cipher = Cipher.getInstance("RSA");
			      cipher.init(Cipher.DECRYPT_MODE, privateKey);
			      byte[] cipherData=cipher.doFinal(Base64.decode(cipherText));
			      String dectyptedText = new String(cipherData);

			      textFileOutD.setText(dectyptedText);
				  repaint();
				  
					
				    
					
					JOptionPane.showMessageDialog(parent,
							rb.getString("msg.decipherok"), rb.getString("title.ok") ,
							JOptionPane.INFORMATION_MESSAGE);
					
					
				  }catch (Exception e){
				    e.printStackTrace();
					JOptionPane.showMessageDialog(parent,
							rb.getString("msg.deciphererror"), rb.getString("title.error") ,
							JOptionPane.ERROR_MESSAGE);
				  }
				}
			  }.start();
	}
	
	
	/**
	 * Main entry of the class. Note: This class is only created so that you can
	 * easily preview the result at runtime. It is not expected to be managed by
	 * the designer. You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow frame = new MainWindow();
				frame.setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
				// frame.setTitle("MainWindow");
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				frame.setIcon();
			}
		});
	}

}

class ChunkDecoder extends Thread{
	Cipher cipher = null;
	List<byte[]> byteCipheredList=null;
	List<Object> objList=null;
	Integer index;
	//PrivateKey privateKey = null;
	LinkedBlockingQueue<String> semaphore=null;
    byte[] buf = new byte[512];
    int bufl;

	public void setObjList(List<Object> objList){
		this.objList=objList;
	}
	public void setByteCipheredList(List<byte[]> byteCipheredList){
		this.byteCipheredList=byteCipheredList;
		
	}
	public void setIndex(Integer index){
		this.index=index;
	}
	
	public void setCipher(Cipher cipher){
		this.cipher=cipher;
	}
	/*
	public void setPrivateKey(PrivateKey privateKey){
		//this.privateKey=privateKey;
		
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	public void setSemaphore(LinkedBlockingQueue<String> semaphore){
		this.semaphore=semaphore;
	}
	public void setBuf(byte[] buf){
		this.buf=buf;
	}

	
	@Override 
	public void run () {
		try {
			//cipher.init(Cipher.DECRYPT_MODE, privateKey);
		    byteCipheredList.set(index,cipher.doFinal(buf));
		    
		    

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			objList.remove(index+".");
			if(objList.size()==0){
				semaphore.add("green");
			}
		}
	}
	
}
