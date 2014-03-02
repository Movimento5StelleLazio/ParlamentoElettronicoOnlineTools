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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
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
import org.dyno.visual.swing.layouts.Trailing;
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

 Jose Damico
 * Eclipse Public License - v 1.0 (http://www.eclipse.org/legal/epl-v10.html)
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

	private byte[] publicKeyBytes = null;
	private byte[] privateKeyBytes = null;
	private byte[] halfkey1 = null;
	private byte[] halfkey2 = null;

	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;

	private String base64PublicKey = "";

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
	javax.swing.JMenuItem menuDecrypt = null;
	javax.swing.JMenuItem menuTest = null;

	javax.swing.JMenuItem menuItalian = null;
	javax.swing.JMenuItem menuEnglish = null;
	javax.swing.JMenuItem menuChinese = null;
	private JProgressBar progressBar;

	//private boolean encryptMode = true;
	private boolean testMode = false;

	private boolean statusPrivateKey1 = false;
	private boolean statusPrivateKey2 = false;
	private boolean statusFileInD = false;
	private boolean statusFileOutD = false;


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

	// START COMPONENT FOR TEST
	private JPanel panelTest;
	private JTextField textFileOutT;
	private JLabel labelStatusFileOutT;
	private JButton buttonFileOutT;
	private JLabel labelFileOutT;
	// END COMPONENT FOR TEST

	private JButton buttonExecute;
	private JLabel labelPrivateKey2;
	private JTextField textPrivateKey2;
	private JButton buttonPrivateKey2;
	private JLabel labelStatusPrivateKey2;
	private JLabel labelWait;
	private JLabel labelProgress;

	public MainWindow() {
		initComponents();
	}

	private void initComponents() {
		setResizable(false);
		setLayout(new GroupLayout());
		add(getPanelTitle(), new Constraints(new Bilateral(12, 12, 0),
				new Leading(7, 57, 10, 10)));
		add(getPanelButtons(), new Constraints(new Leading(12, 584, 12, 12),
				new Leading(230, 65, 10, 10)));
		add(getProgressBar(), new Constraints(new Bilateral(12, 12, 10),
				new Leading(214, 12, 12)));
		add(getPanelAction(), new Constraints(new Bilateral(12, 12, 0),
				new Leading(70, 140, 10, 10)));
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
											if (statusFileOutD && statusFileInD
													&& statusPrivateKey1
													&& statusPrivateKey2) {
												buttonExecute.setEnabled(true);
											} else {
												buttonExecute.setEnabled(false);
											}

										} else {
											statusPrivateKey1 = false;
											statusPrivateKey2 = false;
											labelStatusPrivateKey1
													.setIcon(null);
											labelStatusPrivateKey2
													.setIcon(null);
										}

										if (statusFileOutD && statusFileInD
												&& statusPrivateKey1
												&& statusPrivateKey2) {
											buttonExecute.setEnabled(true);
										} else {
											buttonExecute.setEnabled(false);
										}
									} else {
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textPrivateKey1.setText("");
										statusPrivateKey1 = false;
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
			labelStatusPrivateKey2.setIcon(new ImageIcon(getClass()
					.getResource("/ko.png")));
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
											if (statusFileOutD && statusFileInD
													&& statusPrivateKey1
													&& statusPrivateKey2) {
												buttonExecute.setEnabled(true);
											} else {
												buttonExecute.setEnabled(false);
											}

										} else {
											statusPrivateKey1 = false;
											statusPrivateKey2 = false;
											labelStatusPrivateKey1
													.setIcon(null);
											labelStatusPrivateKey2
													.setIcon(null);
										}

										if (statusFileOutD && statusFileInD
												&& statusPrivateKey1
												&& statusPrivateKey2) {
											buttonExecute.setEnabled(true);
										} else {
											buttonExecute.setEnabled(false);
										}
									} else {
										labelStatusPrivateKey1
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textPrivateKey1.setText("");
										statusPrivateKey1 = false;
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
			labelStatusPrivateKey1.setIcon(new ImageIcon(getClass()
					.getResource("/ko.png")));
			statusPrivateKey1 = false;
		}
		return labelStatusPrivateKey1;
	}

	private JLabel getLabelFileOutD() {
		if (labelFileOutD == null) {
			labelFileOutD = new JLabel();
			labelFileOutD.setText(rb.getString("title.folderout"));
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
			buttonFileOutD
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return false;
											}
										}

										@Override
										public String getDescription() {
											return "Folder";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.isDirectory()
											&& !"".equals(file
													.getAbsolutePath())) {
										labelStatusFileOutD
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileOutD.setText(file
												.getAbsolutePath());
										statusFileOutD = true;
										if (statusFileOutD && statusFileInD
												&& statusPrivateKey1) {
											buttonExecute.setEnabled(true);
										} else {
											buttonExecute.setEnabled(false);
										}
									} else {
										labelStatusFileOutD
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileOutD.setText("");
										statusFileOutD = false;
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
			labelStatusFileOutD.setIcon(new ImageIcon(getClass().getResource(
					"/ko.png")));
			statusFileOutD = false;
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
			buttonFileInD
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();

							fileChooser
									.addChoosableFileFilter(new FileNameExtensionFilter(
											"XML Ciphered files", "xml",
											"ciphered"));
							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()) {
										labelStatusFileInD
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileInD.setText(file
												.getAbsolutePath());
										statusFileInD = true;
										if (statusFileOutD && statusFileInD
												&& statusPrivateKey1) {
											buttonExecute.setEnabled(true);
										} else {
											buttonExecute.setEnabled(false);
										}
									} else {
										labelStatusFileInD
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileInD.setText("");
										statusFileInD = false;
										buttonExecute.setEnabled(false);
									}
								}

							}
							repaint();
						}
					}));

		}
		return buttonFileInD;
	}

	private JLabel getLabelStatusFileInD() {
		if (labelStatusFileInD == null) {
			labelStatusFileInD = new JLabel();
			labelStatusFileInD.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource(iconKo)));
			statusFileInD = false;
		}
		return labelStatusFileInD;
	}

	private JPanel getPanelDecrypt() {
		if (panelDecrypt == null) {
			panelDecrypt = new JPanel();
			panelDecrypt.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelDecrypt.setLayout(new GroupLayout());
			panelDecrypt.add(getButtonFileInD(), new Constraints(new Leading(413, 12, 12), new Leading(3, 12, 12)));
			panelDecrypt.add(getButtonFileOutD(), new Constraints(new Leading(413, 12, 12), new Leading(30, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey1(), new Constraints(new Leading(413, 12, 12), new Leading(62, 12, 12)));
			panelDecrypt.add(getButtonPrivateKey2(), new Constraints(new Leading(413, 12, 12), new Leading(89, 12, 12)));
			panelDecrypt.add(getTextFileOutD(), new Constraints(new Leading(102, 308, 12, 12), new Leading(30, 24, 12, 12)));
			panelDecrypt.add(getTextFileInD(), new Constraints(new Leading(102, 308, 12, 12), new Leading(4, 24, 12, 12)));
			panelDecrypt.add(getTextPrivateKey1(), new Constraints(new Leading(102, 308, 12, 12), new Leading(62, 24, 44, 48)));
			panelDecrypt.add(getTextPrivateKey2(), new Constraints(new Leading(102, 308, 12, 12), new Leading(88, 24, 12, 12)));
			panelDecrypt.add(getLabelFileInD(), new Constraints(new Leading(2, 94, 12, 12), new Leading(8, 12, 12)));
			panelDecrypt.add(getLabelFileOutD(), new Constraints(new Leading(2, 94, 12, 12), new Leading(30, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey1(), new Constraints(new Leading(2, 94, 12, 12), new Leading(62, 12, 12)));
			panelDecrypt.add(getLabelPrivateKey2(), new Constraints(new Leading(2, 98, 10, 10), new Leading(94, 12, 12)));
			panelDecrypt.add(getLabelStatusFileInD(), new Constraints(new Trailing(0, 471, 471), new Leading(4, 12, 12)));
			panelDecrypt.add(getLabelStatusFileOutD(), new Constraints(new Trailing(0, 471, 471), new Leading(31, 12, 12)));
			panelDecrypt.add(getLabelStatusPrivateKey1(), new Constraints(new Trailing(0, 471, 471), new Leading(62, 12, 12)));
			panelDecrypt.add(getLabelStatusPrivateKey2(), new Constraints(new Trailing(0, 471, 471), new Leading(90, 12, 12)));
		}
		return panelDecrypt;
	}

	// START COMPONENT FOR TEST
	private JLabel getLabelFileInT() {
		if (labelFileOutT == null) {
			labelFileOutT = new JLabel();
			labelFileOutT.setText(rb.getString("title.folderout"));
		}
		return labelFileOutT;
	}

	private JTextField getTextFileOutT() {
		if (textFileOutT == null) {
			textFileOutT = new JTextField();
			textFileOutT.setEditable(false);
		}
		return textFileOutT;
	}

	private JButton getButtonFileOutT() {
		if (buttonFileOutT == null) {
			buttonFileOutT = new JButton();
			buttonFileOutT.setText("....");
			buttonFileOutT
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {
							JFileChooser fileChooser = new JFileChooser();
							fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
							fileChooser
									.addChoosableFileFilter(new FileFilter() {
										@Override
										public boolean accept(File f) {
											if (f.isDirectory()) {
												return true;
											} else {
												return false;
											}
										}

										@Override
										public String getDescription() {
											return "Folder";
										}
									});

							if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
								File file = fileChooser.getSelectedFile();
								if (file != null) {
									if (file.exists()) {
										labelStatusFileOutT
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconOk)));
										textFileOutT.setText(file
												.getAbsolutePath());


										buttonExecute.setEnabled(true);
									} else {
										labelStatusFileOutT
												.setIcon(new javax.swing.ImageIcon(
														getClass().getResource(
																iconKo)));
										textFileOutT.setText("");
										buttonExecute.setEnabled(false);
									}
								}

							}
							repaint();
						}
					}));

		}
		return buttonFileOutT;
	}

	private JLabel getLabelStatusFileOutT() {
		if (labelStatusFileOutT == null) {
			labelStatusFileOutT = new JLabel();
			labelStatusFileOutT.setIcon(new javax.swing.ImageIcon(getClass()
					.getResource(iconKo)));
		}
		return labelStatusFileOutT;
	}

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

	private JPanel getPanelTest() {
		if (panelTest == null) {
			panelTest = new JPanel();
			panelTest.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelTest.setLayout(new GroupLayout());
			panelTest.add(getTextFileOutT(), new Constraints(new Leading(91, 319, 10, 10), new Leading(5, 24, 10, 10)));
			panelTest.add(getButtonFileOutT(), new Constraints(new Leading(413, 10, 10), new Leading(4, 12, 12)));
			panelTest.add(getJScrollPane0(), new Constraints(new Leading(5, 484, 12, 12), new Leading(36, 80, 10, 10)));
			panelTest.add(getLabelFileInT(), new Constraints(new Leading(4, 85, 10, 10), new Leading(12, 12, 12)));
			panelTest.add(getLabelStatusFileOutT(), new Constraints(new Leading(473, 10, 10), new Leading(5, 12, 12)));
		}
		return panelTest;
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
			menuFile.add(getMenuDecrypt());
			menuFile.add(getMenuTest());
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
		menuDecrypt.setText(rb.getString("title.decrypt"));
		menuTest.setText(rb.getString("title.test"));
		menuAbout.setText(rb.getString("title.about"));
		menuVersion.setText(rb.getString("title.version"));
		buttonClose.setText(rb.getString("button.close"));
		labelTitle.setText(rb.getString("title"));


		labelFileOutT.setText(rb.getString("title.folderout"));
		
		labelFileInD.setText(rb.getString("title.filein"));
		labelFileOutD.setText(rb.getString("title.fileout"));
		labelPrivateKey1.setText(rb.getString("title.privatekey1"));
		labelPrivateKey2.setText(rb.getString("title.privatekey2"));

		if (testMode) {
			parent.setTitle(rb.getString("title") + " - ["
					+ rb.getString("title.test") + "]");
			buttonExecute.setText(rb.getString("title.test"));
			buttonExecute.setMnemonic('T');
		} else {
			parent.setTitle(rb.getString("title") + " - ["
						+ rb.getString("title.decrypt") + "]");
			buttonExecute.setText(rb.getString("title.decrypt"));
			buttonExecute.setMnemonic('D');
			
		}
		repaint();
	}


	private JMenuItem getMenuDecrypt() {
		if (menuDecrypt == null) {
			menuDecrypt = new javax.swing.JMenuItem(
					rb.getString("title.decrypt"));
			menuDecrypt.setMnemonic('D');
			menuDecrypt.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					clearPane(jTextResults);
					//encryptMode = false;
					testMode = false;
					parent.setTitle(rb.getString("title") + " - ["
							+ rb.getString("title.decrypt") + "]");
					panelDecrypt.setVisible(true);
					panelTest.setVisible(false);
					buttonExecute.setText(rb.getString("title.decrypt"));
					buttonExecute.setMnemonic('D');
					buttonExecute.setEnabled(false);
					textPrivateKey1.setText("");
					statusPrivateKey1 = false;
					labelStatusPrivateKey1.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));
					textPrivateKey2.setText("");
					statusPrivateKey2 = false;
					labelStatusPrivateKey2.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));
					textFileInD.setText("");
					statusFileInD = false;
					labelStatusFileInD.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));
					textFileOutD.setText("");
					statusFileOutD = false;
					labelStatusFileOutD.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));
					privateKey = null;
					privateKeyBytes = null;
					halfkey1 = null;
					halfkey2 = null;
					test = false;
				}
			}));
		}
		return menuDecrypt;
	}

	private JMenuItem getMenuTest() {
		if (menuTest == null) {
			menuTest = new javax.swing.JMenuItem(rb.getString("title.test"));
			menuTest.setMnemonic('T');
			menuTest.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					clearPane(jTextResults);
					testMode = true;
					//encryptMode = false;
					parent.setTitle(rb.getString("title") + " - ["
							+ rb.getString("title.test") + "]");
					panelDecrypt.setVisible(false);
					panelTest.setVisible(true);
					buttonExecute.setText(rb.getString("title.test"));
					buttonExecute.setMnemonic('T');
					buttonExecute.setEnabled(false);
					textFileOutT.setText("");
					labelStatusFileOutT.setIcon(new javax.swing.ImageIcon(
							getClass().getResource(iconKo)));

					publicKey = null;
					publicKeyBytes = null;
					privateKey = null;
					privateKeyBytes = null;
					halfkey1 = null;
					halfkey2 = null;
					test = true;

				}
			}));
		}
		return menuTest;
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
			panelButtons.add(getButtonExecute(), new Constraints(new Leading(69, 128, 10, 10), new Leading(5, 53, 12, 12)));
			panelButtons.add(getButtonClose(), new Constraints(new Leading(389, 130, 10, 10), new Leading(5, 53, 12, 12)));
		}
		return panelButtons;
	}

	private JButton getButtonExecute() {
		if (buttonExecute == null) {
			buttonExecute = new JButton();
			buttonExecute.setFont(new Font("Serif", Font.BOLD, 16));
			buttonExecute.setText(rb.getString("title.decrypt"));
			buttonExecute.setMnemonic('D');
			buttonExecute.setEnabled(false);
			buttonExecute
					.addActionListener((new java.awt.event.ActionListener() {
						public void actionPerformed(
								java.awt.event.ActionEvent evt) {

							new Thread() {
								@Override
								public void run() {

									if (testMode) {
										mapSerialSeed.clear();
										clearPane(jTextResults);
										// jTextResults.setText("");
										testFile();
									} else {
										mapSerialSeed.clear();
										clearPane(jTextResults);
										if (statusFileInD && statusFileOutD
												&& statusPrivateKey1
												&& statusPrivateKey2) {
											decryptFile();
										} else {

										}
									}

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
			panelAction.add(getLabelIcon(), new Constraints(new Leading(8, 53,
					18, 18), new Leading(30, 54, 12, 12)));
			panelAction.add(getPanelDecrypt(), new Constraints(new Bilateral(
					67, 12, 0), new Leading(5, 126, 10, 10)));
			panelAction.add(getPanelTest(), new Constraints(new Bilateral(67,
					12, 0), new Leading(5, 126, 10, 10)));

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
			panelTitle.add(getLabelTitle(), new Constraints(new Bilateral(12,
					12, 0), new Leading(7, 44, 10, 10)));
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

	private void createTestKeys() {
		try {
			labelProgress.setText(rb.getString("msg.creatingtestkeys"));
			BlumBlumShub random = new BlumBlumShub(4096);
			byte b[] = random.randBytes(4096);
			random.setSeed(b);
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(RSA_KEY_LENGTH, random);
			KeyPair keyPair = keyGen.genKeyPair();
			publicKey = keyPair.getPublic();
			privateKey = keyPair.getPrivate();

			publicKeyBytes = privateKey.getEncoded();
			base64PublicKey = Base64.encode(publicKeyBytes);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void readTestKeys() {
		ByteArrayOutputStream bos = null;
		InputStream fispbk = null;
		InputStream fisprk1 = null;
		InputStream fisprk2 = null;
		try {
			bos = new ByteArrayOutputStream();
			fispbk = this.getClass().getResourceAsStream("/public.key");
			fisprk1 = this.getClass().getResourceAsStream("/private1.key");
			fisprk2 = this.getClass().getResourceAsStream("/private2.key");

			int b = fispbk.read();
			while (b > -1) {
				bos.write(b);
				b = fispbk.read();
			}
			bos.flush();
			byte[] key = bos.toByteArray();
			bos.close();

			bos = new ByteArrayOutputStream();
			b = fisprk1.read();
			while (b > -1) {
				bos.write(b);
				b = fisprk1.read();
			}
			bos.flush();
			byte[] halfkey1 = bos.toByteArray();
			bos.close();

			bos = new ByteArrayOutputStream();
			b = fisprk2.read();
			while (b > -1) {
				bos.write(b);
				b = fisprk2.read();
			}
			bos.flush();
			byte[] halfkey2 = bos.toByteArray();
			bos.close();

			publicKey = KeyFactory.getInstance("RSA").generatePublic(
					new X509EncodedKeySpec(Base64.decode(key)));
			base64PublicKey = new String(key);

			byte[] b1 = Base64.decode(halfkey1);
			byte[] b2 = Base64.decode(halfkey2);

			byte[] privateKeyBytes = new byte[halfkey1.length + halfkey2.length];
			for (int i = 0; i < b1.length; i++) {
				privateKeyBytes[i] = b1[i];
			}
			for (int i = 0; i < b2.length; i++) {
				privateKeyBytes[i + b1.length] = b2[i];
			}

			privateKey = KeyFactory.getInstance("RSA").generatePrivate(
					new PKCS8EncodedKeySpec(privateKeyBytes));
			bos.close();
			fispbk.close();
			fisprk1.close();
			fisprk2.close();
			bos = null;
			fispbk = null;
			fisprk1 = null;
			fisprk2 = null;

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				if (bos != null)
					bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fispbk != null)
					fispbk.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fisprk1 != null)
					fisprk1.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fisprk2 != null)
					fisprk2.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void testFile() {

		buttonExecute.setEnabled(false);
		serialDuplicated = false;
		errorReadingLine = false;
		errorVerifying = false;
		String fileBin = null;
		String fileXml = null;
		InputStream srcFileName=null;
		try {
			srcFileName = this.getClass().getResourceAsStream("/keyvalue.csv");
			String outFolder = textFileOutT.getText();
			if(!outFolder.endsWith(File.separator)){
				outFolder=outFolder+File.separator;
			}
			if(!new File(outFolder).exists()||!new File(outFolder).canWrite()){
				appendToPane(jTextResults, rb.getString("msg.foldererror")
						+ "\n", Color.RED);
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.foldererror"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			fileBin = outFolder + "test_ciphered.bin";
			fileXml = outFolder + "test.xml";			
			test = true;
			appendToPane(jTextResults, rb.getString("msg.creatingtestkeys")
					+ "\n", Color.BLUE);
			// createTestKeys();
			readTestKeys();
			if (!encrypt(srcFileName,fileXml,fileBin)) {
				appendToPane(jTextResults, rb.getString("msg.ciphererror")
						+ "\n", Color.RED);
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.ciphererror"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				return;
			} else {
				appendToPane(jTextResults, rb.getString("msg.cipherok") + "\n",
						Color.BLUE);
			}
			if (serialDuplicated) {
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.doublederror"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (errorReadingLine) {
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.lineerror"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}


			if (!decrypt(fileBin, fileXml)) {
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.deciphererror"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (!verifyResults(fileXml)) {
				appendToPane(jTextResults,
						rb.getString("msg.testError") + "\n", Color.RED);
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.testError"),
						rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
				errorVerifying = true;
				return;
			} else {
				appendToPane(jTextResults, rb.getString("msg.testOK") + "\n",
						Color.BLUE);
				JOptionPane.showMessageDialog(parent,
						rb.getString("msg.testOK"),
						rb.getString("title.ok"),
						JOptionPane.INFORMATION_MESSAGE);

			}

		} finally {
			try{
				if(srcFileName!=null)
					srcFileName.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			if (fileBin != null&&new File(fileBin).exists())
				safeFileRemoval(new File(fileBin));
			if (fileXml != null&&new File(fileXml).exists())
				safeFileRemoval(new File(fileXml));
		}

		buttonExecute.setEnabled(true);

	}


	private synchronized boolean encrypt(InputStream srcFileName,String fileXml,String fileBin) {
		boolean retval = false;
		ArrayList<String> listSerial = new ArrayList<String>();
		progressBar.setVisible(true);
		progressBar.setMaximum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		labelProgress.setText(rb.getString("msg.formattingkeys") + " 0%");
		labelProgress.setVisible(true);
		labelWait.setVisible(true);
		buttonClose.setEnabled(false);
		buttonExecute.setEnabled(false);
		menuFile.setEnabled(false);
		menuAbout.setEnabled(false);
		menuLanguage.setEnabled(false);


		repaint();

		//long fileLen = new File(srcFileName).length();
		long counter = 0;
		int progressValue = 0;
		int prevProgressValue = 0;
		InputStreamReader fr = null;
		BufferedReader in = null;
		FileOutputStream outputWriter = null;
		InputStream inputReader = null;
		CipherOutputStream cos = null;

		try {
			appendToPane(jTextResults, rb.getString("msg.formattingkeys") + "\n",	Color.BLUE);
			outputWriter = new FileOutputStream(fileXml);
			fr = new InputStreamReader(srcFileName);
			in = new BufferedReader(fr);
			String string;
			long i = 0;
			AlgorithmParameters ap = new AlgorithmParameters(
					new ResponseFormat("6", "DECIMAL"),
					Constants.ALGO_TYPE_TOTP);
			// String separator=";";
			outputWriter.write(getXmlHeader().getBytes("UTF8"));
			outputWriter.flush();
			while ((string = in.readLine()) != null) {

				try {
					if(!"".equals(string.trim())){
					String[] serialkey = getSerialKey(string.trim());
					String lineserial = serialkey[0];
					String linekey = serialkey[1];

					if (!listSerial.contains(lineserial)) {
						if (test) {
							mapSerialSeed.put(lineserial, linekey);
						}
						DeviceInfo di = new DeviceInfo("xyzw", lineserial);
						byte[] byteSeed = keyGetByteCiphered(linekey);

						String data = Base64.encode(byteSeed);
						Data d = new Data(new Secret(data), "60", "0");
						// Data d = new Data(new Secret(linekey), "60", "0");
						org.jdamico.pskcbuilder.dataobjects.Key k = new org.jdamico.pskcbuilder.dataobjects.Key(
								"1", "urn:ietf:params:xml:ns:keyprov:pskc:"
										+ Constants.ALGO_TYPE_TOTP, "xyzw", d,
								ap);
						KeyPackage kp = new KeyPackage(di, k);

						outputWriter.write(getObj2XmlStr(kp, i)
								.getBytes("UTF8"));
						outputWriter.flush();
						listSerial.add(lineserial);
					} else {
						logger.error("Error: element(" + (i + 1) + ") file("
								+ srcFileName + ") serial(" + lineserial
								+ ") duplicate");
						appendToPane(
								jTextResults,
								"(" + (i + 1) + ")[" + lineserial + "] "
										+ rb.getString("msg.doublederror")
										+ "\n", Color.RED);
						// jTextResults.append("("+(i +
						// 1)+")["+lineserial+"] "+rb.getString("msg.doublederror")+"\n");
						serialDuplicated = true;
					}
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("Error: element(" + (i + 1) + ") file("
							+ srcFileName + ")", e);
					appendToPane(
							jTextResults,
							"(" + (i + 1) + ") "
									+ rb.getString("msg.lineerror") + "\n",
							Color.RED);
					// jTextResults.append("("+(i +
					// 1)+") "+rb.getString("msg.lineerror")+"\n");
					errorReadingLine = true;
				}

				counter += string.length();
                /*
				progressValue = (int) ((counter * 100) / fileLen);
				if (progressValue != prevProgressValue) {
					progressBar.setValue(progressValue);
					labelProgress.setText(rb.getString("msg.formattingkeys")
							+ " " + progressValue + "%");
					prevProgressValue = progressValue;
					// System.out.println(prevProgressValue+"%");
					repaint();
				}
				*/
				i++;
			}

			outputWriter.write(getXmlFooter().getBytes("UTF8"));
			outputWriter.flush();
			outputWriter.close();
			in.close();
			fr.close();
			in = null;
			fr = null;
			outputWriter = null;
			progressBar.setValue(100);
			labelProgress.setText(rb.getString("msg.formattingkeys") + "100%");
			repaint();
			appendToPane(jTextResults, rb.getString("msg.ciphering") + "\n",	Color.BLUE);
			
			progressBar.setMaximum(0);
			progressBar.setMaximum(100);
			progressBar.setValue(0);
			labelProgress.setText(rb.getString("msg.ciphering") + " 0%");
			labelProgress.setVisible(true);
			labelWait.setVisible(true);
			buttonClose.setEnabled(false);
			buttonExecute.setEnabled(false);

			menuFile.setEnabled(false);
			menuAbout.setEnabled(false);
			menuLanguage.setEnabled(false);
			repaint();
			long fileLen = new File(fileXml).length();
			outputWriter = null;
			inputReader = null;
			// System.out.println("srcFileName ----- "+srcFileName);
			// System.out.println("destFileName ----- "+destFileName);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			/*
			 * SecureRandom random = SecureRandom.getInstance("SHA1PRNG",
			 * "SUN"); byte[] b = random.generateSeed(AES_KEY_LENGTH);
			 * random.setSeed(b);
			 */

			BlumBlumShub random = new BlumBlumShub(1024);
			byte b[] = random.randBytes(1024);
			random.setSeed(b);
			KeyGenerator keyGen = KeyGenerator.getInstance("AES");
			keyGen.init(AES_KEY_LENGTH, random);
			SecretKey secretAesKey = keyGen.generateKey();

			byte[] aesKey = secretAesKey.getEncoded();

			byte[] iv = new byte[16];
			// SecureRandom.getInstance("SHA1PRNG").nextBytes(iv); 
			random = new BlumBlumShub(1024);
			iv = random.randBytes(16);

			counter = 0;
			progressValue = 0;
			prevProgressValue = 0;

			outputWriter = new FileOutputStream(fileBin);
			inputReader = new FileInputStream(fileXml);
			byte[] cipherText = null;
			cipherText = cipher.doFinal(aesKey);
			outputWriter.write(cipherText);
			outputWriter.flush();

			outputWriter.write(END_OF_KEY.getBytes("UTF-8"));
			outputWriter.flush();

			cipherText = null;
			cipherText = cipher.doFinal(iv);
			outputWriter.write(cipherText);
			outputWriter.flush();

			// outputWriter.write(iv);
			// outputWriter.flush();
			outputWriter.write(END_OF_KEY.getBytes("UTF-8"));
			outputWriter.flush();

			cipher = Cipher.getInstance("AES");

			AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

			encryptAES(secretAesKey, paramSpec, inputReader, outputWriter,
					fileLen);

			outputWriter.close();
			inputReader.close();
			outputWriter = null;
			inputReader = null;
			progressBar.setVisible(false);
			labelProgress.setVisible(false);
			labelWait.setVisible(false);

			retval = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.debug("Error: file(" + srcFileName + ")", e);
			appendToPane(jTextResults, e.getLocalizedMessage() + "\n",	Color.RED);
		} finally {
			try {
				if (cos != null)
					cos.close();
				if (in != null)
					in.close();
				if (fr != null)
					fr.close();
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
			safeFileRemoval(new File(fileXml));

		}
		return retval;
	}

	private void decryptFile() {
		String srcFileName = textFileInD.getText();
		String outFolder = textFileOutD.getText();
		if(!outFolder.endsWith(File.separator)){
			outFolder=outFolder+File.separator;
		}
        String destFileName = outFolder;
		// String destFile = textFileOutD.getText()+".bin";
		test = false;
		if(srcFileName.endsWith("_xml_ciphered.bin")){
			String cipheredName= new File(srcFileName).getName();
			destFileName = destFileName + cipheredName.substring(0,cipheredName.indexOf("_xml_ciphered.bin"))+".xml";	
		}
		else{
			String cipheredName= new File(srcFileName).getName();
			destFileName = destFileName + cipheredName+".xml";	
		}
		if(!new File(outFolder).exists()||!new File(outFolder).canWrite()){
			JOptionPane.showMessageDialog(parent,
					rb.getString("msg.foldererror"),
					rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if (decrypt(srcFileName, destFileName)) {
			appendToPane(jTextResults, rb.getString("msg.decipherok") + "\n",
					Color.BLUE);
			JOptionPane.showMessageDialog(parent,
					rb.getString("msg.decipherok"), rb.getString("title.ok"),
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			appendToPane(jTextResults,
					rb.getString("msg.deciphererror") + "\n", Color.RED);
			JOptionPane.showMessageDialog(parent,
					rb.getString("msg.deciphererror"),
					rb.getString("title.error"), JOptionPane.ERROR_MESSAGE);
		}

	}

	private synchronized boolean decrypt(String srcFileName, String destFileName) {
		boolean retval = false;
		progressBar.setVisible(true);
		progressBar.setMaximum(0);
		progressBar.setMaximum(100);
		progressBar.setValue(0);
		labelProgress.setText(rb.getString("msg.deciphering") + " 0%");
		labelProgress.setVisible(true);
		labelWait.setVisible(true);
		buttonClose.setEnabled(false);
		buttonExecute.setEnabled(false);
		if (buttonFileInD != null) {
			buttonFileInD.setEnabled(false);
		}
		if (buttonFileOutD != null) {
			buttonFileOutD.setEnabled(false);
		}
		if (buttonPrivateKey1 != null) {
			buttonPrivateKey1.setEnabled(false);
		}
		if (buttonPrivateKey2 != null) {
			buttonPrivateKey2.setEnabled(false);
		}
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

			if (buttonFileInD != null) {
				buttonFileInD.setEnabled(true);
			}
			if (buttonFileOutD != null) {
				buttonFileOutD.setEnabled(true);
			}
			if (buttonPrivateKey1 != null) {
				buttonPrivateKey1.setEnabled(true);
			}
			if (buttonPrivateKey2 != null) {
				buttonPrivateKey2.setEnabled(true);
			}
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

	private byte[] keyGetByteCiphered(String value) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			// byte[]byteSeed = cipher.doFinal(linekey.getBytes());
			// byte[]byteSeed =
			// cipher.doFinal(formatKey(value.getBytes("UTF8")));
			byte[] byteSeed = cipher.doFinal(value.getBytes("UTF8"));
			cipher.doFinal();
			cipher.getIV();
			cipher = null;
			return byteSeed;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * public synchronized byte[] hexStringToByteArray(String s) throws
	 * Exception { int len = s.length(); byte[] data = new byte[len / 2]; for
	 * (int i = 0; i < len; i += 2) data[i / 2] = (byte)
	 * ((Character.digit(s.charAt(i), 16) << 4) + Character .digit(s.charAt(i +
	 * 1), 16)); return data; }
	 * 
	 * public String byteArrayToHexString(byte[] raw) throws
	 * UnsupportedEncodingException { final byte[] HEX_CHAR_TABLE = { (byte)
	 * '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte)
	 * '6', (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte)
	 * 'c', (byte) 'd', (byte) 'e', (byte) 'f' };
	 * 
	 * byte[] hex = new byte[2 * raw.length]; int index = 0;
	 * 
	 * for (byte b : raw) { int v = b & 0xFF; hex[index++] = HEX_CHAR_TABLE[v
	 * >>> 4]; hex[index++] = HEX_CHAR_TABLE[v & 0xF]; } return new String(hex,
	 * "ASCII"); }
	 */
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

	private synchronized String getXmlHeader() {
		StringBuffer sb = new StringBuffer();
		/*
		 * sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"); sb.append(
		 * "<KeyContainer Version=\"1.0\" xmlns=\"urn:ietf:params:xml:ns:keyprov:pskc\">\n"
		 * );
		 */
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sb.append("<KeyContainer xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" xmlns=\"urn:ietf:params:xml:ns:keyprov:pskc\" xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" id=\"KC0001\" Version=\"1.0\">\n");
		sb.append("<EncryptionKey><ds:X509Data><ds:X509Certificate>"
				+ base64PublicKey
				+ "</ds:X509Certificate></ds:X509Data></EncryptionKey>\n");
		return sb.toString();
	}

	private synchronized String getXmlFooter() {
		StringBuffer sb = new StringBuffer();
		sb.append("</KeyContainer>");
		return sb.toString();
	}

	private synchronized String getObj2XmlStr(KeyPackage kp, long i) {
		StringBuffer sb = new StringBuffer();

		sb.append("<KeyPackage>\n");
		sb.append("<DeviceInfo>\n");
		sb.append("<Manufacturer>" + kp.getDeviceInfo().getManufacturer()
				+ "</Manufacturer>\n");
		sb.append("<SerialNo>" + kp.getDeviceInfo().getSerialNo()
				+ "</SerialNo>\n");
		sb.append("</DeviceInfo>\n");
		sb.append("<Key Id=\"" + (i + 1) + "\" Algorithm=\""
				+ kp.getKey().getAlgorithm() + "\">\n");
		sb.append("<Issuer>" + kp.getKey().getIssuer() + "</Issuer>\n");
		sb.append("<AlgorithmParameters>\n");
		sb.append("<ResponseFormat Length=\""
				+ kp.getKey().getAlgorithmParameters().getResponseFormat()
						.getLength()
				+ "\" Encoding=\""
				+ kp.getKey().getAlgorithmParameters().getResponseFormat()
						.getEncoding() + "\"/>\n");
		sb.append("</AlgorithmParameters>\n");
		sb.append("<Data>\n");

		sb.append("<Secret><EncryptedValue><xenc:EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa_1_5\"/><xenc:CipherData><xenc:CipherValue>"
				+ kp.getKey().getData().getSecret().getPlainValue()
				+ "</xenc:CipherValue></xenc:CipherData></EncryptedValue></Secret>\n");

		String sAlgoType = kp.getKey().getAlgorithmParameters().getAlgoType() == Constants.ALGO_TYPE_HOTP ? "Counter"
				: "Time";

		sb.append("<" + sAlgoType + "><PlainValue>"
				+ kp.getKey().getData().getCounter() + "</PlainValue></"
				+ sAlgoType + ">\n");
		sb.append("<TimeInterval><PlainValue>"
				+ kp.getKey().getData().getTimeInterval()
				+ "</PlainValue></TimeInterval>\n");
		// "<TimeDrift><PlainValue>"+kp.getKeyPackageList().get(i).getKey().getData().getTimeInterval()+"</PlainValue></TimeDrift>\n"
		// +
		sb.append("</Data>\n");
		sb.append("</Key>\n");
		sb.append("</KeyPackage>\n");

		return sb.toString();
	}

	private static final int AES_KEY_LENGTH = 128;
	private static final int RSA_KEY_LENGTH = 4096;
	private boolean test = false;
	private boolean serialDuplicated = false;
	private boolean errorReadingLine = false;
	private boolean errorVerifying = false;
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
				frame.panelDecrypt.setVisible(true);
				frame.panelTest.setVisible(false);
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
