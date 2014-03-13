package com.dicarlo;

import ietf.params.xml.ns.keyprov.pskc.BinaryDataType;
import ietf.params.xml.ns.keyprov.pskc.DeviceInfoType;
import ietf.params.xml.ns.keyprov.pskc.KeyContainerType;
import ietf.params.xml.ns.keyprov.pskc.KeyDataType;
import ietf.params.xml.ns.keyprov.pskc.KeyPackageType;
import ietf.params.xml.ns.keyprov.pskc.KeyType;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.jdamico.pskcbuilder.dataobjects.AlgorithmParameters;
import org.jdamico.pskcbuilder.dataobjects.Data;
import org.jdamico.pskcbuilder.dataobjects.DeviceInfo;
import org.jdamico.pskcbuilder.dataobjects.KeyPackage;
import org.jdamico.pskcbuilder.dataobjects.ResponseFormat;
import org.jdamico.pskcbuilder.dataobjects.Secret;
import org.jdamico.pskcbuilder.utils.Constants;
import org.w3._2001._04.xmlenc_.EncryptedDataType;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/*
 AUTHOR
 MARCO DI CARLO
 */

//VS4E -- DO NOT REMOVE THIS LINE!
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger("trace");
	private ResourceBundle rb = ResourceBundle.getBundle("MyResources",
			Locale.getDefault());
	String iconApplication = "/keyspair.png";
	String iconApplicationsmall = "/keyspairsmall.png";
	String iconLoading = "/loading.gif";
 
	String iconFlagItaly = "/flag_italy.gif";
	String iconFlagUk = "/flag_uk.gif";
	String iconFlagChina = "/flag_china.gif";

	String iconOk = "/ok.png";
	String iconKo = "/ko.png";



	private byte[] privateKeyBytes = null;
	private byte[] halfkey1 = null;
	private byte[] halfkey2 = null;

	private PrivateKey privateKey = null;
	
	private boolean statusPrivateKey1 = false;
	private boolean statusPrivateKey2 = false;

	
	private JPanel panelTitle;
	private JLabel labelTitle;
	private JPanel panelAction;
	private JLabel labelIcon;
	private JButton buttonClose;
	private JPanel panelButtons;
	private JFrame parent = null;
	private JMenu menuAbout;
	private JMenu menuFile;
	private JMenu menuLanguage;
	private JMenuBar menuBar;
	private JTextPane jTextResults;
	private JScrollPane jScrollPane0;

	javax.swing.JMenuItem menuVersion = null;
	javax.swing.JMenuItem menuTest = null;
	javax.swing.JMenuItem menuLog = null;

	javax.swing.JMenuItem menuItalian = null;
	javax.swing.JMenuItem menuEnglish = null;
	javax.swing.JMenuItem menuChinese = null;
	private JProgressBar progressBar;

	private boolean encryptMode = true;
	private boolean testMode = false;



	// START COMPONENT FOR TEST
	private JPanel panelTestChoice;
	
	private JLabel labelPrivateKey1;
	private JTextField textPrivateKey1;
	private JLabel labelStatusPrivateKey1;
	private JButton buttonPrivateKey1;
	private JLabel labelPrivateKey2;
	private JTextField textPrivateKey2;
	private JLabel labelStatusPrivateKey2;
	private JButton buttonPrivateKey2;

	private int panelSelected=0;
	// PANEL 1
	private JPanel panelTest1;
	
	private JLabel labelFileCsv;
	private JTextField textFileCsv;
	private JButton buttonFileCsv;
	private JLabel labelStatusFileCsv;
	private boolean statusFileCsv=false;

	private JTextField textFileBin;
	private JLabel labelFileBin;
	private JButton buttonFileBin;
	private JLabel labelStatusFileBin;
	private boolean statusFileBin=false;

	// PANEL 2
	private JPanel panelTest2;
	
	private JLabel labelFileCsv1;
	private JTextField textFileCsv1;
	private JButton buttonFileCsv1;
	private JLabel labelStatusFileCsv1;
	private boolean statusFileCsv1=false;

	private JTextField textFileXml;
	private JLabel labelFileXml;
	private JButton buttonFileXml;
	private JLabel labelStatusFileXml;
	private boolean statusFileXml=false;

	// PANEL 3
	private JPanel panelTest3;

	private JTextField textFileXml1;
	private JLabel labelFileXml1;
	private JButton buttonFileXml1;
	private JLabel labelStatusFileXml1;
	private boolean statusFileXml1=false;

	private JLabel labelFileBin1;
	private JTextField textFileBin1;
	private JButton buttonFileBin1;
	private JLabel labelStatusFileBin1;
	private boolean statusFileBin1=false;


	
	// START COMPONENT FOR LOG
	private JPanel panelTestResults;
	/*
	private JTextField textFileInT;
	private JTextField textFileOutT;
	private JLabel labelStatusFileInT;
	private JLabel labelStatusFileOutT;
	private JButton buttonFileInT;
	private JButton buttonFileOutT;
	private JLabel labelFileInT;
	private JLabel labelFileOutT;
	*/
	// END COMPONENT FOR TEST

	private JButton buttonExecute;
	private JLabel labelWait;
	private JLabel labelProgress;
	
	private JTabbedPane jTabbedPane;


	public MainWindow() {
		initComponents();
	}

	private void initComponents() {
		setResizable(false);
		setLayout(new GroupLayout());
		add(getProgressBar(), new Constraints(new Bilateral(12, 12, 10), new Leading(214, 12, 12)));
		add(getPanelTitle(), new Constraints(new Leading(12, 655, 12, 12), new Leading(7, 41, 10, 10)));
		add(getPanelButtons(), new Constraints(new Leading(12, 654, 12, 12), new Leading(246, 65, 12, 12)));
		add(getPanelAction(), new Constraints(new Leading(12, 654, 28, 166), new Leading(51, 12, 12)));
		setJMenuBar(getJMenuBar());
		setSize(694, 350);
	}


	// START COMPONENT FOR TEST

	
	private JTabbedPane getJTabbedPane() {
		if (jTabbedPane == null) {
			jTabbedPane = new JTabbedPane();

			jTabbedPane.addTab( rb.getString("title.test")+" 1",getPanelTest1());
			jTabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
			
			jTabbedPane.addTab(rb.getString("title.test")+" 2",getPanelTest2());
			jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
			
			jTabbedPane.addTab(rb.getString("title.test")+" 3",getPanelTest3());
			jTabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
			
			jTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
                        if (e.getSource() instanceof JTabbedPane) {
                            int index=jTabbedPane.getSelectedIndex();
                            JTabbedPane pane = (JTabbedPane) e.getSource();
                            System.out.println("Selected paneNo : " + index);
                            panelSelected=index;
                            
                        }

					
					
					//int index=jTabbedPane.getSelectedIndex();
		            //System.out.println("Tab: " + index);
		            //jTabbedPane.removeTabAt(index);
		            //jTabbedPane.insertTab("Test "+(index+1),null,getPanelTestCmd(),"",index);
		            
				}
		    });
			
			/*
			jTabbedPane.addTab("Test 2",getPanelEncrypt());
			jTabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
			jTabbedPane.addTab("Test 3",getPanelEncrypt());
			jTabbedPane.setMnemonicAt(1, KeyEvent.VK_3);
            */
		}
		return jTabbedPane;
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

	private JButton getbuttonPrivateKey1() {
		if (buttonPrivateKey1 == null) {
			buttonPrivateKey1 = new JButton();
			buttonPrivateKey1.setText("....");
			buttonPrivateKey1
			.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(
						java.awt.event.ActionEvent evt) {
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
								String path = file.getAbsolutePath()
										.toLowerCase();
								return path.toLowerCase().endsWith(
										".key");
							}
						}
					});
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						if (file != null) {
							if (file.exists()
									&& file.getAbsolutePath()
											.toLowerCase()
											.endsWith(".key")
									&& file.isFile()) {
								textPrivateKey1.setText(file
										.getAbsolutePath());
								String filePK1 = textPrivateKey1
										.getText();
								String filePK2 = textPrivateKey2
										.getText();
								if (!"".equals(filePK1)
										&& !"".equals(filePK2)) {
									FileInputStream fis1 = null;
									FileInputStream fis2 = null;
									try {
										fis1 = new FileInputStream(
												new File(filePK1));
										fis2 = new FileInputStream(
												new File(filePK2));
										halfkey1 = new byte[(int) new File(
												filePK1).length()];
										halfkey2 = new byte[(int) new File(
												filePK2).length()];
										fis1.read(halfkey1);
										fis2.read(halfkey2);
										byte[] b1 = Base64
												.decode(halfkey1);
										byte[] b2 = Base64
												.decode(halfkey2);

										privateKeyBytes = new byte[halfkey1.length
												+ halfkey2.length];
										for (int i = 0; i < b1.length; i++) {
											privateKeyBytes[i] = b1[i];
										}
										for (int i = 0; i < b2.length; i++) {
											privateKeyBytes[i
													+ b1.length] = b2[i];
										}
										privateKey = KeyFactory
												.getInstance("RSA")
												.generatePrivate(
														new PKCS8EncodedKeySpec(
																privateKeyBytes));
										fis1.close();
										fis2.close();
										fis1 = null;
										fis2 = null;
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconOk)));
										labelStatusPrivateKey2
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconOk)));
									} catch (Exception e) {
										e.printStackTrace();
										privateKey = null;
										privateKeyBytes = null;
										textPrivateKey1.setText("");
										textPrivateKey2.setText("");
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconKo)));
										labelStatusPrivateKey2
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconKo)));
										JOptionPane.showMessageDialog(
												parent,
												rb.getString("msg.invalidprivatekey"),
												rb.getString("title.error"),
												JOptionPane.ERROR_MESSAGE);
									} finally {
										try {
											if (fis1 != null)
												fis1.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
										try {
											if (fis2 != null)
												fis2.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									statusPrivateKey1 = (privateKey != null);
									statusPrivateKey2 = statusPrivateKey1;
									

								} else {
									statusPrivateKey1 = false;
									statusPrivateKey2 = false;
									labelStatusPrivateKey1
											.setIcon(null);
									labelStatusPrivateKey2
											.setIcon(null);
								}

								
							} else {
								labelStatusPrivateKey1
										.setIcon(new javax.swing.ImageIcon(
												getClass().getResource(
														iconKo)));
								textPrivateKey1.setText("");
								statusPrivateKey1 = false;
							}
						}

					}
					repaint();
				}
			}));

		}
		return buttonPrivateKey1;
	}

	private JLabel getlabelStatusPrivateKey1() {
		if (labelStatusPrivateKey1 == null) {
			labelStatusPrivateKey1 = new JLabel();
			labelStatusPrivateKey1.setIcon(new ImageIcon(getClass().getResource(
					"/ko.png")));
		}
		return labelStatusPrivateKey1;
	}

	
	private JLabel getlabelPrivateKey2() {
		if (labelPrivateKey2 == null) {
			labelPrivateKey2 = new JLabel();
			labelPrivateKey2.setText(rb.getString("title.privatekey2"));
		}
		return labelPrivateKey2;
	}


	private JTextField gettextPrivateKey2() {
		if (textPrivateKey2 == null) {
			textPrivateKey2 = new JTextField();
			textPrivateKey2.setEditable(false);
			textPrivateKey2.setText("");
		}
		return textPrivateKey2;
	}

	private JButton getbuttonPrivateKey2() {
		if (buttonPrivateKey2 == null) {
			buttonPrivateKey2 = new JButton();
			buttonPrivateKey2.setText("....");
			buttonPrivateKey2
			.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(
						java.awt.event.ActionEvent evt) {
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
								String path = file.getAbsolutePath()
										.toLowerCase();
								return path.toLowerCase().endsWith(
										".key");
							}
						}
					});
					if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
						File file = fileChooser.getSelectedFile();
						if (file != null) {
							if (file.exists()
									&& file.getAbsolutePath()
											.toLowerCase()
											.endsWith(".key")
									&& file.isFile()) {
								textPrivateKey2.setText(file
										.getAbsolutePath());
								String filePK1 = textPrivateKey1
										.getText();
								String filePK2 = textPrivateKey2
										.getText();
								if (!"".equals(filePK1)
										&& !"".equals(filePK2)) {
									FileInputStream fis1 = null;
									FileInputStream fis2 = null;
									try {
										fis1 = new FileInputStream(
												new File(filePK1));
										fis2 = new FileInputStream(
												new File(filePK2));
										halfkey1 = new byte[(int) new File(
												filePK1).length()];
										halfkey2 = new byte[(int) new File(
												filePK2).length()];
										fis1.read(halfkey1);
										fis2.read(halfkey2);
										byte[] b1 = Base64
												.decode(halfkey1);
										byte[] b2 = Base64
												.decode(halfkey2);

										privateKeyBytes = new byte[halfkey1.length
												+ halfkey2.length];
										for (int i = 0; i < b1.length; i++) {
											privateKeyBytes[i] = b1[i];
										}
										for (int i = 0; i < b2.length; i++) {
											privateKeyBytes[i
													+ b1.length] = b2[i];
										}

										privateKey = KeyFactory
												.getInstance("RSA")
												.generatePrivate(
														new PKCS8EncodedKeySpec(
																privateKeyBytes));
										fis1.close();
										fis2.close();
										fis1 = null;
										fis2 = null;
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconOk)));
										labelStatusPrivateKey2
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconOk)));
									} catch (Exception e) {
										e.printStackTrace();
										privateKey = null;
										privateKeyBytes = null;
										textPrivateKey1.setText("");
										textPrivateKey2.setText("");
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconKo)));
										labelStatusPrivateKey2
												.setIcon(new javax.swing.ImageIcon(
														getClass()
																.getResource(
																		iconKo)));
										JOptionPane.showMessageDialog(
												parent,
												rb.getString("msg.invalidprivatekey"),
												rb.getString("title.error"),
												JOptionPane.ERROR_MESSAGE);
									} finally {
										try {
											if (fis1 != null)
												fis1.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
										try {
											if (fis2 != null)
												fis2.close();
										} catch (Exception e) {
											e.printStackTrace();
										}
									}

									statusPrivateKey1 = (privateKey != null);
									statusPrivateKey2 = statusPrivateKey1;
									

								} else {
									statusPrivateKey1 = false;
									statusPrivateKey2 = false;
									labelStatusPrivateKey1
											.setIcon(null);
									labelStatusPrivateKey2
											.setIcon(null);
								}
							} else {
								labelStatusPrivateKey1
										.setIcon(new javax.swing.ImageIcon(
												getClass().getResource(
														iconKo)));
								textPrivateKey1.setText("");
								statusPrivateKey1 = false;

							}
						}

					}
					repaint();
				}
			}));
		}
		return buttonPrivateKey2;
	}

	private JLabel getlabelStatusPrivateKey2() {
		if (labelStatusPrivateKey2 == null) {
			labelStatusPrivateKey2 = new JLabel();
			labelStatusPrivateKey2.setIcon(new ImageIcon(getClass().getResource("/ko.png")));
			labelStatusPrivateKey2.setText("");
		}
		return labelStatusPrivateKey2;
	}


	
	// PANEL 1
	
	private JPanel getPanelTest1() {
		if (panelTest1 == null) {
			panelTest1 = new JPanel();
			panelTest1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTest1.setLayout(new GroupLayout());
			panelTest1.add(getLabelFileCsv(), new Constraints(new Leading(4, 111, 12, 12), new Leading(12, 12, 12)));
			panelTest1.add(getTextFileCsv(), new Constraints(new Leading(121, 319, 10, 10), new Leading(8, 24, 12, 12)));
			panelTest1.add(getButtonFileCsv(), new Constraints(new Leading(443, 10, 10), new Leading(5, 12, 12)));
			panelTest1.add(getLabelStatusFileCsv(), new Constraints(new Leading(503, 10, 10), new Leading(8, 12, 12)));
			panelTest1.add(getLabelFileBin(), new Constraints(new Leading(4, 111, 12, 12), new Leading(37, 17, 12, 12)));
			panelTest1.add(getTextFileBin(), new Constraints(new Leading(121, 319, 10, 10), new Leading(34, 24, 12, 12)));
			panelTest1.add(getButtonFileBin(), new Constraints(new Leading(443, 10, 10), new Leading(34, 12, 12)));
			panelTest1.add(getLabelStatusFileBin(), new Constraints(new Leading(503, 10, 10), new Leading(34, 12, 12)));
		}
		return panelTest1;
	}

	
	
	private JLabel getLabelFileCsv() {
		if (labelFileCsv == null) {
			labelFileCsv = new JLabel();
			labelFileCsv.setText(rb.getString("title.filecsv"));
			//labelFileIn.setFont(new java.awt.Font("Arial Narrow",Font.BOLD,12));
		}
		return labelFileCsv;
	}

	private JTextField getTextFileCsv() {
		if (textFileCsv == null) {
			textFileCsv = new JTextField();
			textFileCsv.setEditable(false);
		}
		return textFileCsv;
	}

	private JButton getButtonFileCsv() {
		if (buttonFileCsv == null) {
			buttonFileCsv = new JButton();
			buttonFileCsv.setText("....");
			buttonFileCsv
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return (f.getName()
														.toLowerCase()
														.endsWith(".csv")||
														f.getName()
														.toLowerCase()
														.endsWith(".txt"));
											}
										}

										@Override
										public String getDescription() {
											return "csv txt files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&(file.getName().toLowerCase().endsWith(".csv")||file.getName().toLowerCase().endsWith(".txt"))) {
										labelStatusFileCsv
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileCsv.setText(file
												.getAbsolutePath());
										statusFileCsv=true;

									} else {
										labelStatusFileCsv
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileCsv.setText("");
										statusFileCsv=false;
									}
								}

							}
							repaint();
						}
					}));

		}
		return buttonFileCsv;
	}

	private JLabel getLabelStatusFileCsv() {
		if (labelStatusFileCsv == null) {
			labelStatusFileCsv = new JLabel();
			labelStatusFileCsv.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource(iconKo)));
		}
		return labelStatusFileCsv;
	}
	
	
	
	
	
	
	
	
	
	
	
	private JLabel getLabelFileBin() {
		if (labelFileBin == null) {
			labelFileBin = new JLabel();
			labelFileBin.setText(rb.getString("title.filebin"));
			//labelFileOut.setFont(new java.awt.Font("Arial",Font.BOLD,12));
		}
		return labelFileBin;
	}

	private JTextField getTextFileBin() {
		if (textFileBin == null) {
			textFileBin = new JTextField();
			textFileBin.setEditable(false);
		}
		return textFileBin;
	}

	private JButton getButtonFileBin() {
		if (buttonFileBin == null) {
			buttonFileBin = new JButton();
			buttonFileBin.setText("....");
			buttonFileBin
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							//fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return f.getName()
														.toLowerCase()
														.endsWith(".bin");
											}
										}

										@Override
										public String getDescription() {
											return "bin Files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&file.getName().toLowerCase().endsWith(".bin")) {
										labelStatusFileBin
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileBin.setText(file
												.getAbsolutePath());
										statusFileBin=true;
										
									} else {
										labelStatusFileBin
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileBin.setText("");
										statusFileBin=false;
									}
								}

							}
							repaint();

						}
					}));
		}
		return buttonFileBin;
	}

	private JLabel getLabelStatusFileBin() {
		if (labelStatusFileBin == null) {
			labelStatusFileBin = new JLabel();
			labelStatusFileBin.setIcon(new ImageIcon(getClass().getResource(
					"/ko.png")));
		}
		return labelStatusFileBin;
	}
	
	
	
	
	// PANEL 2
	private JPanel getPanelTest2() {
		if (panelTest2 == null) {
			panelTest2 = new JPanel();
			panelTest2.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTest2.setLayout(new GroupLayout());
			panelTest2.add(getLabelFileCsv1(), new Constraints(new Leading(4, 111, 12, 12), new Leading(12, 12, 12)));
			panelTest2.add(getTextFileCsv1(), new Constraints(new Leading(121, 319, 10, 10), new Leading(8, 24, 12, 12)));
			panelTest2.add(getButtonFileCsv1(), new Constraints(new Leading(443, 10, 10), new Leading(5, 12, 12)));
			panelTest2.add(getLabelStatusFileCsv1(), new Constraints(new Leading(503, 10, 10), new Leading(8, 12, 12)));
			panelTest2.add(getLabelFileXml(), new Constraints(new Leading(4, 111, 12, 12), new Leading(37, 17, 12, 12)));
			panelTest2.add(getTextFileXml(), new Constraints(new Leading(121, 319, 10, 10), new Leading(34, 24, 12, 12)));
			panelTest2.add(getButtonFileXml(), new Constraints(new Leading(443, 10, 10), new Leading(34, 12, 12)));
			panelTest2.add(getLabelStatusFileXml(), new Constraints(new Leading(503, 10, 10), new Leading(34, 12, 12)));
		}
		return panelTest2;
	}
	
	private JLabel getLabelFileCsv1() {
		if (labelFileCsv1 == null) {
			labelFileCsv1 = new JLabel();
			labelFileCsv1.setText(rb.getString("title.filecsv"));
			//labelFileIn.setFont(new java.awt.Font("Arial Narrow",Font.BOLD,12));
		}
		return labelFileCsv1;
	}

	private JTextField getTextFileCsv1() {
		if (textFileCsv1 == null) {
			textFileCsv1 = new JTextField();
			textFileCsv1.setEditable(false);
		}
		return textFileCsv1;
	}

	private JButton getButtonFileCsv1() {
		if (buttonFileCsv1 == null) {
			buttonFileCsv1 = new JButton();
			buttonFileCsv1.setText("....");
			buttonFileCsv1
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (!f.isDirectory()) {
												return true;
											} else {
												return f.getName()
														.toLowerCase()
														.endsWith(".csv");
											}
										}

										@Override
										public String getDescription() {
											return "csv txt files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&(file.getName().toLowerCase().endsWith(".csv")||file.getName().toLowerCase().endsWith(".txt"))) {
										labelStatusFileCsv1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileCsv1.setText(file
												.getAbsolutePath());
										statusFileCsv1=true;

									} else {
										labelStatusFileCsv1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileCsv1.setText("");
										statusFileCsv1=false;
									}
								}

							}
							repaint();
						}
					}));

		}
		return buttonFileCsv1;
	}

	private JLabel getLabelStatusFileCsv1() {
		if (labelStatusFileCsv1 == null) {
			labelStatusFileCsv1 = new JLabel();
			labelStatusFileCsv1.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource(iconKo)));
		}
		return labelStatusFileCsv1;
	}
	
	
	private JLabel getLabelFileXml() {
		if (labelFileXml == null) {
			labelFileXml = new JLabel();
			labelFileXml.setText(rb.getString("title.filexml"));
			//labelFileOut.setFont(new java.awt.Font("Arial",Font.BOLD,12));
		}
		return labelFileXml;
	}

	private JTextField getTextFileXml() {
		if (textFileXml == null) {
			textFileXml = new JTextField();
			textFileXml.setEditable(false);
		}
		return textFileXml;
	}

	private JButton getButtonFileXml() {
		if (buttonFileXml == null) {
			buttonFileXml = new JButton();
			buttonFileXml.setText("....");
			buttonFileXml
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							//fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return f.getName()
														.toLowerCase()
														.endsWith(".xml");
											}
										}

										@Override
										public String getDescription() {
											return "xml files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&file.getName().toLowerCase().endsWith(".xml")) {
										labelStatusFileXml
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileXml.setText(file
												.getAbsolutePath());
										statusFileXml=true;
									} else {
										labelStatusFileXml
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileXml.setText("");
										statusFileXml=false;
									}
								}

							}
							repaint();

						}
					}));
		}
		return buttonFileXml;
	}

	private JLabel getLabelStatusFileXml() {
		if (labelStatusFileXml == null) {
			labelStatusFileXml = new JLabel();
			labelStatusFileXml.setIcon(new ImageIcon(getClass().getResource(
					"/ko.png")));
		}
		return labelStatusFileXml;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// PANEL 3
	private JPanel getPanelTest3() {
		if (panelTest3 == null) {
			panelTest3 = new JPanel();
			panelTest3.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTest3.setLayout(new GroupLayout());
			panelTest3.add(getLabelFileXml1(), new Constraints(new Leading(4, 111, 12, 12), new Leading(12, 12, 12)));
			panelTest3.add(getTextFileXml1(), new Constraints(new Leading(121, 319, 10, 10), new Leading(8, 24, 12, 12)));
			panelTest3.add(getButtonFileXml1(), new Constraints(new Leading(443, 10, 10), new Leading(5, 12, 12)));
			panelTest3.add(getLabelStatusFileXml1(), new Constraints(new Leading(503, 10, 10), new Leading(8, 12, 12)));
			panelTest3.add(getLabelFileBin1(), new Constraints(new Leading(4, 111, 12, 12), new Leading(37, 17, 12, 12)));
			panelTest3.add(getTextFileBin1(), new Constraints(new Leading(121, 319, 10, 10), new Leading(34, 24, 12, 12)));
			panelTest3.add(getButtonFileBin1(), new Constraints(new Leading(443, 10, 10), new Leading(34, 12, 12)));
			panelTest3.add(getLabelStatusFileBin1(), new Constraints(new Leading(503, 10, 10), new Leading(34, 12, 12)));
		}
		return panelTest3;
	}

	
	
	private JLabel getLabelFileXml1() {
		if (labelFileXml1 == null) {
			labelFileXml1 = new JLabel();
			labelFileXml1.setText(rb.getString("title.filexml"));
			//labelFileOut.setFont(new java.awt.Font("Arial",Font.BOLD,12));
		}
		return labelFileXml1;
	}

	private JTextField getTextFileXml1() {
		if (textFileXml1 == null) {
			textFileXml1 = new JTextField();
			textFileXml1.setEditable(false);
		}
		return textFileXml1;
	}

	private JButton getButtonFileXml1() {
		if (buttonFileXml1 == null) {
			buttonFileXml1 = new JButton();
			buttonFileXml1.setText("....");
			buttonFileXml1
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							//fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return f.getName()
														.toLowerCase()
														.endsWith(".xml");
											}
										}

										@Override
										public String getDescription() {
											return "xml files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&file.getName().toLowerCase().endsWith(".xml")) {
										labelStatusFileXml1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileXml1.setText(file
												.getAbsolutePath());
										statusFileXml1=true;
									} else {
										labelStatusFileXml1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileXml1.setText("");
										statusFileXml1=false;
									}
								}

							}
							repaint();

						}
					}));
		}
		return buttonFileXml1;
	}

	private JLabel getLabelStatusFileXml1() {
		if (labelStatusFileXml1 == null) {
			labelStatusFileXml1 = new JLabel();
			labelStatusFileXml1.setIcon(new ImageIcon(getClass().getResource(
					"/ko.png")));
		}
		return labelStatusFileXml1;
	}
	
	
	private JLabel getLabelFileBin1() {
		if (labelFileBin1 == null) {
			labelFileBin1 = new JLabel();
			labelFileBin1.setText(rb.getString("title.filebin"));
			//labelFileIn.setFont(new java.awt.Font("Arial Narrow",Font.BOLD,12));
		}
		return labelFileBin1;
	}

	private JTextField getTextFileBin1() {
		if (textFileBin1 == null) {
			textFileBin1 = new JTextField();
			textFileBin1.setEditable(false);
		}
		return textFileBin1;
	}

	private JButton getButtonFileBin1() {
		if (buttonFileBin1 == null) {
			buttonFileBin1 = new JButton();
			buttonFileBin1.setText("....");
			buttonFileBin1
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (!f.isDirectory()) {
												return true;
											} else {
												return f.getName()
														.toLowerCase()
														.endsWith(".bin");
											}
										}

										@Override
										public String getDescription() {
											return "bin files";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()&&file.getName().toLowerCase().endsWith(".bin")) {
										labelStatusFileBin1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileBin1.setText(file
												.getAbsolutePath());
										statusFileBin1=true;

									} else {
										labelStatusFileBin1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileBin1.setText("");
										statusFileBin1=false;
									}
								}

							}
							repaint();
						}
					}));

		}
		return buttonFileBin1;
	}

	private JLabel getLabelStatusFileBin1() {
		if (labelStatusFileBin1 == null) {
			labelStatusFileBin1 = new JLabel();
			labelStatusFileBin1.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource(iconKo)));

		}
		return labelStatusFileBin1;
	}
	
	

	
	
	
	
	
	
	
	
	// START COMPONENT FOR LOG
	
	private JScrollPane getJScrollPane0() {
		if (jScrollPane0 == null) {
			jScrollPane0 = new JScrollPane();
			jScrollPane0.setViewportView(getTextResults());
		}
		return jScrollPane0;
	}

	private JTextPane getTextResults() {
		if (jTextResults == null) {
			jTextResults = new JTextPane();
			jTextResults.setText("");
			jTextResults.setEditable(false);
		}
		return jTextResults;
	}

	private JPanel getPanelTestResults() {
		if (panelTestResults == null) {
			panelTestResults = new JPanel();
			panelTestResults.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTestResults.setLayout(new GroupLayout());
			panelTestResults.add(getJScrollPane0(), new Constraints(new Leading(5, 542, 10, 10), new Leading(8, 151, 10, 10)));
		}
		return panelTestResults;
	}

	private JPanel getPanelTestChoice() {
		if (panelTestChoice == null) {
			panelTestChoice = new JPanel();
			panelTestChoice.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTestChoice.setLayout(new GroupLayout());
			panelTestChoice.add(getJTabbedPane(), new Constraints(new Leading(8, 555, 12, 12), new Leading(0, 108, 12, 12)));
			panelTestChoice.add(getlabelPrivateKey2(), new Constraints(new Leading(14, 116, 12, 12), new Leading(142, 12, 12)));
			panelTestChoice.add(getbuttonPrivateKey2(), new Constraints(new Leading(454, 10, 10), new Leading(140, 24, 10, 10)));
			panelTestChoice.add(getTextPrivateKey1(), new Constraints(new Leading(134, 317, 10, 10), new Leading(114, 24, 44, 46)));
			panelTestChoice.add(getLabelPrivateKey1(), new Constraints(new Leading(14, 116, 10, 10), new Leading(120, 12, 12)));
			panelTestChoice.add(gettextPrivateKey2(), new Constraints(new Leading(134, 316, 12, 12), new Leading(141, 22, 12, 12)));
			panelTestChoice.add(getlabelStatusPrivateKey2(), new Constraints(new Leading(516, 12, 12), new Leading(140, 12, 12)));
			panelTestChoice.add(getlabelStatusPrivateKey1(), new Constraints(new Leading(516, 12, 12), new Leading(114, 12, 12)));
			panelTestChoice.add(getbuttonPrivateKey1(), new Constraints(new Leading(454, 10, 10), new Leading(112, 24, 10, 10)));
		}
		return panelTestChoice;
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
			menuFile.add(getMenuTest());
			menuFile.add(getMenuLog());
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
			menuItalian = new javax.swing.JMenuItem("Italiano", new ImageIcon(
					this.getClass().getResource(iconFlagItaly)));
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
			menuEnglish = new javax.swing.JMenuItem("English", new ImageIcon(
					this.getClass().getResource(iconFlagUk)));
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
			menuChinese = new javax.swing.JMenuItem("中国的", new ImageIcon(this
					.getClass().getResource(iconFlagChina)));
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

	public void reloadLocale() {
		rb = ResourceBundle.getBundle("MyResources", Locale.getDefault());

		menuFile.setText(rb.getString("title.file"));
		menuLanguage.setText(rb.getString("title.language"));
		menuTest.setText(rb.getString("title.test"));
		menuLog.setText(rb.getString("title.log"));
		menuAbout.setText(rb.getString("title.about"));
		menuVersion.setText(rb.getString("title.version"));
		buttonClose.setText(rb.getString("button.close"));
		labelTitle.setText(rb.getString("title"));

		labelPrivateKey1.setText(rb.getString("title.privatekey1"));
		labelPrivateKey2.setText(rb.getString("title.privatekey2"));

		jTabbedPane.setTabComponentAt(0, new JLabel(rb.getString("title.test")+" 1"));
		jTabbedPane.setTabComponentAt(1, new JLabel(rb.getString("title.test")+" 2"));
		jTabbedPane.setTabComponentAt(2, new JLabel(rb.getString("title.test")+" 3"));

		
		labelFileCsv.setText(rb.getString("title.filecsv"));
		labelFileCsv1.setText(rb.getString("title.filecsv"));
		
		labelFileXml.setText(rb.getString("title.filexml"));
		labelFileXml1.setText(rb.getString("title.filexml"));
		
		labelFileBin.setText(rb.getString("title.filebin"));
		labelFileBin1.setText(rb.getString("title.filebin"));
		
		
		parent.setTitle(rb.getString("title") + " - ["
					+ rb.getString("title.test") + "]");
		buttonExecute.setText(rb.getString("title.test"));
		buttonExecute.setMnemonic('T');

		repaint();
	}

	private JMenuItem getMenuTest() {
		if (menuTest == null) {
			menuTest = new javax.swing.JMenuItem(
					rb.getString("title.encrypt"));
			menuTest.setMnemonic('C');
			menuTest.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					clearPane(jTextResults);
					encryptMode = true;
					testMode = false;
					parent.setTitle(rb.getString("title") + " - ["
							+ rb.getString("title.encrypt") + "]");
					panelTestChoice.setVisible(true);
					panelTestResults.setVisible(false);
					buttonExecute.setText(rb.getString("title.test"));
					buttonExecute.setMnemonic('T');
					buttonExecute.setEnabled(true);
					textPrivateKey1.setText("");
					labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));
					textFileCsv.setText("");
					labelStatusFileCsv.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));

				}
			}));
		}
		return menuTest;
	}


	private JMenuItem getMenuLog() {
		if (menuLog == null) {
			menuLog = new javax.swing.JMenuItem(rb.getString("title.test"));
			menuLog.setMnemonic('T');
			menuLog.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					clearPane(jTextResults);
					testMode = true;
					encryptMode = false;
					parent.setTitle(rb.getString("title") + " - ["
							+ rb.getString("title.test") + "]");
					panelTestChoice.setVisible(false);
					panelTestResults.setVisible(true);
					buttonExecute.setText(rb.getString("title.test"));
					buttonExecute.setMnemonic('T');
					buttonExecute.setEnabled(true);
					//textFileInT.setText("");
					//textFileOutT.setText("");
					//labelStatusFileInT.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconKo)));


				}
			}));
		}
		return menuLog;
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
		if (menuVersion == null) {
			menuVersion = new javax.swing.JMenuItem(
					rb.getString("title.version"));
			menuVersion.setMnemonic('V');
			menuVersion.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					AboutWindow frame = new AboutWindow(parent, true);
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
			panelButtons.add(getLabelWait(), new Constraints(new Leading(266, 10, 10), new Leading(26, 12, 12)));
			panelButtons.add(getLabelProgress(), new Constraints(new Leading(218, 10, 10), new Leading(3, 10, 10)));
			panelButtons.add(getButtonExecute(), new Constraints(new Leading(97, 115, 12, 12), new Leading(5, 53, 12, 12)));
			panelButtons.add(getButtonClose(), new Constraints(new Leading(407, 115, 10, 10), new Leading(5, 53, 12, 12)));
		}
		return panelButtons;
	}

	private JButton getButtonExecute() {
		if (buttonExecute == null) {
			buttonExecute = new JButton();
			buttonExecute.setFont(new Font("Serif", Font.BOLD, 16));
			buttonExecute.setText(rb.getString("title.test"));
			buttonExecute.setMnemonic('T');
			buttonExecute.setEnabled(false);
			buttonExecute
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {

							new Thread() {
								@Override
								public void run() {

									buttonExecute.setEnabled(true);									

								}
							}.start();
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
			labelWait.setIcon(new javax.swing.ImageIcon(getClass().getResource(
					iconLoading)));
			labelWait.setVisible(false);
		}
		return labelWait;
	}

	private JLabel getLabelIcon() {
		if (labelIcon == null) {
			labelIcon = new JLabel();
			// jLabel0.setText("jLabel0");
			labelIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource(
					iconApplicationsmall)));
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
			panelAction.add(getPanelTestChoice(), new Constraints(new Leading(64, 571, 10, 10), new Leading(5, 183, 10, 10)));
			panelAction.add(getPanelTestResults(), new Constraints(new Leading(67, 568, 12, 12), new Leading(5, 172, 12, 12)));
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
			panelTitle.add(getLabelTitle(), new Constraints(new Bilateral(12, 12, 0), new Leading(7, 23, 10, 10)));
		}
		return panelTitle;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
			//UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial Narrow", Font.PLAIN, 4));
			
			
			
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	public void setIcon() {
		setTitle(rb.getString("title") + " - [" + rb.getString("title.encrypt")
				+ "]");
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
		parent = this;
	}

	private void appendToPane(JTextPane tp, String msg, Color c) {
		if (tp != null) {
			StyleContext sc = StyleContext.getDefaultStyleContext();
			AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
					StyleConstants.Foreground, c);

			aset = sc.addAttribute(aset, StyleConstants.FontFamily,
					"Lucida Console");
			aset = sc.addAttribute(aset, StyleConstants.Alignment,
					StyleConstants.ALIGN_JUSTIFIED);

			int len = tp.getDocument().getLength();
			tp.setCaretPosition(len);
			tp.setCharacterAttributes(aset, false);
			tp.replaceSelection(msg);
			try {
				tp.getStyledDocument().insertString(len, msg, aset);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private void clearPane(JTextPane tp) {
		if (tp != null) {
			int len = tp.getDocument().getLength();
			try {
				tp.getStyledDocument().remove(0, len);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}





	private synchronized boolean decrypt(String srcFileName, String destFileName) {
		boolean retval = false;
		progressBar.setVisible(true);
		progressBar.setMaximum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		appendToPane(jTextResults, rb.getString("msg.deciphering") + "\n",	Color.BLUE);

		labelProgress.setText(rb.getString("msg.deciphering") + " 0%");
		labelProgress.setVisible(true);
		labelWait.setVisible(true);
		buttonClose.setEnabled(false);
		buttonExecute.setEnabled(false);
		menuFile.setEnabled(false);
		menuAbout.setEnabled(false);
		menuLanguage.setEnabled(false);
		repaint();

		OutputStream outputWriter = null;
		InputStream inputReader = null;

		byte[] arrayByte = new byte[0];
		byte[] arrayByte1 = new byte[0];
		try {

			outputWriter = new FileOutputStream(destFileName);
			inputReader = new FileInputStream(srcFileName);

			boolean loop = true;
			int b;
			while ((b = inputReader.read()) != -1 && loop) {
				arrayByte = appendByteArray(arrayByte, (byte) b);
				loop = !checkSeq(arrayByte);
				if (!loop)
					break;
			}

			int keylen = arrayByte.length - seq.length;
			byte[] aesKeyEncoded = new byte[keylen];
			for (int i = 0; i < aesKeyEncoded.length; i++) {
				aesKeyEncoded[i] = arrayByte[i];
			}

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] aesKey = cipher.doFinal(aesKeyEncoded);

			SecretKey secretAesKey = new SecretKeySpec(aesKey, 0,
					aesKey.length, "AES");

			loop = true;
			while ((b = inputReader.read()) != -1 && loop) {
				arrayByte1 = appendByteArray(arrayByte1, (byte) b);
				loop = !checkSeq(arrayByte1);
				if (!loop)
					break;
			}

			keylen = arrayByte1.length - seq.length;
			byte[] ivEncoded = new byte[keylen];
			for (int i = 0; i < aesKeyEncoded.length; i++) {
				ivEncoded[i] = arrayByte1[i];
			}

			byte[] iv = cipher.doFinal(ivEncoded);

			/*
			 * byte[] iv = new byte[16];
			 * 
			 * for(int i=0;i<16;i++){ iv[i]=arrayByte[i+keylen]; }
			 */

			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
			long fileLen = new File(srcFileName).length() - arrayByte.length
					- arrayByte1.length;
			decryptAES(secretAesKey, paramSpec, inputReader, outputWriter,
					fileLen);

			outputWriter.close();
			inputReader.close();
			// bos.close();
			outputWriter = null;
			inputReader = null;

			retval = true;
		} catch (Exception e) {
			appendToPane(jTextResults, e.getLocalizedMessage() + "\n",	Color.RED);
			e.printStackTrace();
		} finally {
			try {
				// if (bos != null)
				// bos.close();
				if (outputWriter != null)
					outputWriter.close();
				if (inputReader != null)
					inputReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			progressBar.setVisible(false);
			labelProgress.setVisible(false);
			labelWait.setVisible(false);
			buttonClose.setEnabled(true);
			buttonExecute.setEnabled(true);

			menuFile.setEnabled(true);
			menuAbout.setEnabled(true);
			menuLanguage.setEnabled(true);

		}
		return retval;
	}

	private synchronized boolean verifyResults(String fileXml) {
		boolean retval = false;
		// 50 MB
		if (new File(fileXml).length() < 52428800L) {
			retval=verifySmallXML(fileXml);
		} else {
			retval=verifyLargeXML(fileXml);
		}
		return retval;
	}

	private synchronized boolean verifySmallXML(String fileXml) {
		boolean retval = true;
		JAXBContext jc;
		java.io.InputStream is = null;
		try {
			int progressValue=0;
			int prevProgressValue=0;
			progressBar.setVisible(true);
			progressBar.setMaximum(0);
			progressBar.setMaximum(100);
			progressBar.setValue(0);
			labelProgress.setVisible(true);
			appendToPane(jTextResults, rb.getString("msg.verification") + "\n",	Color.BLUE);

			labelProgress.setText(rb.getString("msg.verification") + " 0%");
            repaint();
			jc = JAXBContext.newInstance("ietf.params.xml.ns.keyprov.pskc");
			Unmarshaller u = jc.createUnmarshaller();
			is = new java.io.FileInputStream(fileXml);
			Object o = u.unmarshal(is);
			javax.xml.bind.JAXBElement<KeyContainerType> f = (javax.xml.bind.JAXBElement<KeyContainerType>) o;
			KeyContainerType keyContainer = (KeyContainerType) f.getValue();

			List<KeyPackageType> listPackages = keyContainer.getKeyPackage();
			if (listPackages != null && listPackages.size() > 0) {
				for (int i = 0; i < listPackages.size(); i++) {
					KeyPackageType KeyPackage = listPackages.get(i);
					DeviceInfoType deviceInfo = KeyPackage.getDeviceInfo();
					KeyType key = KeyPackage.getKey();
					KeyDataType KeyData = key.getData();
					BinaryDataType binaryDataType = KeyData.getSecret();
					EncryptedDataType encryptedDataType = binaryDataType
							.getEncryptedValue();

					String seed = encryptedDataType.getCipherData()
							.getCipherValue();
					String serial = deviceInfo.getSerialNo();

					Cipher cipher = Cipher.getInstance("RSA");
					cipher.init(Cipher.DECRYPT_MODE, privateKey);
					byte[] cipherData = cipher.doFinal(Base64.decode(seed));
					String decodedseed = new String(cipherData);

					String seedOrigin = mapSerialSeed.remove(serial);
					if (seedOrigin == null) {
						retval = false;
						appendToPane(
								jTextResults,
								"(1)[" + serial + "] "
										+ rb.getString("msg.testNotFound")
										+ "\n", Color.RED);
					} else {
						if (!seedOrigin.equals(decodedseed)) {
							retval = false;
							appendToPane(
									jTextResults,
									"["
											+ serial
											+ "] "
											+ rb.getString("msg.testSeedNotMatch")
											+ "\n", Color.RED);
						}
					}
					progressValue = (int) ((i * 100) / listPackages.size());
					if (progressValue != prevProgressValue) {
						progressBar.setValue(progressValue);
						labelProgress.setText(rb.getString("msg.verification")
								+ " " + progressValue + "%");
						prevProgressValue = progressValue;
						// System.out.println(prevProgressValue+"%");
						repaint();
					}

					// System.out.println(serial+"\n"+decodedseed+"\n\n\n");
				}
			}
			if (mapSerialSeed.size() > 0) {
				retval = false;
				Object[] object = mapSerialSeed.keySet().toArray();
				for (int i = 0; i < object.length; i++) {
					appendToPane(
							jTextResults,
							"(2)[" + object[i] + "] "
									+ rb.getString("msg.testNotFound") + "\n",
							Color.RED);
				}

			}
			// System.out.println("OK");

		} catch (Exception e) {
			e.printStackTrace();
			appendToPane(jTextResults, e.getLocalizedMessage() + "\n",	Color.RED);

			retval = false;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {

			}
		}
		progressBar.setVisible(false);
		labelProgress.setVisible(false);
		repaint();
		return retval;
	}

	private synchronized boolean verifyLargeXML(String fileXml) {
		boolean retval = true;
		JAXBContext jc;
		XMLStreamReader xsr = null;
		try {
			
			XMLInputFactory xif = XMLInputFactory.newFactory();
			StreamSource xml = new StreamSource(fileXml);
			xsr = xif.createXMLStreamReader(xml);
			xsr.nextTag();

			int progressValue=0;
			int prevProgressValue=0;
			progressBar.setVisible(true);
			progressBar.setMaximum(0);
			progressBar.setMaximum(100);
			progressBar.setValue(0);
			labelProgress.setVisible(true);
			appendToPane(jTextResults, rb.getString("msg.verification") + "\n",	Color.BLUE);
			labelProgress.setText(rb.getString("msg.verification") + " 0%");
            repaint();
            int counter=0;
            int len=mapSerialSeed.size();
			while (xsr.hasNext()) {
				if (xsr.isStartElement()) {
					if (xsr.getLocalName().equals("KeyPackage")) {

						jc = JAXBContext.newInstance(KeyPackageType.class);
						Unmarshaller unmarshaller = jc.createUnmarshaller();
						JAXBElement<KeyPackageType> jb = unmarshaller
								.unmarshal(xsr, KeyPackageType.class);
						xsr.close();
						KeyPackageType KeyPackage = jb.getValue();

						KeyType key = KeyPackage.getKey();
						KeyDataType KeyData = key.getData();
						BinaryDataType binaryDataType = KeyData.getSecret();
						EncryptedDataType encryptedDataType = binaryDataType
								.getEncryptedValue();
						String seed = encryptedDataType.getCipherData()
								.getCipherValue();

						DeviceInfoType deviceInfo = KeyPackage.getDeviceInfo();
						String serial = deviceInfo.getSerialNo();


						Cipher cipher = Cipher.getInstance("RSA");
						cipher.init(Cipher.DECRYPT_MODE, privateKey);
						byte[] cipherData = cipher.doFinal(Base64.decode(seed));
						String decodedseed = new String(cipherData);

						String seedOrigin = mapSerialSeed.remove(serial);
						if (seedOrigin == null) {
							retval = false;
							appendToPane(jTextResults, "(1)[" + serial + "] "
									+ rb.getString("msg.testNotFound") + "\n",
									Color.RED);
						} else {
							if (!seedOrigin.equals(decodedseed)) {
								retval = false;
								appendToPane(jTextResults, "[" + serial + "] "
										+ rb.getString("msg.testSeedNotMatch")
										+ "\n", Color.RED);
							}
						}
						progressValue = (int) ((counter * 100) / len);
						if (progressValue != prevProgressValue) {
							progressBar.setValue(progressValue);
							labelProgress.setText(rb.getString("msg.verification")
									+ " " + progressValue + "%");
							prevProgressValue = progressValue;
							// System.out.println(prevProgressValue+"%");
							repaint();
						}

						counter++;

					}
				}
				
				xsr.next();
			}
			if (mapSerialSeed.size() > 0) {
				retval = false;
				Object[] object = mapSerialSeed.keySet().toArray();
				for (int i = 0; i < object.length; i++) {
					appendToPane(
							jTextResults,
							"(2)[" + object[i] + "] "
									+ rb.getString("msg.testNotFound") + "\n",
							Color.RED);
				}

			}
			progressBar.setValue(100);
			labelProgress.setText(rb.getString("msg.verification")	+ " 100%");
			repaint();
		} catch (Exception e) {
			e.printStackTrace();
			appendToPane(jTextResults, e.getLocalizedMessage() + "\n",	Color.RED);
			retval = false;
		} finally {
			try {
				if (xsr != null)
					xsr.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		progressBar.setVisible(false);
		labelProgress.setVisible(false);
		repaint();
		return retval;
	}


	private synchronized void encryptAES(SecretKey key,
			AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out,
			long fileLen) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		CipherOutputStream cos = null;
		try {
			cos = new CipherOutputStream(out, c);
			int count = 0;
			byte[] buffer = new byte[1024];
			long counter = 0;
			int progressValue = 0;
			int prevProgressValue = 0;
			while ((count = in.read(buffer)) >= 0) {
				counter = counter + count;
				cos.write(buffer, 0, count);
				progressValue = (int) ((counter * 100) / fileLen);
				if (progressValue != prevProgressValue) {
					progressBar.setValue(progressValue);
					labelProgress.setText(rb.getString("msg.ciphering") + " "
							+ progressValue + "%");
					prevProgressValue = progressValue;
					repaint();
				}
			}
			cos.flush();
		} finally {
			if (cos != null)
				cos.close();
		}

	}

	private synchronized void decryptAES(SecretKey key,
			AlgorithmParameterSpec paramSpec, InputStream in, OutputStream out,
			long fileLen) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, key, paramSpec);
		CipherOutputStream cos = null;
		try {
			cos = new CipherOutputStream(out, c);
			int count = 0;
			byte[] buffer = new byte[1024];
			long counter = 0;
			int progressValue = 0;
			int prevProgressValue = 0;
			while ((count = in.read(buffer)) >= 0) {
				counter = counter + count;
				cos.write(buffer, 0, count);
				progressValue = (int) ((counter * 100) / fileLen);
				if (progressValue != prevProgressValue) {
					progressBar.setValue(progressValue);
					labelProgress.setText(rb.getString("msg.deciphering") + " "
							+ progressValue + "%");
					prevProgressValue = progressValue;
					repaint();
				}

			}
			cos.flush();
		} finally {
			if (cos != null)
				cos.close();
		}

	}



	private synchronized String[] getSerialKey(String string) throws Exception {
		// possibili separatori SPACE ;,:| TAB
		if (string.contains(";")) {
			return getSerialKey(string.split(";"));
		} else if (string.contains(",")) {
			return getSerialKey(string.split(","));
		} else if (string.contains(":")) {
			return getSerialKey(string.split(":"));
		} else if (string.contains("|")) {
			return getSerialKey(string.split("\\|"));
		} else if (string.contains("\t")) {
			return getSerialKey(string.split("\t"));
		} else if (string.contains(" ")) {
			return getSerialKey(string.split(" "));
		} else {
			return getSerialKey(string.split("\\W"));
		}
	}

	private synchronized String[] getSerialKey(String[] stringArray)
			throws Exception {

		Pattern hexdecimal = Pattern.compile("^[0-9A-F]+$");
		String serial = "";
		String key = "";
		String[] serialKey = null;
		if (stringArray.length > 0) {
			for (int i = 0; i < stringArray.length; i++) {
				String value = stringArray[i];
				if (value.length() > 20 && "".equals(key)) {
					key = value;
				}
				if (hexdecimal.matcher(value.toUpperCase()).matches()
						&& "".equals(serial) && value.length() <= 20) {
					serial = value;
				}
			}
			serialKey = new String[2];
			serialKey[0] = serial;
			serialKey[1] = key;
		}
		return serialKey;
	}


	private HashMap<String, String> mapSerialSeed = new HashMap<String, String>();

	/**
	 * Main entry of the class. Note: This class is only created so that you can
	 * easily preview the result at runtime. It is not expected to be managed by
	 * the designer. You can modify it as you like.
	 */
	public static void main(String[] args) {
		try {
			seq = END_OF_KEY.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static String END_OF_KEY = "=,uytnnnnnnnnnneereee,c------";
	public static byte[] seq = null;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public byte[] appendByteArray(byte[] array, byte b) {
		byte[] c = new byte[array.length + 1];
		for (int i = 0; i < array.length; i++) {
			c[i] = array[i];
		}
		c[array.length] = b;
		return c;
	}

	public boolean checkSeq(byte[] data) {
		boolean found = true;
		if (seq.length < data.length && data.length > 0) {
			int indexData = data.length - seq.length;
			for (int i = 0; i < seq.length; i++) {
				if (seq[i] != data[indexData]) {
					found = false;
					break;
				}
				indexData++;
			}
		} else {
			found = false;
		}
		return found;
	}
	
	private void safeFileRemoval(File file){
		if (file.exists()) {
		  appendToPane(jTextResults, "secure delete file "+file.getName()+ "\n", Color.BLUE);
		  RandomAccessFile raf = null;
		  try{
			long length = file.length();
			SecureRandom random = new SecureRandom();
			raf = new RandomAccessFile(file, "rws");
			raf.seek(0);
			raf.getFilePointer();
			byte[] data = new byte[64];
			int pos = 0;
			while (pos < length) {
				random.nextBytes(data);
				raf.write(data);
				pos += data.length;
			}
			raf.close();
			raf=null;
			file.delete();
		  }
		  catch(Exception e){
				appendToPane(jTextResults, e.getLocalizedMessage()+ "\n", Color.RED);
		  }
		  finally{
			  try{
				if(raf!=null){
				  raf.close();
				}  
			  }
			  catch(Exception e){
				e.printStackTrace();  
			  }
			  if(file.exists()){
				  file.delete(); 
			  }
		  }
		}
	}

}
