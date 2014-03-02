package com.dicarlo;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
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

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;

//VS4E -- DO NOT REMOVE THIS LINE!
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
		setLayout(new GroupLayout());
		add(getJLabel2(), new Constraints(new Leading(19, 10, 10), new Leading(74, 10, 10)));
		add(getJLabel3(), new Constraints(new Leading(121, 50, 10, 10), new Leading(104, 43, 10, 10)));
		add(getJLabel4(), new Constraints(new Leading(180, 10, 10), new Leading(104, 47, 10, 10)));
		add(getJLabel5(), new Constraints(new Leading(242, 10, 10), new Leading(104, 12, 12)));
		add(getJLabel6(), new Constraints(new Leading(302, 12, 12), new Leading(104, 12, 12)));
		add(getJLabel0(), new Constraints(new Leading(9, 10, 10), new Leading(12, 12, 12)));
		add(getJLabel1(), new Constraints(new Leading(9, 10, 10), new Leading(34, 12, 12)));
		add(getJButton0(), new Constraints(new Leading(180, 112, 12, 12), new Leading(170, 40, 12, 12)));
		setSize(460, 248);
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
			jLabel1.setForeground(new Color(0, 0, 255));
			jLabel1.setText(rb.getString("credit")+" Marco Di Carlo");
		}
		return jLabel1;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
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

}
