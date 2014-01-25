package com.dicarlo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Locale;
import java.util.ResourceBundle;

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
//import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;
import org.dyno.visual.swing.layouts.Trailing;


/*
 AUTHOR
 MARCO DI CARLO
*/

//VS4E -- DO NOT REMOVE THIS LINE!
public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	ResourceBundle rb = ResourceBundle.getBundle("MyResources",
			Locale.getDefault());
	String iconApplication = "/keyspair.png";
	String iconApplicationsmall = "/keyspairsmall.png";
	String iconLoading = "/loading.gif";
    private byte[] publicKey =null;
    private byte[] privateKey = null;
    private byte[] halfkey1=null;
    private byte[] halfkey2=null;

	private JPanel panelTitle;
	private JLabel labelTitle;
	private JPanel panelLength;
	private JLabel labelLength;
	private JLabel labelIcon;
	private JButton buttonClose;
	private JPanel panelButtons;
	private JButton buttonCreate;
	private JLabel labelWait;
	private JButton buttonPrivate1;
	private JButton buttonPrivate2;
	private JButton buttonPublic;
	private JFrame parent=null;
	private JSeparator separatorPane;
	private JLabel labelWaiting;
	private JLabel labelExport;
	//private JMenuItem jMenuItem0;
	private JMenu menuAbout;
	private JMenuBar menuBar;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public MainWindow() {
		initComponents();
	}

	private void initComponents() {
		setResizable(false);
		setLayout(new GroupLayout());
		add(getPanelTitle(), new Constraints(new Bilateral(12, 12, 0), new Leading(7, 57, 10, 10)));
		add(getPanelLength(), new Constraints(new Bilateral(12, 12, 0), new Leading(70, 66, 10, 10)));
		add(getJPanel0(), new Constraints(new Bilateral(12, 12, 0), new Leading(145, 100, 10, 10)));
		setJMenuBar(getJMenuBar());
		setSize(596, 271);
	}

	public JMenuBar getJMenuBar() {
		if (menuBar == null) {
			menuBar = new JMenuBar();
			menuBar.add(getMenuAbout());
		}
		return menuBar;
	}

	private JMenu getMenuAbout() {
		if (menuAbout == null) {
			menuAbout = new JMenu();
			menuAbout.setText(rb.getString("title.about"));
			menuAbout.setMnemonic('A');
			menuAbout.addMenuListener((new MenuListener(){

				@Override
				public void menuCanceled(MenuEvent arg0) {
					
				}

				@Override
				public void menuDeselected(MenuEvent arg0) {
					
				}

				@Override
				public void menuSelected(MenuEvent arg0) {
					AboutWindow frame = new AboutWindow(parent,true);
					frame.setIcon();
					frame.setVisible(true);				}
			}));
		}
		return menuAbout;
	}



	private JLabel getLabelExport() {
		if (labelExport == null) {
			labelExport = new JLabel();
			labelExport.setFont(new Font("Serif", Font.BOLD, 16));
			labelExport.setText(rb.getString("msg.exportingkeys"));
		}
		return labelExport;
	}

	private JLabel getLabelWaiting() {
		if (labelWaiting == null) {
			labelWaiting = new JLabel();
			labelWaiting.setForeground(new Color(0, 0, 255));
			labelWaiting.setFont(new Font("Serif", Font.BOLD, 16));
			labelWaiting.setText(rb.getString("msg.creationwaiting"));
		}
		return labelWaiting;
	}

	private JSeparator getSeparatorPane() {
		if (separatorPane == null) {
			separatorPane = new JSeparator();
			separatorPane.setBorder(new LineBorder(Color.black, 1, false));
		}
		return separatorPane;
	}

	private JButton getButtonPublic() {
		if (buttonPublic == null) {
			buttonPublic = new JButton();
			buttonPublic.setText(rb.getString("button.public"));
			buttonPublic.setMnemonic('P');
			buttonPublic.setFont(new Font("Serif", Font.BOLD, 16));
			buttonPublic.setHorizontalAlignment(SwingConstants.CENTER);
			buttonPublic.addActionListener((new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  JFileChooser fileChooser = new JFileChooser();
				  fileChooser.setDialogTitle(rb.getString("dialog.title"));    
				  fileChooser.setSelectedFile(new File("public.key"));
			      
				  int userSelection = fileChooser.showSaveDialog(parent);
				   
				  if (userSelection == JFileChooser.APPROVE_OPTION) {
				      File fileToSave = fileChooser.getSelectedFile();
				      System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				      OutputStream output = null;
				      FileOutputStream fos =null;
				      
				          try {
				        	fos =  new FileOutputStream(fileToSave.getAbsolutePath());
				            output = new BufferedOutputStream(fos);
				            output.write(publicKey);
				            output.flush();
				            output.close();
				            fos.close();
				            System.out.println("file: " + fileToSave.getAbsolutePath()+" saved");
				            
							JOptionPane.showMessageDialog(parent,
										rb.getString("msg.ok.copyingpublic")+" "+fileToSave.getAbsolutePath(), rb.getString("title.ok") ,
										JOptionPane.INFORMATION_MESSAGE);
				          }
				          catch(Exception e){
					          e.printStackTrace();
					          
							  JOptionPane.showMessageDialog(parent,
										rb.getString("msg.error.copyingpublic"), rb.getString("title.error") ,
										JOptionPane.ERROR_MESSAGE);
					        }
				          finally {
				            try {
				            	if(output!=null)
								  output.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            try {
				            	if(fos!=null)
				            		fos.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            
				          }
				        
				        
				  }
			  }
			}));
		}
		return buttonPublic;
	}

	private JButton getButtonPrivate2() {
		if (buttonPrivate2 == null) {
			buttonPrivate2 = new JButton();
			buttonPrivate2.setText(rb.getString("button.private2"));
			buttonPrivate2.setMnemonic('2');
			buttonPrivate2.setFont(new Font("Serif", Font.BOLD, 16));
			buttonPrivate2.setHorizontalAlignment(SwingConstants.CENTER);
			buttonPrivate2.addActionListener((new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  JFileChooser fileChooser = new JFileChooser();
				  fileChooser.setDialogTitle(rb.getString("dialog.title"));    
				  fileChooser.setSelectedFile(new File("private2.key"));
				  int userSelection = fileChooser.showSaveDialog(parent);
				   
				  if (userSelection == JFileChooser.APPROVE_OPTION) {
				      File fileToSave = fileChooser.getSelectedFile();
				      System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				      
				      OutputStream output = null;
				      FileOutputStream fos =null;
				      
				          try {
				        	fos =  new FileOutputStream(fileToSave.getAbsolutePath());
				            output = new BufferedOutputStream(fos);
				            output.write(halfkey2);
				            output.flush();
				            output.close();
				            fos.close();
				            System.out.println("file: " + fileToSave.getAbsolutePath()+" saved");
				            
							JOptionPane.showMessageDialog(parent,
										rb.getString("msg.ok.copyingprivate2")+" "+fileToSave.getAbsolutePath(), rb.getString("title.ok") ,
										JOptionPane.INFORMATION_MESSAGE);
				          }
				          catch(Exception e){
					          e.printStackTrace();
							  JOptionPane.showMessageDialog(parent,
										rb.getString("msg.error.copyingprivate2"), rb.getString("title.error") ,
										JOptionPane.ERROR_MESSAGE);
										
					        }
				          finally {
				            try {
				            	if(output!=null)
								  output.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            try {
				            	if(fos!=null)
				            		fos.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            
				          }
				        

				      
				      
				      
				  }
			  }
			}));
		}
		return buttonPrivate2;
	}

	private JButton getButtonPrivate1() {
		if (buttonPrivate1 == null) {
			buttonPrivate1 = new JButton();
			buttonPrivate1.setText(rb.getString("button.private1"));
			buttonPrivate1.setMnemonic('1');
			buttonPrivate1.setFont(new Font("Serif", Font.BOLD, 16));
			buttonPrivate1.setHorizontalAlignment(SwingConstants.CENTER);
			buttonPrivate1.addActionListener((new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent evt) {
				  JFileChooser fileChooser = new JFileChooser();
				  fileChooser.setDialogTitle(rb.getString("dialog.title"));    
				  fileChooser.setSelectedFile(new File("private1.key"));
				  int userSelection = fileChooser.showSaveDialog(parent);
				   
				  if (userSelection == JFileChooser.APPROVE_OPTION) {
				      File fileToSave = fileChooser.getSelectedFile();
				      System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				      
				      OutputStream output = null;
				      FileOutputStream fos =null;
				      
				          try {
				        	fos =  new FileOutputStream(fileToSave.getAbsolutePath());
				            output = new BufferedOutputStream(fos);
				            output.write(halfkey1);
				            output.flush();
				            output.close();
				            fos.close();
				            System.out.println("file: " + fileToSave.getAbsolutePath()+" saved");
				            
							JOptionPane.showMessageDialog(parent,
										rb.getString("msg.ok.copyingprivate1")+" "+fileToSave.getAbsolutePath(), rb.getString("title.ok") ,
										JOptionPane.INFORMATION_MESSAGE);

				          }
				          catch(Exception e){
					          e.printStackTrace();
					          
							  JOptionPane.showMessageDialog(parent,
										rb.getString("msg.error.copyingprivate1"), rb.getString("title.error") ,
										JOptionPane.ERROR_MESSAGE);

					        }
				          finally {
				            try {
				            	if(output!=null)
								  output.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            try {
				            	if(fos!=null)
				            		fos.close();
							} catch (IOException e) {
								
								e.printStackTrace();
							}
				            
				          }
				  }
			  }
			}));
		}
		return buttonPrivate1;
	}

	private JLabel getLabelWait() {
		if (labelWait == null) {
			labelWait = new JLabel();
			labelWait.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconLoading)));
		}
		return labelWait;
	}

	private JButton getButtonCreate() {
		if (buttonCreate == null) {
			buttonCreate = new JButton();
			buttonCreate.setText(rb.getString("button.generate"));
			buttonCreate.setMnemonic('r');
			buttonCreate.setFont(new Font("Serif", Font.BOLD, 16));
			buttonCreate.setHorizontalAlignment(SwingConstants.CENTER);
			buttonCreate.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					
					new Thread() {
					    @Override public void run () {
							try{
								labelWait.setVisible(true);
								buttonCreate.setEnabled(false);
								buttonClose.setEnabled(false);
								labelWaiting.setVisible(true);
								labelWaiting.setText(rb.getString("msg.creationwaiting"));
								//Thread.sleep(500);
								repaint();
								
								SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
								byte[]b=random.generateSeed(4096);
								random.setSeed(b);
								KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
						        keyGen.initialize(4096, random);
						        KeyPair keyPair=keyGen.genKeyPair();
						        publicKey = keyPair.getPublic().getEncoded();
						        privateKey = keyPair.getPrivate().getEncoded();
						        String formatPriv=keyPair.getPrivate().getFormat();
						        String formatPub=keyPair.getPublic().getFormat();		
						        System.out.println("generated OK.......FORMAT private:("+formatPriv+") - FORMAT public:("+formatPub+")");
						        labelWaiting.setText(rb.getString("msg.creationtesting"));
						        //System.out.println("Public key length: " + publicKey.length);
						        //System.out.println("Private key length: " + privateKey.length);
							    halfkey1= new byte[(privateKey.length/2)];
					
							    for(int i=0;i<(privateKey.length/2);i++){
							      halfkey1[i]=privateKey[i];

							    }
						        
							    halfkey2= new byte[privateKey.length-(privateKey.length/2)];
							    int counter=0;
							    for(int i=(privateKey.length/2);i<privateKey.length;i++){
							      halfkey2[counter]=privateKey[i];
							      counter++;
							    }

						        byte[] testPrivateKey = new byte[halfkey1.length+halfkey2.length];
							    for(int i=0;i<halfkey1.length;i++){
							    	testPrivateKey[i]=halfkey1[i];
							    }
							    for(int i=0;i<halfkey2.length;i++){
							    	testPrivateKey[i+halfkey1.length]=halfkey2[i];
							    }
							    
						        String text="test";
						        String dectyptedText="";
						        byte[] cipherText;
						        final Cipher cipher = Cipher.getInstance("RSA");
						        // encrypt the plain text using the public key
						        PublicKey publicKeyTest = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey));
						        cipher.init(Cipher.ENCRYPT_MODE, publicKeyTest);
						        cipherText = cipher.doFinal(text.getBytes());

						        // decrypt the text using the private key
						        PrivateKey privateKeyTest = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(testPrivateKey));
						        cipher.init(Cipher.DECRYPT_MODE, privateKeyTest);
						        dectyptedText = new String(cipher.doFinal(cipherText));
						        if(text.equals(dectyptedText)){
						        	System.out.println("test keys OK..........................");
									labelWait.setVisible(false);
									buttonCreate.setVisible(false);
									buttonPrivate1.setVisible(true);
									buttonPrivate2.setVisible(true);
									buttonPublic.setVisible(true);
									separatorPane.setVisible(true);
									labelExport.setVisible(true);
									labelWaiting.setVisible(false);
									
									buttonClose.setEnabled(true);
									repaint();
									
									JOptionPane.showMessageDialog(parent,
											rb.getString("msg.ok.generating"), rb.getString("title.ok") ,
											JOptionPane.INFORMATION_MESSAGE);

						        }
						        else{
						        	System.out.println("test keys KO..........................");
									labelWait.setVisible(false);
									buttonCreate.setVisible(true);
									buttonPrivate1.setVisible(false);
									buttonPrivate2.setVisible(false);
									buttonPublic.setVisible(false);
									separatorPane.setVisible(false);
									labelExport.setVisible(false);
									labelWaiting.setVisible(false);
									
									buttonCreate.setEnabled(true);
									buttonClose.setEnabled(true);
									repaint();
									
									JOptionPane.showMessageDialog(parent,
											rb.getString("msg.ok.generating"), rb.getString("title.error") ,
											JOptionPane.ERROR_MESSAGE);
						        }
						        
						        
										
								}
								catch(Exception e){
									e.printStackTrace();
									
									JOptionPane.showMessageDialog(parent,
											rb.getString("msg.error.generating"), rb.getString("title.error") ,
											JOptionPane.ERROR_MESSAGE);
									labelWait.setVisible(false);
									buttonCreate.setVisible(true);
									buttonPrivate1.setVisible(false);
									buttonPrivate2.setVisible(false);
									buttonPublic.setVisible(false);
									separatorPane.setVisible(false);
									labelExport.setVisible(false);
									labelWaiting.setVisible(false);
									
									buttonCreate.setEnabled(true);
									buttonClose.setEnabled(true);
									repaint();
											
								}
					    }
					  }.start();
				}
			}));
		}
		return buttonCreate;
	}

	private JPanel getJPanel0() {
		if (panelButtons == null) {
			panelButtons = new JPanel();
			panelButtons.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, null, null, null, null));
			panelButtons.setLayout(new GroupLayout());
			panelButtons.add(getButtonPrivate1(), new Constraints(new Leading(14, 114, 10, 10), new Leading(34, 52, 10, 10)));
			panelButtons.add(getButtonCreate(), new Constraints(new Leading(65, 114, 10, 10), new Leading(34, 53, 12, 12)));
			panelButtons.add(getButtonPublic(), new Constraints(new Leading(283, 111, 10, 10), new Leading(35, 50, 12, 12)));
			panelButtons.add(getButtonClose(), new Constraints(new Leading(426, 115, 10, 10), new Trailing(12, 53, 12, 12)));
			panelButtons.add(getLabelWait(), new Constraints(new Leading(259, 10, 10), new Leading(40, 34, 10, 10)));
			panelButtons.add(getLabelWaiting(), new Constraints(new Leading(229, 12, 12), new Leading(6, 12, 12)));
			panelButtons.add(getButtonPrivate2(), new Constraints(new Leading(149, 114, 10, 10), new Leading(35, 50, 12, 12)));
			panelButtons.add(getLabelExport(), new Constraints(new Leading(134, 12, 12), new Leading(6, 12, 12)));
			panelButtons.add(getSeparatorPane(), new Constraints(new Leading(7, 398, 12, 12), new Leading(2, 88, 10, 10)));
		}
		return panelButtons;
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

	private JPanel getPanelLength() {
		if (panelLength == null) {
			panelLength = new JPanel();
			panelLength.setBorder(new LineBorder(Color.black, 1, false));
			panelLength.setLayout(new GroupLayout());
			panelLength.add(getLabelLength(), new Constraints(new Leading(117, 10, 10), new Leading(12, 12, 12)));
			panelLength.add(getLabelIcon(), new Constraints(new Leading(39, 62, 10, 10), new Leading(5, 54, 10, 10)));
		}
		return panelLength;
	}

	private JLabel getLabelLength() {
		if (labelLength == null) {
			labelLength = new JLabel();
			labelLength.setFont(new Font("Serif", Font.BOLD, 30));
			labelLength.setForeground(new Color(0, 128, 128));
			labelLength.setHorizontalAlignment(SwingConstants.CENTER);
			labelLength.setText(rb.getString("title.keylength"));
		}
		return labelLength;
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
		setTitle(rb.getString("title"));
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
		buttonCreate.setVisible(true);
		buttonPrivate1.setVisible(false);
		buttonPrivate2.setVisible(false);
		buttonPublic.setVisible(false);
		labelWait.setVisible(false);
		separatorPane.setVisible(false);
		labelWaiting.setVisible(false);
		labelExport.setVisible(false);
		repaint();
		parent=this;
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
