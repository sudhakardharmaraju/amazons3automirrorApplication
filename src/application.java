import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Toolkit;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import java.awt.SystemColor;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;

import java.awt.Color;

import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileUpload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.util.StringUtils;

import javax.swing.JTree;
import javax.swing.JScrollPane;

import org.aspectj.weaver.ast.Var;

public class S3 extends JFrame {

	private JPanel contentPane;
	private JDesktopPane desktopPane;
	private JTextField txtEnterTheAccess;
	private JPasswordField passwordField;
	private JPanel Bucket_Directories;
	private JPanel AWS_Credential;
	private JTextField txtAbcd;
	private JTextField txtFolderIsNot;
	
	private String accessKey = null;
	private String secretKey = null;
	private String bucketName = null;
	private String virtualKeyPath = null;
	
	private AmazonS3 con = null;
	private JTextField keyPath;
	
	private AWSCredentials Credentials;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					S3 frame = new S3();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public S3() {
		setResizable(false);
		setAutoRequestFocus(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(S3.class.getResource("/com/aws/resourse/download.jpg")));
		setTitle("AMAZON S3 AUTO BACKUP (sync) 1.0 ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 958, 516);
		
		final JComboBox<ComboItem> bucketBox = new JComboBox<ComboItem>();
		bucketBox.setEnabled(false);
		bucketBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				bucketName =((ComboItem) bucketBox.getSelectedItem()).toString();
				keyPath.setEnabled(true);
				
			
				}
			
		});
		
		
		
		
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAboutAmazonS = new JMenuItem("About AMAZON S3 AUTO BACKUP 1.0");
		mnHelp.add(mntmAboutAmazonS);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		desktopPane = new JDesktopPane();
		desktopPane.setBackground(SystemColor.window);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(desktopPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 942, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(desktopPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
		);
		
		AWS_Credential = new JPanel();
		AWS_Credential.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "AWS Credentials", TitledBorder.LEFT, TitledBorder.TOP, null, SystemColor.desktop));
		AWS_Credential.setBackground(SystemColor.window);
		AWS_Credential.setBounds(10, 11, 316, 207);
		desktopPane.add(AWS_Credential);
		
		JLabel lblA = new JLabel("Access Key :");
		lblA.setForeground(Color.BLACK);
		lblA.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblA.setHorizontalAlignment(SwingConstants.LEFT);
		
		txtEnterTheAccess = new JTextField();
		txtEnterTheAccess.setText("Enter the Access Key");
		txtEnterTheAccess.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		txtEnterTheAccess.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Secret Key :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		passwordField = new JPasswordField();
		
		JButton btnAccess = new JButton("Access");
		
		
		btnAccess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				accessKey = txtEnterTheAccess.getText();
				secretKey = passwordField.getText();
				
				if(accessKey!=null&&secretKey!=null){
				
				try {
					 Credentials = new BasicAWSCredentials(accessKey, secretKey);
					 con = new AmazonS3Client(Credentials);
					 
					 
					
					java.util.List<Bucket> buckets = con.listBuckets();
					
					bucketBox.setEnabled(true);
					
						for (Bucket bucket : buckets) {
							
							bucketBox.addItem(new ComboItem(bucket.getName()));
							
					        
					}
						
					
					
				} catch (AmazonS3Exception Ae) {
					JOptionPane.showMessageDialog(null, Ae.getErrorCode());
					
				}catch (AmazonClientException e) {
					JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
					
				}
				
				
				
			 }else{
				 JOptionPane.showMessageDialog(null, "no values");
			 }
				
				
				
				
			}
		});
		btnAccess.setForeground(new Color(0, 0, 128));
		btnAccess.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GroupLayout gl_AWS_Credential = new GroupLayout(AWS_Credential);
		gl_AWS_Credential.setHorizontalGroup(
			gl_AWS_Credential.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AWS_Credential.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblA, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
				.addGroup(gl_AWS_Credential.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(165, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_AWS_Credential.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_AWS_Credential.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnAccess, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)
						.addComponent(passwordField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
						.addComponent(txtEnterTheAccess, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE))
					.addGap(19))
		);
		gl_AWS_Credential.setVerticalGroup(
			gl_AWS_Credential.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_AWS_Credential.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblA)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtEnterTheAccess, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(passwordField, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnAccess, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(146, Short.MAX_VALUE))
		);
		AWS_Credential.setLayout(gl_AWS_Credential);
		
		Bucket_Directories = new JPanel();
		Bucket_Directories.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "Bucket & Directories", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		Bucket_Directories.setBackground(SystemColor.window);
		Bucket_Directories.setForeground(new Color(0, 0, 0));
		Bucket_Directories.setBounds(10, 229, 316, 207);
		desktopPane.add(Bucket_Directories);
		
		JLabel lblBucketDirectories = new JLabel("Bucket & Directories");
		lblBucketDirectories.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		
		bucketBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bucketBox.setMaximumRowCount(2);
		
		keyPath = new JTextField();
		keyPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				virtualKeyPath = keyPath.getText();
				
			}
		});
		keyPath.setEnabled(false);
		keyPath.setFont(new Font("Tahoma", Font.PLAIN, 18));
		keyPath.setText("Enter the Key path");
		keyPath.setColumns(100);
		
		
		GroupLayout gl_Bucket_Directories = new GroupLayout(Bucket_Directories);
		gl_Bucket_Directories.setHorizontalGroup(
			gl_Bucket_Directories.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Bucket_Directories.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_Bucket_Directories.createParallelGroup(Alignment.TRAILING)
						.addComponent(keyPath, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
						.addComponent(lblBucketDirectories, Alignment.LEADING)
						.addComponent(bucketBox, Alignment.LEADING, 0, 277, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_Bucket_Directories.setVerticalGroup(
			gl_Bucket_Directories.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Bucket_Directories.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblBucketDirectories)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(bucketBox, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addGap(54)
					.addComponent(keyPath, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(135, Short.MAX_VALUE))
		);
		Bucket_Directories.setLayout(gl_Bucket_Directories);
		
		JLabel lblNewLabel_1 = new JLabel("");
		lblNewLabel_1.setIcon(new ImageIcon(S3.class.getResource("/com/aws/resourse/314.png")));
		lblNewLabel_1.setBounds(331, 19, 314, 163);
		desktopPane.add(lblNewLabel_1);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "Option", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBackground(SystemColor.window);
		panel.setBounds(336, 186, 309, 256);
		desktopPane.add(panel);
		
		JButton btnRequestInv = new JButton("Request Inventory");
		btnRequestInv.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnNewButton = new JButton("View Log");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton btnNewButton_1 = new JButton("Check for update");
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JLabel lblEmailTheCronjob = new JLabel("Email the cron-job report");
		lblEmailTheCronjob.setFont(new Font("Tahoma", Font.BOLD, 15));
		
		txtAbcd = new JTextField();
		txtAbcd.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtAbcd.setText("abcd@abcd.com");
		txtAbcd.setColumns(10);
		
		JLabel lblThis = new JLabel("this panal is under construction");
		lblThis.setFont(new Font("Tahoma", Font.PLAIN, 11));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(13)
							.addComponent(lblEmailTheCronjob))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(btnRequestInv, GroupLayout.PREFERRED_SIZE, 279, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(3)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(2)
							.addComponent(txtAbcd, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(4)
							.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 280, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(72)
							.addComponent(lblThis)))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(3)
					.addComponent(btnRequestInv, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(5)
					.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addComponent(lblEmailTheCronjob)
					.addGap(5)
					.addComponent(txtAbcd, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblThis)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(180, 180, 180)), "Local directory to Back up", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel_1.setBackground(SystemColor.window);
		panel_1.setBounds(655, 17, 264, 419);
		desktopPane.add(panel_1);
		
		JButton btnSelectFolder = new JButton("Select Folder");
		btnSelectFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("choosertitle");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    chooser.setAcceptAllFileFilterUsed(false);

			    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			      
			     
			      txtFolderIsNot.setText(chooser.getSelectedFile().getAbsolutePath());
			      
			      
			    } else {
			    	txtFolderIsNot.setText("Please Select the folder");
			    }
			}
		});
		btnSelectFolder.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtFolderIsNot.setText("Folder is not selected");
			}
		});
		btnClear.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		txtFolderIsNot = new JTextField();
		txtFolderIsNot.setBackground(SystemColor.window);
		txtFolderIsNot.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtFolderIsNot.setText("Folder is not selected");
		txtFolderIsNot.setColumns(10);
		
		JLabel lblSyncAndBack = new JLabel("Sync and Back up status");
		lblSyncAndBack.setFont(new Font("Tahoma", Font.BOLD, 18));
		
		JButton btnManulSyncNow = new JButton("Manual Sync Now");
		
		
		//note : upload is done here 
		
		btnManulSyncNow.addActionListener(uplode);
		
		
		
		btnManulSyncNow.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnAutoSyncEvery = new JButton("Auto Sync every 1 hour");
		
		
		btnAutoSyncEvery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				uplode.actionPerformed(null);
				Timer myloop = new Timer(3600000, uplode);
				myloop.start();
				
			}
		});
		
		
		
		btnAutoSyncEvery.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblCornreportIsSend = new JLabel("Corn-report is send when Sync");
		lblCornreportIsSend.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JLabel lblTakesPlaceincluding = new JLabel("takes place (including failuer report)");
		lblTakesPlaceincluding.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(14)
					.addComponent(btnManulSyncNow, GroupLayout.PREFERRED_SIZE, 224, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(16, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(16)
					.addComponent(btnAutoSyncEvery, GroupLayout.PREFERRED_SIZE, 225, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(13, Short.MAX_VALUE))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(6)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
							.addComponent(lblCornreportIsSend)
							.addGap(23))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(13)
							.addComponent(lblTakesPlaceincluding)))
					.addGap(5))
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblSyncAndBack)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addComponent(txtFolderIsNot)
							.addGroup(gl_panel_1.createSequentialGroup()
								.addComponent(btnSelectFolder)
								.addGap(18)
								.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGap(20)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnSelectFolder)
						.addComponent(btnClear))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(txtFolderIsNot, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(lblSyncAndBack)
					.addGap(14)
					.addComponent(btnManulSyncNow, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(7)
					.addComponent(btnAutoSyncEvery, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addGap(81)
					.addComponent(lblCornreportIsSend)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblTakesPlaceincluding)
					.addContainerGap(22, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		JLabel lblCreatedAndDesigned = new JLabel("Created and designed by Sudhakar raj");
		lblCreatedAndDesigned.setForeground(new Color(255, 215, 0));
		lblCreatedAndDesigned.setHorizontalAlignment(SwingConstants.CENTER);
		lblCreatedAndDesigned.setBounds(335, 610, 481, 14);
		desktopPane.add(lblCreatedAndDesigned);
		contentPane.setLayout(gl_contentPane);
	}
	
	
	ActionListener uplode = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try {
				
				if((bucketName!=null)&&(virtualKeyPath!=null)){
				TransferManager tx = new TransferManager(Credentials);
				 
				System.out.println(bucketName+virtualKeyPath+new File(txtFolderIsNot.getText()).getName());
				
				MultipleFileUpload myUpload =  tx.uploadDirectory(bucketName, virtualKeyPath, new File(txtFolderIsNot.getText()+"/"), true);
				
				
				// You can poll your transfer's status to check its progress
				if (myUpload.isDone() == false) {
				       System.out.println("Transfer: " + myUpload.getDescription());
				       System.out.println("  - State: " + myUpload.getState());
				       System.out.println("  - Progress: "
				                       + myUpload.getProgress().getBytesTransferred());
				}
				 
				
				 
				// Or you can block the current thread and wait for your transfer to
				// to complete. If the transfer fails, this method will throw an
				// AmazonClientException or AmazonServiceException detailing the reason.
				myUpload.waitForCompletion();
				 
				// After the upload is complete, call shutdownNow to release the resources.
				tx.shutdownNow();
				
				Toolkit.getDefaultToolkit().beep();
				
				
				}else{
					JOptionPane.showMessageDialog(null, "Enter the key path");
				}
				
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
			}
			
		}
	};
	
	
}
