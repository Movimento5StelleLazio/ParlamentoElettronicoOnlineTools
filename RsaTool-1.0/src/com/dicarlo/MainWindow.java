package com.dicarlo;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
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
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.jdamico.pskcbuilder.dataobjects.AlgorithmParameters;
import org.jdamico.pskcbuilder.dataobjects.Data;
import org.jdamico.pskcbuilder.dataobjects.DeviceInfo;
import org.jdamico.pskcbuilder.dataobjects.KeyContainer;
import org.jdamico.pskcbuilder.dataobjects.KeyPackage;
import org.jdamico.pskcbuilder.dataobjects.ResponseFormat;
import org.jdamico.pskcbuilder.dataobjects.Secret;
import org.jdamico.pskcbuilder.utils.Constants;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.utils.Base64;




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
	String iconFlagChina="/flag_china.gif";
	
	String iconOk="/ok.png";
	String iconKo="/ko.png";
	
    private byte[] publicKeyBytes =null;
    private byte[] privateKeyBytes = null;
    private byte[] halfkey1=null;
    private byte[] halfkey2=null;

    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    
	private JPanel panelTitle;
	private JLabel labelTitle;
	private JPanel panelAction;
	private JLabel labelIcon;
	private JButton buttonClose;
	private JPanel panelButtons;
	private JFrame parent=null;
	private JMenu menuAbout;
	private JMenu menuFile;
	private JMenu menuLanguage;	
	private JMenuBar menuBar;
	
	javax.swing.JMenuItem menuVersion=null;
	javax.swing.JMenuItem menuEncrypt=null;
	javax.swing.JMenuItem menuDecrypt=null;
	
	javax.swing.JMenuItem menuItalian=null;
	javax.swing.JMenuItem menuEnglish=null;
	javax.swing.JMenuItem menuChinese=null;
	private JProgressBar progressBar;
	
	private boolean encryptMode=true;
	private boolean statusFileIn=false;
	private boolean statusFileOut=false;
	private boolean statusPublicKey=false;
	private boolean statusPrivateKey1=false;
	private boolean statusPrivateKey2=false;
	private boolean statusFileInD=false;
	private boolean statusFileOutD=false;
	
	// START COMPONENT FOR ENCRYPTION
	private JPanel panelEncrypt;
	private JTextField textFileIn;
	private JTextField textFileOut;
	private JTextField textPublicKey;
	private JLabel labelFileIn;
	private JLabel labelFileOut;
	private JLabel labelPublicKey;
	private JButton buttonFileIn;
	private JButton buttonFileOut;
	private JButton buttonPublicKey;
	private JLabel labelStatusFileIn;
	private JLabel labelStatusFileOut;
	private JLabel labelStatusPublicKey;
	// END COMPONENT FOR ENCRYPTION
	
	// START COMPONENT FOR DECRYPTION
	private JPanel panelDecrypt;
	private JTextField textFileInD;
	private JTextField textFileOutD;
	private JTextField textPrivateKey1;
	private JLabel labelFileInD;
	private JLabel labelFileOutD;
	private JLabel labelPrivateKey1;
	private JButton buttonFileInD;
	private JButton buttonFileOutD;
	private JButton buttonPrivateKey1;
	private JLabel labelStatusFileInD;
	private JLabel labelStatusFileOutD;
	private JLabel labelStatusPrivateKey1;
	// END COMPONENT FOR DECRYPTION
	
	private JButton buttonExecute;
	private JLabel labelPrivateKey2;
	private JTextField textPrivateKey2;
	private JButton buttonPrivateKey2;
	private JLabel labelStatusPrivateKey2;
	private JLabel labelWait;
	private JLabel labelProgress;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public MainWindow() {
		initComponents();
	}

	private void initComponents() {
		setResizable(false);
		setLayout(new GroupLayout());
		add(getPanelTitle(), new Constraints(new Bilateral(12, 12, 0), new Leading(7, 57, 10, 10)));
		add(getPanelButtons(), new Constraints(new Leading(12, 584, 12, 12), new Leading(230, 65, 10, 10)));
		add(getProgressBar(), new Constraints(new Bilateral(12, 12, 10), new Leading(214, 12, 12)));
		add(getPanelAction(), new Constraints(new Bilateral(12, 12, 0), new Leading(70, 140, 10, 10)));
		setJMenuBar(getJMenuBar());
		setSize(608, 324);
	}

	// START COMPONENT FOR ENCRYPTION
	private JLabel getLabelPublicKey() {
		if (labelPublicKey == null) {
			labelPublicKey = new JLabel();
			labelPublicKey.setText(rb.getString("title.publickey"));
		}
		return labelPublicKey;
	}
	private JTextField getTextPublicKey() {
		if (textPublicKey == null) {
			textPublicKey = new JTextField();
			textPublicKey.setEditable(false);
		}
		return textPublicKey;
	}
	private JButton getButtonPublicKey() {
		if (buttonPublicKey == null) {
			buttonPublicKey = new JButton();
			buttonPublicKey.setText("....");
			buttonPublicKey.addActionListener((new java.awt.event.ActionListener() {
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
							  labelStatusPublicKey.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
							  textPublicKey.setText(file.getAbsolutePath());
							  FileInputStream fis=null;
							  try{
								fis = new FileInputStream(file);
								publicKeyBytes = new byte[(int)file.length()];
								fis.read(publicKeyBytes);
							    publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
							    fis.close();
							    fis=null;
							  }
							  catch(Exception e){
								e.printStackTrace();
								publicKey=null;
								publicKeyBytes=null;
								textPublicKey.setText("");
								labelStatusPublicKey.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
								JOptionPane.showMessageDialog(parent,
										rb.getString("msg.invalidpublickey"), rb.getString("title.error") ,
										JOptionPane.ERROR_MESSAGE);
							  }
							  finally{
								try{
								  if(fis!=null)
									  fis.close();
								}
								catch(Exception e){
								  e.printStackTrace();
								}
							  }
							  
							  statusPublicKey=(publicKey!=null);
							  if(statusFileOut&&statusFileIn&&statusPublicKey){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusPublicKey.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textPublicKey.setText("");
							  statusPublicKey=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
				}
			}));

		}
		return buttonPublicKey;
	}
	private JLabel getLabelStatusPublicKey() {
		if (labelStatusPublicKey == null) {
			labelStatusPublicKey = new JLabel();
			labelStatusPublicKey.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
			statusPublicKey=false;
		}
		return labelStatusPublicKey;
	}
	
	private JLabel getLabelFileOut() {
		if (labelFileOut == null) {
			labelFileOut = new JLabel();
			labelFileOut.setText(rb.getString("title.fileout"));
		}
		return labelFileOut;
	}
	private JTextField getTextFileOut() {
		if (textFileOut == null) {
			textFileOut = new JTextField();
			textFileOut.setEditable(false);
		}
		return textFileOut;
	}
	private JButton getButtonFileOut() {
		if (buttonFileOut == null) {
			buttonFileOut = new JButton();
			buttonFileOut.setText("....");
			buttonFileOut.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(!file.isDirectory()&&!"".equals(file.getAbsolutePath())){
							  labelStatusFileOut.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
							  textFileOut.setText(file.getAbsolutePath());
							  statusFileOut=true;
							  if(statusFileOut&&statusFileIn&&statusPublicKey){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusFileOut.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textFileOut.setText("");
							  statusFileOut=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
					
				}
			}));
		}
		return buttonFileOut;
	}
	private JLabel getLabelStatusFileOut() {
		if (labelStatusFileOut == null) {
			labelStatusFileOut = new JLabel();
			labelStatusFileOut.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
			statusFileOut=false;
		}
		return labelStatusFileOut;
	}


	private JLabel getLabelFileIn() {
		if (labelFileIn == null) {
			labelFileIn = new JLabel();
			labelFileIn.setText(rb.getString("title.filein"));
		}
		return labelFileIn;
	}
	private JTextField getTextFileIn() {
		if (textFileIn == null) {
			textFileIn = new JTextField();
			textFileIn.setEditable(false);
		}
		return textFileIn;
	}
	private JButton getButtonFileIn() {
		if (buttonFileIn == null) {
			buttonFileIn = new JButton();
			buttonFileIn.setText("....");
			buttonFileIn.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(file.exists()){
							  labelStatusFileIn.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
							  textFileIn.setText(file.getAbsolutePath());
							  statusFileIn=true;
							  if(statusFileOut&&statusFileIn&&statusPublicKey){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusFileIn.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textFileIn.setText("");
							  statusFileIn=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
				}}));

		}
		return buttonFileIn;
	}
	private JLabel getLabelStatusFileIn() {
		if (labelStatusFileIn == null) {
			labelStatusFileIn = new JLabel();
			labelStatusFileIn.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
			statusFileIn=false;
		}
		return labelStatusFileIn;
	}


	private JPanel getPanelEncrypt() {
		if (panelEncrypt == null) {
			panelEncrypt = new JPanel();
			panelEncrypt.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelEncrypt.setLayout(new GroupLayout());
			panelEncrypt.add(getTextFileIn(), new Constraints(new Leading(91, 319, 10, 10), new Leading(5, 24, 10, 10)));
			panelEncrypt.add(getTextFileOut(), new Constraints(new Leading(91, 319, 12, 12), new Leading(35, 24, 12, 12)));
			panelEncrypt.add(getTextPublicKey(), new Constraints(new Leading(91, 319, 12, 12), new Leading(65, 24, 12, 12)));
			panelEncrypt.add(getButtonFileIn(), new Constraints(new Leading(413, 10, 10), new Leading(4, 12, 12)));
			panelEncrypt.add(getButtonFileOut(), new Constraints(new Leading(413, 12, 12), new Leading(35, 12, 12)));
			panelEncrypt.add(getButtonPublicKey(), new Constraints(new Leading(413, 12, 12), new Leading(65, 12, 12)));
			panelEncrypt.add(getLabelStatusFileIn(), new Constraints(new Leading(465, 12, 12), new Leading(5, 12, 12)));
			panelEncrypt.add(getLabelStatusFileOut(), new Constraints(new Leading(465, 12, 12), new Leading(37, 12, 12)));
			panelEncrypt.add(getLabelStatusPublicKey(), new Constraints(new Leading(465, 12, 12), new Leading(65, 12, 12)));
			panelEncrypt.add(getLabelFileOut(), new Constraints(new Leading(4, 81, 12, 12), new Leading(41, 12, 12)));
			panelEncrypt.add(getLabelPublicKey(), new Constraints(new Leading(4, 80, 12, 12), new Leading(69, 12, 12)));
			panelEncrypt.add(getLabelFileIn(), new Constraints(new Leading(4, 73, 10, 10), new Leading(12, 12, 12)));
		}
		return panelEncrypt;
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
								  if(statusFileOut&&statusFileIn&&statusPrivateKey1&&statusPrivateKey2){
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
							  
							  
							  
							  if(statusFileOutD&&statusFileInD&&statusPrivateKey1&&statusPrivateKey2){
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
								  if(statusFileOut&&statusFileIn&&statusPrivateKey1&&statusPrivateKey2){
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
							  
							  
							  
							  if(statusFileOutD&&statusFileInD&&statusPrivateKey1&&statusPrivateKey2){
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
	private JButton getButtonFileOutD() {
		if (buttonFileOutD == null) {
			buttonFileOutD = new JButton();
			buttonFileOutD.setText("....");
			buttonFileOutD.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(!file.isDirectory()&&!"".equals(file.getAbsolutePath())){
							  labelStatusFileOutD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
							  textFileOutD.setText(file.getAbsolutePath());
							  statusFileOutD=true;
							  if(statusFileOutD&&statusFileInD&&statusPrivateKey1){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusFileOutD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textFileOutD.setText("");
							  statusFileOutD=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
					
				}
			}));
		}
		return buttonFileOutD;
	}
	private JLabel getLabelStatusFileOutD() {
		if (labelStatusFileOutD == null) {
			labelStatusFileOutD = new JLabel();
			labelStatusFileOutD.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
			statusFileOutD=false;
		}
		return labelStatusFileOutD;
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
			textFileInD.setEditable(false);
		}
		return textFileInD;
	}
	private JButton getButtonFileInD() {
		if (buttonFileInD == null) {
			buttonFileInD = new JButton();
			buttonFileInD.setText("....");
			buttonFileInD.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					JFileChooser fileChooser = new JFileChooser();
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
						if (file != null){
						  if(file.exists()){
							  labelStatusFileInD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconOk)));
							  textFileInD.setText(file.getAbsolutePath());
							  statusFileInD=true;
							  if(statusFileOutD&&statusFileInD&&statusPrivateKey1){
								  buttonExecute.setEnabled(true);  
							  }
							  else{
								  buttonExecute.setEnabled(false);  
							  }
						  }
						  else{
							  labelStatusFileInD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
							  textFileInD.setText("");
							  statusFileInD=false;
							  buttonExecute.setEnabled(false);
						  }
						}
					  
					}
					repaint();
				}}));

		}
		return buttonFileInD;
	}
	private JLabel getLabelStatusFileInD() {
		if (labelStatusFileInD == null) {
			labelStatusFileInD = new JLabel();
			labelStatusFileInD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
			statusFileInD=false;
		}
		return labelStatusFileInD;
	}


	private JPanel getPanelDecrypt() {
		if (panelDecrypt == null) {
			panelDecrypt = new JPanel();
			panelDecrypt.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelDecrypt.setLayout(new GroupLayout());
			panelDecrypt.add(getTextFileInD(), new Constraints(new Leading(91, 319, 12, 12), new Leading(4, 24, 12, 12)));
			panelDecrypt.add(getButtonFileInD(), new Constraints(new Leading(413, 12, 12), new Leading(3, 12, 12)));
			panelDecrypt.add(getLabelStatusFileInD(), new Constraints(new Leading(465, 12, 12), new Leading(4, 12, 12)));
			panelDecrypt.add(getLabelFileInD(), new Constraints(new Leading(12, 47, 12, 12), new Leading(8, 12, 12)));
			panelDecrypt.add(getTextFileOutD(), new Constraints(new Leading(91, 319, 12, 12), new Leading(30, 24, 12, 12)));
			panelDecrypt.add(getLabelFileOutD(), new Constraints(new Leading(12, 50, 12, 12), new Leading(36, 12, 12)));
			panelDecrypt.add(getButtonFileOutD(), new Constraints(new Leading(413, 12, 12), new Leading(30, 12, 12)));
			panelDecrypt.add(getLabelStatusFileOutD(), new Constraints(new Leading(465, 12, 12), new Leading(30, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey2(), new Constraints(new Leading(12, 76, 12, 12), new Leading(94, 12, 12)));
			panelDecrypt.add(getTextPrivateKey1(), new Constraints(new Leading(91, 319, 12, 12), new Leading(62, 24, 44, 48)));
			panelDecrypt.add(getTextPrivateKey2(), new Constraints(new Leading(91, 319, 12, 12), new Leading(88, 24, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey1(), new Constraints(new Leading(413, 12, 12), new Leading(62, 12, 12)));
			panelDecrypt.add(getLabelStatusPrivateKey1(), new Constraints(new Leading(465, 12, 12), new Leading(62, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey1(), new Constraints(new Leading(12, 76, 12, 12), new Leading(67, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey2(), new Constraints(new Leading(413, 12, 12), new Leading(89, 12, 12)));
			panelDecrypt.add(getLabelStatusPrivateKey2(), new Constraints(new Leading(465, 24, 12, 12), new Leading(90, 12, 12)));
			panelDecrypt.setVisible(false);
		}
		return panelDecrypt;
	}

	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar(0, 100);
			progressBar.setVisible(false);
		}
		return progressBar;
	}

	public JMenuBar getJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMenuFile());
			menuBar.add(getMenuLanguage());
			menuBar.add(getMenuAbout());
		}
		return menuBar;
	}
	private JMenu getMenuFile() {
		if (menuFile == null) {
			menuFile = new JMenu();
			menuFile.setText(rb.getString("title.file"));
			menuFile.setMnemonic('F');
			menuFile.add(getMenuEncrypt());
			menuFile.add(getMenuDecrypt());
			
		}
		return menuFile;
	}
	private JMenu getMenuLanguage() {
		if (menuLanguage == null) {
			menuLanguage = new JMenu();
			menuLanguage.setText(rb.getString("title.language"));
			menuLanguage.setMnemonic('L');
			menuLanguage.add(getMenuItalian());
			menuLanguage.add(getMenuEnglish());
			menuLanguage.add(getMenuChinese());
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
	private JMenuItem getMenuChinese() {
		if (menuChinese == null) {
			menuChinese = new javax.swing.JMenuItem("中国的",new ImageIcon(this.getClass().getResource(iconFlagChina)));
			menuChinese.setMnemonic('C');
			menuChinese.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					Locale.setDefault(Locale.CHINESE);
					reloadLocale();
				}
			}));
		}
		return menuChinese;
	}

	public void reloadLocale(){
		rb = ResourceBundle.getBundle("MyResources", Locale.getDefault());

		menuFile.setText(rb.getString("title.file"));
		menuLanguage.setText(rb.getString("title.language"));
		menuEncrypt.setText(rb.getString("title.encrypt"));
		menuDecrypt.setText(rb.getString("title.decrypt"));
		menuAbout.setText(rb.getString("title.about"));
		menuVersion.setText(rb.getString("title.version"));
		buttonClose.setText(rb.getString("button.close"));
		labelTitle.setText(rb.getString("title"));
		
		labelPublicKey.setText(rb.getString("title.publickey"));
		labelFileOut.setText(rb.getString("title.fileout"));
		labelFileIn.setText(rb.getString("title.filein"));
		if(encryptMode){
		  parent.setTitle(rb.getString("title")+ " - ["+rb.getString("title.encrypt")+"]");
		  buttonExecute.setText(rb.getString("title.encrypt"));
		  buttonExecute.setMnemonic('C');
		}
		else{
		  parent.setTitle(rb.getString("title")+ " - ["+rb.getString("title.decrypt")+"]");		
		  buttonExecute.setText(rb.getString("title.decrypt"));
		  buttonExecute.setMnemonic('D');
		}

        repaint();
	}


	private JMenuItem getMenuEncrypt() {
		if (menuEncrypt == null) {
			menuEncrypt = new javax.swing.JMenuItem(rb.getString("title.encrypt"));
			menuEncrypt.setMnemonic('C');
			menuEncrypt.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					encryptMode=true;
					parent.setTitle(rb.getString("title")+ " - ["+rb.getString("title.encrypt")+"]");
					panelEncrypt.setVisible(true);
					panelDecrypt.setVisible(false);
					buttonExecute.setText(rb.getString("title.encrypt"));
					buttonExecute.setMnemonic('C');
					buttonExecute.setEnabled(false);
					textPublicKey.setText("");
					statusPublicKey=false;
					labelStatusPublicKey.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					textFileIn.setText("");
					statusFileIn=false;
					labelStatusFileIn.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					textFileOut.setText("");
					statusFileOut=false;
					labelStatusFileOut.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					publicKey=null;
					publicKeyBytes=null;
				}
			}));
		}
		return menuEncrypt;
	}
	private JMenuItem getMenuDecrypt() {
		if(menuDecrypt==null){
			menuDecrypt = new javax.swing.JMenuItem(rb.getString("title.decrypt"));
			menuDecrypt.setMnemonic('D');
			menuDecrypt.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					encryptMode=false;
					parent.setTitle(rb.getString("title")+ " - ["+rb.getString("title.decrypt")+"]");			
					panelEncrypt.setVisible(false);
					panelDecrypt.setVisible(true);
					buttonExecute.setText(rb.getString("title.decrypt"));
					buttonExecute.setMnemonic('D');
					buttonExecute.setEnabled(false);
					textPrivateKey1.setText("");
					statusPrivateKey1=false;
					labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					textPrivateKey2.setText("");
					statusPrivateKey2=false;
					labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					textFileInD.setText("");
					statusFileInD=false;
					labelStatusFileInD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));
					textFileOutD.setText("");
					statusFileOutD=false;
					labelStatusFileOutD.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));

				}
			}));
		}
		return menuDecrypt;
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
			panelButtons.add(getLabelWait(), new Constraints(new Leading(266, 10, 10), new Leading(26, 12, 12)));
			panelButtons.add(getLabelProgress(), new Constraints(new Leading(218, 10, 10), new Leading(3, 10, 10)));
		}
		return panelButtons;
	}

	private JButton getButtonExecute() {
		if (buttonExecute == null) {
			buttonExecute = new JButton();
			buttonExecute.setFont(new Font("Serif", Font.BOLD, 16));
			buttonExecute.setText(rb.getString("title.encrypt"));
			buttonExecute.setMnemonic('C');
			buttonExecute.setEnabled(false);
			buttonExecute.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					if(encryptMode){
						if(statusFileIn&&statusFileOut&&statusPublicKey){
							encryptFile();
						}
						else{
							
						}
					}
					else{
						if(statusFileInD&&statusFileOutD&&statusPrivateKey1&&statusPrivateKey2){
							decryptFile();
						}
						else{
							
						}
					}
					
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
	private JLabel getLabelWait() {
		if (labelWait == null) {
			labelWait = new JLabel();
			labelWait.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconLoading)));
			labelWait.setVisible(false);
		}
		return labelWait;
	}

	private JLabel getLabelIcon() {
		if (labelIcon == null) {
			labelIcon = new JLabel();
			//jLabel0.setText("jLabel0");
			labelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconApplicationsmall)));
		}
		return labelIcon;
	}
	private JLabel getLabelProgress() {
		if (labelProgress == null) {
			labelProgress = new JLabel();
			labelProgress.setForeground(new Color(0, 0, 255));
			labelProgress.setFont(new Font("Serif", Font.BOLD, 16));
			labelProgress.setText("jLabel0");
			labelProgress.setVisible(false);
		}
		return labelProgress;
	}

	private JPanel getPanelAction() {
		if (panelAction == null) {
			panelAction = new JPanel();
			panelAction.setBorder(new LineBorder(Color.black, 1, false));
			panelAction.setLayout(new GroupLayout());
			panelAction.add(getLabelIcon(), new Constraints(new Leading(8, 53, 18, 18), new Leading(30, 54, 12, 12)));
			panelAction.add(getPanelEncrypt(), new Constraints(new Bilateral(67, 12, 0), new Leading(5, 126, 10, 10)));
			panelAction.add(getPanelDecrypt(), new Constraints(new Bilateral(67, 12, 0), new Leading(5, 126, 10, 10)));
		}
		return panelAction;
	}

	private JLabel getLabelTitle() {
		if (labelTitle == null) {
			labelTitle = new JLabel();
			labelTitle.setText(rb.getString("title"));
			labelTitle.setBackground(Color.white);
			labelTitle.setFont(new Font("Serif", Font.BOLD, 30));
			labelTitle.setLayout(new GroupLayout());
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setVerticalAlignment(SwingConstants.CENTER);
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
		setTitle(rb.getString("title")+ " - ["+rb.getString("title.encrypt")+"]");
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
	private void encryptFile(){
	  new Thread() {
		@Override public void run () {
		  String srcFileName = textFileIn.getText();
		  String destFileName = textFileOut.getText();
		  progressBar.setVisible(true);
		  progressBar.setMaximum(0);
		  progressBar.setMaximum(100);
		  progressBar.setValue(0);
		  labelProgress.setText(rb.getString("msg.ciphering")+" 0%");
		  labelProgress.setVisible(true);
		  labelWait.setVisible(true);
		  buttonClose.setEnabled(false);
		  buttonExecute.setEnabled(false);
          buttonFileIn.setEnabled(false);
          buttonFileOut.setEnabled(false);
          buttonPublicKey.setEnabled(false);
          menuFile.setEnabled(false);
          menuAbout.setEnabled(false);
          menuLanguage.setEnabled(false);
		  repaint();
		  long fileLen=new File(srcFileName).length();
		  OutputStream outputWriter = null;
		  InputStream inputReader = null;	
		  //System.out.println("srcFileName ----- "+srcFileName);	
		  //System.out.println("destFileName ----- "+destFileName);	
		  try{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);		    
            int counter=0;
            int progressValue=0;
            int prevProgressValue=0;
            
		    //byte[] buf = new byte[100];
            byte[] buf = new byte[100];
		    int bufl;
		
		    outputWriter = new FileOutputStream(destFileName);
		    inputReader = new FileInputStream(srcFileName);
		    while ( (bufl = inputReader.read(buf)) != -1){
		      counter+=bufl;
		      byte[] cipherText = null;
		      cipherText = cipher.doFinal(copyBytes(buf,bufl));
		      
		      outputWriter.write(cipherText);
		      outputWriter.flush();
		      progressValue=(int)((counter*100)/fileLen);
		      if(progressValue!=prevProgressValue){
		        progressBar.setValue(progressValue);
		        labelProgress.setText(rb.getString("msg.ciphering")+" "+progressValue+ "%");
		        prevProgressValue=progressValue;		        
		        //System.out.println(prevProgressValue+"%");
		        repaint();
		      }

		    }
		    outputWriter.flush();
		    outputWriter.close();
		    inputReader.close();
		    outputWriter=null;
		    inputReader=null;
		    progressBar.setVisible(false);
			labelProgress.setVisible(false);
			labelWait.setVisible(false);

			  JOptionPane.showMessageDialog(parent,
						rb.getString("msg.cipherok"), rb.getString("title.ok") ,
						JOptionPane.INFORMATION_MESSAGE);

		  }catch (Exception e){
		    e.printStackTrace();
			JOptionPane.showMessageDialog(parent,
					rb.getString("msg.ciphererror"), rb.getString("title.error") ,
					JOptionPane.ERROR_MESSAGE);
		  }
		  finally{
		    try{			
		      if (outputWriter != null)
		        outputWriter.close();			
		      if (inputReader != null)
		        inputReader.close();
		    }catch (Exception e){
		      e.printStackTrace();
		    }
		    progressBar.setVisible(false);
			labelProgress.setVisible(false);
			labelWait.setVisible(false);
			buttonClose.setEnabled(true);
			buttonExecute.setEnabled(true);

	        buttonFileIn.setEnabled(true);
	        buttonFileOut.setEnabled(true);
	        buttonPublicKey.setEnabled(true);
	        menuFile.setEnabled(true);
	        menuAbout.setEnabled(true);
	        menuLanguage.setEnabled(true);

			
		  }
		}
	  }.start();
	}
	private void decryptFile(){
		  new Thread() {
				@Override public void run () {
				  String srcFileName = textFileInD.getText();
				  String destFileName = textFileOutD.getText();
				  progressBar.setVisible(true);
				  progressBar.setMaximum(0);
				  progressBar.setMaximum(100);
				  progressBar.setValue(0);
				  labelProgress.setText(rb.getString("msg.deciphering")+" 0%");
				  labelProgress.setVisible(true);
				  labelWait.setVisible(true);
				  buttonClose.setEnabled(false);
				  buttonExecute.setEnabled(false);
		          buttonFileInD.setEnabled(false);
		          buttonFileOutD.setEnabled(false);
		          buttonPrivateKey1.setEnabled(false);
		          buttonPrivateKey2.setEnabled(false);
		          menuFile.setEnabled(false);
		          menuAbout.setEnabled(false);
		          menuLanguage.setEnabled(false);
				  repaint();
				  
				  LinkedBlockingQueue<String> semaphore = new LinkedBlockingQueue<String>();
				  long fileLen=new File(srcFileName).length();
				  int numberThreads=(int)(fileLen/512);
				  OutputStream outputWriter = null;
				  InputStream inputReader = null;	
				  //System.out.println("srcFileName ----- "+srcFileName);	
				  //System.out.println("destFileName ----- "+destFileName);	
				  try{
					int index=0;
		            int counter=0;
		            int progressValue=0;
		            int prevProgressValue=0;
		            int maxThread=0;

		            List<Object> objList = Collections.synchronizedList(new ArrayList<Object>());
		            List<byte[]> byteCipheredList = Collections.synchronizedList(new ArrayList<byte[]>());
		            Cipher[] cipherArray = new Cipher[51];
		            for(int i=0;i<51;i++){
						Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
						cipher.init(Cipher.DECRYPT_MODE, privateKey);	
						cipherArray[i]=cipher;
		            }
		            
		            
		            if(numberThreads<50){
		            	maxThread=numberThreads;
		            }
		            else{
		            	maxThread=50;
		            }
		            
				    byte[] buf = new byte[512];
				    int bufl;
				    
				    outputWriter = new FileOutputStream(destFileName);
				    inputReader = new FileInputStream(srcFileName);				    
		            while ( (bufl = inputReader.read(buf)) != -1){
		            	counter+=bufl;
		            	byteCipheredList.add(null);
		            	objList.add((byteCipheredList.size()-1)+".");
		            	
		            	ChunkDecoder chunkDecoder = new ChunkDecoder();
		            	chunkDecoder.setByteCipheredList(byteCipheredList);
		            	chunkDecoder.setObjList(objList);
		            	//chunkDecoder.setPrivateKey(privateKey);
		            	chunkDecoder.setSemaphore(semaphore);		            	
		            	chunkDecoder.setCipher(cipherArray[byteCipheredList.size()-1]);
		            	chunkDecoder.setIndex(byteCipheredList.size()-1);
		            	chunkDecoder.setBuf(copyBytes(buf,bufl));

		            	
		            	
		            	chunkDecoder.start();
		            	//System.out.println(index);
		            	buf = new byte[512];
		            	index++;
		            	if(index>maxThread){
		            		
		            		semaphore.take();
		 		            	for(int i=0;i<byteCipheredList.size();i++){
		 		            		byte[]decipherText=byteCipheredList.get(i);
		 		            		
		 						    outputWriter.write(decipherText);
		 						    outputWriter.flush();

		 		            	}
		 					      progressValue=(int)((counter*100)/fileLen);
		 					      if(progressValue!=prevProgressValue){
		 					        progressBar.setValue(progressValue);
		 					        labelProgress.setText(rb.getString("msg.deciphering")+" "+progressValue+ "%");
		 					        prevProgressValue=progressValue;		        
		 					        //System.out.println(prevProgressValue+"%");
		 					        repaint();
		 					      }
		 					     byteCipheredList.clear();
				            	  index=0;
		 		            
		            		
		            		
			            }
		            }
		            
            		while(byteCipheredList.size()>0){
            		if(objList.size()==0){
 		            	for(int i=0;i<byteCipheredList.size();i++){
 		            		byte[]decipherText=byteCipheredList.get(i);
 		            		
 						    outputWriter.write(decipherText);
 						    outputWriter.flush();

 		            	}
 					     byteCipheredList.clear();
 		            }
            		else{
            			Thread.sleep(200);
            		}
            		}
				      progressValue=(int)((counter*100)/fileLen);
				      if(progressValue!=prevProgressValue){
				        progressBar.setValue(progressValue);
				        labelProgress.setText(rb.getString("msg.deciphering")+" "+progressValue+ "%");
				        prevProgressValue=progressValue;		        
				        //System.out.println(prevProgressValue+"%");
				        repaint();
				      }
				    
				    outputWriter.flush();
				    outputWriter.close();
				    inputReader.close();
				    outputWriter=null;
				    inputReader=null;
					
					List<String> lst = getListStringFromFile(destFileName);
                    new File(destFileName).delete();
					KeyContainer kc = new KeyContainer();
					List<KeyPackage> keyPackageList = new ArrayList<KeyPackage>();
					AlgorithmParameters ap = new AlgorithmParameters(new ResponseFormat("6", "DECIMAL"), Constants.ALGO_TYPE_TOTP);
                    String separator=";";
					if(lst!=null&&lst.size()>0){
						String line = lst.get(0);
						if(line.indexOf(";")>0){
							separator=";";
						}
						else if(line.indexOf(":")>0){
							separator=":";
						}
						else if(line.indexOf(";")>0){
							separator=";";
						}
						progressBar.setValue(0);
				        labelProgress.setText(rb.getString("msg.formattingkeys")+"0%");
				        repaint();
				        
						for (int i=0;i<lst.size();i++) {
							String string = lst.get(i);
							//StringTokenizer st = new StringTokenizer(string, ";");
	                        String[] serialkey=string.split(separator);
	                        String lineserial=serialkey[0];
	                        String linekey=serialkey[1];
							DeviceInfo di = new DeviceInfo("xyzw", lineserial);
							byte[] byteSeed = hexStringToByteArray(linekey);

							Data d = new Data(new Secret(Base64.encode(byteSeed)), "60", "0");
							org.jdamico.pskcbuilder.dataobjects.Key k= new org.jdamico.pskcbuilder.dataobjects.Key("1", "urn:ietf:params:xml:ns:keyprov:pskc:" + Constants.ALGO_TYPE_TOTP, "xyzw", d, ap);
							keyPackageList.add(new KeyPackage(di, k));
							progressValue=(int)((i*100)/lst.size());
						    if(progressValue!=prevProgressValue){
							  progressBar.setValue(progressValue);
							  labelProgress.setText(rb.getString("msg.formattingkeys")+" "+progressValue+ "%");
							  prevProgressValue=progressValue;		        
							  //System.out.println(prevProgressValue+"%");
							  repaint();
							}
						}

						kc.setKeyPackageList(keyPackageList);

						String xmlStr = Obj2XmlStr(kc);
						if(isDocValid(xmlStr)){ 
							stringToFile(xmlStr, destFileName);
							JOptionPane.showMessageDialog(parent,
										rb.getString("msg.decipherok"), rb.getString("title.ok") ,
										JOptionPane.INFORMATION_MESSAGE);
                            return;
	
						}
					}
					JOptionPane.showMessageDialog(parent,
							rb.getString("msg.deciphererror"), rb.getString("title.error") ,
							JOptionPane.ERROR_MESSAGE);
				  }catch (Exception e){
				    e.printStackTrace();
					JOptionPane.showMessageDialog(parent,
							rb.getString("msg.deciphererror"), rb.getString("title.error") ,
							JOptionPane.ERROR_MESSAGE);
				  }
				  finally{
				    try{			
				      if (outputWriter != null)
				        outputWriter.close();			
				      if (inputReader != null)
				        inputReader.close();
				    }catch (Exception e){
				      e.printStackTrace();
				    }
				    progressBar.setVisible(false);
					labelProgress.setVisible(false);
					labelWait.setVisible(false);
					buttonClose.setEnabled(true);
					buttonExecute.setEnabled(true);

			        buttonFileInD.setEnabled(true);
			        buttonFileOutD.setEnabled(true);
			        buttonPrivateKey1.setEnabled(true);
			        buttonPrivateKey2.setEnabled(true);
			        menuFile.setEnabled(true);
			        menuAbout.setEnabled(true);
			        menuLanguage.setEnabled(true);

					
				  }
				}
			  }.start();
	}
	public synchronized void stringToFile(String content, String strFilePath) throws IOException {
	    progressBar.setValue(0);
	    labelProgress.setText(rb.getString("msg.writingkeys")+"0%");
        repaint();

		BufferedWriter out=null;
		FileWriter fw=null;
		try{
		  fw=new FileWriter(strFilePath);
		  out = new BufferedWriter(fw);
		  out.write(content);
		  out.flush();
		  out.close();
		  fw.close();
		  out=null;
		  fw=null;
		  progressBar.setValue(100);
		  labelProgress.setText(rb.getString("msg.writingkeys")+"100%");
	      repaint();

		}
		finally{
	      if(out!=null)
	        out.close();
	      if(fw!=null)
	        fw.close();
		}

	}

	public synchronized List<String> getListStringFromFile(String filePath) throws IOException {

	    progressBar.setValue(0);
	    labelProgress.setText(rb.getString("msg.readingkeys")+"0%");
        repaint();
        long fileLen=new File(filePath).length();
        long counter =0;
        int progressValue=0;
        int prevProgressValue=0;
		List<String> ret = new ArrayList<String>();
		FileReader fr=null;
		BufferedReader in=null;
        try{
          fr = new FileReader(filePath);
		  in = new BufferedReader(fr);
		  String str;
		  while ((str = in.readLine()) != null){ 
			  ret.add(str);
			  counter+=str.length();
			  progressValue=(int)((counter*100)/fileLen);
		      if(progressValue!=prevProgressValue){
		        progressBar.setValue(progressValue);
		        labelProgress.setText(rb.getString("msg.readingkeys")+" "+progressValue+ "%");
		        prevProgressValue=progressValue;		        
		        //System.out.println(prevProgressValue+"%");
		        repaint();
		      }
		    
		  }
		  in.close();
		  fr.close();
		  in=null;
		  fr=null;
		  progressBar.setValue(100);
		  labelProgress.setText(rb.getString("msg.readingkeys")+"100%");
	      repaint();
        }
        finally{
          if(in!=null)
        	  in.close();
          if(fr!=null)
        	  fr.close();
        }

		return ret;
	}
	public synchronized byte[] hexStringToByteArray(String s) throws Exception {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		return data;
	}
	public synchronized boolean isDocValid(String xml) {

		boolean ret = false;
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		Source schemaFile = new StreamSource(getClass().getClassLoader().getResourceAsStream(Constants.XSD_PATH));

		
		
			Schema schema = null;
			try {
				schema = factory.newSchema(schemaFile);
			} catch (SAXException e) {
				ret = false;
				e.printStackTrace();
			}
			Validator validator = schema.newValidator();
			Source source = new StreamSource(new StringReader(xml));
			try {
				validator.validate(source);
				ret=true;
			} catch (SAXException e) {
				ret = false;
				e.printStackTrace();
			} catch (IOException e) {
				ret = false;
				e.printStackTrace();
			}
		

		return ret;

	}
	public synchronized String Obj2XmlStr(KeyContainer kc) {
		
	    progressBar.setValue(0);
	    labelProgress.setText(rb.getString("msg.exportingkeys")+"0%");
        repaint();

        int progressValue=0;
        int prevProgressValue=0;

        StringBuffer sb = new StringBuffer();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<KeyContainer Version=\"1.0\" xmlns=\"urn:ietf:params:xml:ns:keyprov:pskc\">\n");
		
		for (int i = 0; i < kc.getKeyPackageList().size(); i++) {
			
			sb.append("<KeyPackage>\n");
			sb.append("<DeviceInfo>\n");
			sb.append("<Manufacturer>"+kc.getKeyPackageList().get(i).getDeviceInfo().getManufacturer()+"</Manufacturer>\n");
			sb.append("<SerialNo>"+kc.getKeyPackageList().get(i).getDeviceInfo().getSerialNo()+"</SerialNo>\n");
			sb.append("</DeviceInfo>\n");
			sb.append("<Key Id=\""+(i+1)+"\" Algorithm=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithm()+"\">\n");
			sb.append("<Issuer>"+kc.getKeyPackageList().get(i).getKey().getIssuer()+"</Issuer>\n");
			sb.append("<AlgorithmParameters>\n");
			sb.append("<ResponseFormat Length=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getResponseFormat().getLength()+"\" Encoding=\""+kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getResponseFormat().getEncoding()+"\"/>\n");
			sb.append("</AlgorithmParameters>\n");
			sb.append("<Data>\n");
			sb.append("<Secret><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getSecret().getPlainValue()+"</PlainValue></Secret>\n");
			
			String sAlgoType = kc.getKeyPackageList().get(i).getKey().getAlgorithmParameters().getAlgoType() == Constants.ALGO_TYPE_HOTP ? "Counter" : "Time";
			
			sb.append("<"+sAlgoType+"><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getCounter()+"</PlainValue></"+sAlgoType+">\n");
			sb.append("<TimeInterval><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getTimeInterval()+"</PlainValue></TimeInterval>\n");
					//"<TimeDrift><PlainValue>"+kc.getKeyPackageList().get(i).getKey().getData().getTimeInterval()+"</PlainValue></TimeDrift>\n" +
			sb.append("</Data>\n");
			sb.append("</Key>\n");
			sb.append("</KeyPackage>\n");
			
			progressValue=(int)((i*100)/kc.getKeyPackageList().size());
		    if(progressValue!=prevProgressValue){
		      progressBar.setValue(progressValue);
		      labelProgress.setText(rb.getString("msg.exportingkeys")+" "+progressValue+ "%");
		      prevProgressValue=progressValue;		        
		      //System.out.println(prevProgressValue+"%");
		      repaint();
		    }

		}
		
		sb.append("</KeyContainer>");
		
		   
		
		return sb.toString();
	}

	public synchronized byte[] copyBytes(byte[] arr, int length)
	{
		byte[] newArr = null;
		if (arr.length == length)
			newArr = arr;
		else
		{
			newArr = new byte[length];
			for (int i = 0; i < length; i++)
			{
				newArr[i] = (byte) arr[i];
			}
		}
		return newArr;
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
