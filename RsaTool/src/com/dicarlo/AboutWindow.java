package com.dicarlo;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JPanel;
import java.awt.FlowLayout;

public class AboutWindow extends javax.swing.JDialog {

	private static final long serialVersionUID = 1L;
	ResourceBundle rb = ResourceBundle.getBundle("MyResources",
			Locale.getDefault());
	String iconApplication = "/keyspair.png";
	String iconAuthor = "/marcodicarlo.jpg";
	String iconFacebook = "/facebook.png";
	String iconLinkedIn = "/linkedin.png";
	String iconTwitter = "/twitter.png";
	String iconGooglePlus = "/googleplus.png";
	
	private JLabel jLabel0;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JButton jButton0;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	private JPanel panel;
	public AboutWindow() {
		initComponents();
	}
    public AboutWindow(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
	private void initComponents() {
		setFont(new Font("Dialog", Font.PLAIN, 12));
		setBackground(Color.white);
		setResizable(false);
		setForeground(Color.black);
		setAlwaysOnTop(true);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		//setLayout(new GroupLayout(this));
		GridBagConstraints gbc_jLabel0 = new GridBagConstraints();
		gbc_jLabel0.insets = new Insets(10, 0, 10, 0);
		gbc_jLabel0.anchor = GridBagConstraints.WEST;
		gbc_jLabel0.gridwidth = 2;
		gbc_jLabel0.gridx = 0;
		gbc_jLabel0.gridy = 0;
		getContentPane().add(getJLabel0(), gbc_jLabel0);
		GridBagConstraints gbc_jLabel1 = new GridBagConstraints();
		gbc_jLabel1.insets = new Insets(10, 0, 10, 0);
		gbc_jLabel1.anchor = GridBagConstraints.WEST;
		gbc_jLabel1.gridwidth = 2;
		gbc_jLabel1.gridx = 0;
		gbc_jLabel1.gridy = 1;
		getContentPane().add(getJLabel1(), gbc_jLabel1);
		GridBagConstraints gbc_jLabel2 = new GridBagConstraints();
		gbc_jLabel2.insets = new Insets(10, 0, 10, 5);
		gbc_jLabel2.gridx = 0;
		gbc_jLabel2.gridy = 2;
		getContentPane().add(getJLabel2(), gbc_jLabel2);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		getContentPane().add(getPanel(), gbc_panel);
		GridBagConstraints gbc_jButton0 = new GridBagConstraints();
		gbc_jButton0.insets = new Insets(10, 1, 10, 0);
		gbc_jButton0.gridwidth = 2;
		gbc_jButton0.gridx = 0;
		gbc_jButton0.gridy = 3;
		getContentPane().add(getJButton0(), gbc_jButton0);
	}
	private JLabel getJLabel6() {
		if (jLabel6 == null) {
			jLabel6 = new JLabel();
			jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconGooglePlus)));
			jLabel6.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent e) {
		        	try {
		                Desktop.getDesktop().browse(new URL("https://plus.google.com/103352222442441688686#103352222442441688686/posts").toURI());
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }

		        }
		    });

		}
		return jLabel6;
	}

	private JLabel getJLabel5() {
		if (jLabel5 == null) {
			jLabel5 = new JLabel();
			jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconFacebook)));
			jLabel5.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent e) {
		        	try {
		                Desktop.getDesktop().browse(new URL("http://www.facebook.com/marcodicarlo.profile").toURI());
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }

		        }
		    });

		}
		return jLabel5;
	}

	private JLabel getJLabel4() {
		if (jLabel4 == null) {
			jLabel4 = new JLabel();
			jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconTwitter)));
			jLabel4.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent e) {
		        	try {
		                Desktop.getDesktop().browse(new URL("https://twitter.com/Marco_Di_Carlo").toURI());
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }

		        }
		    });
		}
		return jLabel4;
	}

	private JLabel getJLabel3() {
		if (jLabel3 == null) {
			jLabel3 = new JLabel();
			jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconLinkedIn)));
			jLabel3.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mouseClicked(MouseEvent e) {
		        	try {
		                Desktop.getDesktop().browse(new URL("http://www.linkedin.com/pub/marco-di-carlo/25/b36/345").toURI());
		            } catch (Exception ex) {
		                ex.printStackTrace();
		            }

		        }
		    });
		}
		return jLabel3;
	}

	private JButton getJButton0() {
		if (jButton0 == null) {
			jButton0 = new JButton();
			jButton0.setText(rb.getString("button.close"));
			jButton0.setMnemonic('C');
			jButton0.setFont(new Font("Serif", Font.BOLD, 16));
			jButton0.setHorizontalAlignment(SwingConstants.CENTER);
			jButton0.addActionListener((new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					dispose();
				}
			}));
		}
		return jButton0;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource(iconAuthor)));
		}
		return jLabel2;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabel1.setForeground(new Color(0, 0, 255));
			jLabel1.setText(rb.getString("credit")+" Marco Di Carlo");
		}
		return jLabel1;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setFont(new Font("Tahoma", Font.BOLD, 11));
			jLabel0.setForeground(new Color(0, 0, 255));
			jLabel0.setText(rb.getString("version"));
		}
		return jLabel0;
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
		setTitle(rb.getString("title.about"));
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
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AboutWindow frame = new AboutWindow();
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setIcon();
				frame.setVisible(true);
			}
		});
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panel.add(getJLabel3());
			panel.add(getJLabel4());
			panel.add(getJLabel5());
			panel.add(getJLabel6());
		}
		return panel;
	}
}
