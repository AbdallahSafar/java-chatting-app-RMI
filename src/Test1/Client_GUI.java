package Test1;

import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTree;
import javax.swing.JScrollBar;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.JDesktopPane;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import java.awt.Color;

public class Client_GUI {

	private JFrame frmRmiProject;
	private JTextField textUserName;
	private JTextField textPassword;
	/**
	 * Launch the application.
	 */
	
	// Global Variables
	Boolean newUser = false;
	Boolean connected = false;
	Boolean newGrp = false;
	IServer serverRef;
    ClientImp client;
    private JTextField textMsg;
    private JTextField textIdGrp;
    private JTextField txtGroupMembers;
    private JTextField txtAddClient;
	
    public String clearString(String str) {
    	return str.split(" ")[0];
    }
    
    public Boolean arrayContains(String str,String[] arr) {
    	for(int i=0;i<arr.length;i++) {
    		if(arr[i].equals(str)) return true;
    	}
    	return false;
    }
    
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_GUI window = new Client_GUI();
					window.frmRmiProject.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client_GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmRmiProject = new JFrame();
		frmRmiProject.getContentPane().setBackground(new Color(192, 192, 192));
		frmRmiProject.setBackground(Color.LIGHT_GRAY);
		frmRmiProject.setTitle("RMI Project");
		frmRmiProject.setBounds(100, 100, 1108, 770);
		frmRmiProject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRmiProject.getContentPane().setLayout(null);
		
		final JButton btnBroadcast = new JButton("Send a Broadcast");
		btnBroadcast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String sender = textUserName.getText();
				String msg = textMsg.getText();
				try {
					serverRef.SendBroadcast(msg, sender);
					textMsg.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBroadcast.setBounds(263, 604, 179, 31);
		frmRmiProject.getContentPane().add(btnBroadcast);
	
		
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(41, 108, 401, 435);
		frmRmiProject.getContentPane().add(textArea);
		
		final JList listClients = new JList();
		listClients.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {		//This line prevents double events
					
					Object select = listClients.getSelectedValue();
					if(select != null) {
						String selected = select.toString();
								selected = clearString(selected);
								DefaultListModel DLM = new DefaultListModel();
								try {
									String[] listUsers = serverRef.listConnected().split("\\,|\\[|\\]");
									String newString;
									for(int i=1;i<listUsers.length;i++) {
										newString = listUsers[i].trim();
										if(!newString.equals(textUserName.getText())) DLM.addElement(newString);
									}
									listClients.setModel(DLM);
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
								try {
									textArea.setText("");
									serverRef.showChat(textUserName.getText(), selected);
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								
						        listClients.setSelectedValue(selected,false);
							}
					}
					
			}
		});
		listClients.setBounds(814, 170, 246, 372);
		frmRmiProject.getContentPane().add(listClients);
		
		final JPanel panelConnect = new JPanel();
		panelConnect.setBackground(new Color(192, 192, 192));
		panelConnect.setBounds(41, 10, 1019, 52);
		frmRmiProject.getContentPane().add(panelConnect);
		panelConnect.setLayout(null);
		
		final JButton btnSend = new JButton("Send to client");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String selected = listClients.getSelectedValue().toString();
				String msg = textMsg.getText().trim();
				textArea.append(">> " + textUserName.getText() + "\n" +msg + "\n");
				textMsg.setText("");
				try {
					serverRef.sendToClient(msg, selected,textUserName.getText());
					textMsg.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSend.setBounds(41, 604, 179, 31);
		frmRmiProject.getContentPane().add(btnSend);
		
		JLabel lblUserName = new JLabel("User name:");
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setBounds(21, 14, 77, 22);
		panelConnect.add(lblUserName);
		
		final JList listGrps = new JList();
		listGrps.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					Object select = listGrps.getSelectedValue();
					if(select != null) {
						String[] listUsers;
						try {
							listUsers = serverRef.listConnected().split("\\,|\\[|\\]");
							for(int k=0;k<listUsers.length;k++) {
								listUsers[k] = listUsers[k].trim();
							}
							String selected = select.toString();
							selected = clearString(selected);
							DefaultListModel DLM = new DefaultListModel();
							try {
								ArrayList<String> userGrps = serverRef.UserGrps(textUserName.getText());
								Boolean allMembersConnected = true;
								ArrayList grpsList = serverRef.UserGrps(textUserName.getText());
								System.out.println(grpsList);
								ArrayList grpMembers;	
								for(int i=0;i<grpsList.size();i++) {
									allMembersConnected = true;
									grpMembers = serverRef.GrpMembers(grpsList.get(i).toString());
									System.out.println(grpMembers);
									for(int j=0;j<grpMembers.size();j++) {
										System.out.println(arrayContains(grpMembers.get(j).toString(),listUsers));
										if(!arrayContains(grpMembers.get(j).toString(),listUsers)) allMembersConnected = false;
									}
									if(allMembersConnected) DLM.addElement(grpsList.get(i));
								}
								
//								for(int i=0;i<userGrps.size();i++) {
//									DLM.addElement(userGrps.get(i));
//								}
								listGrps.setModel(DLM);
								System.out.println("3");
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
								textArea.setText("");
								serverRef.showGrpChat(selected,textUserName.getText());
								System.out.println("4");
							} catch (RemoteException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
							try {
					        listGrps.setSelectedValue(selected,false);
					        System.out.println("5");
							}
							catch (Exception e2){
								
							}
						} catch (RemoteException e3) {
							// TODO Auto-generated catch block
							e3.printStackTrace();
						}
							}
				}
			}
		});
		listGrps.setBounds(551, 170, 246, 372);
		frmRmiProject.getContentPane().add(listGrps);
		
		final JButton btnAddClient = new JButton("Add client");
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selectedGrp = listGrps.getSelectedValue().toString();
				String newClient = txtAddClient.getText();
				try {
					serverRef.addClientToGrp(selectedGrp, newClient);
					JOptionPane.showMessageDialog(null, "You have successfully added " + newClient + " to group "+ selectedGrp);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAddClient.setBounds(692, 696, 105, 21);
		frmRmiProject.getContentPane().add(btnAddClient);
		
		final JButton btnLeave = new JButton("Leave group");
		btnLeave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String grpSelected = listGrps.getSelectedValue().toString();
				String idClient = textUserName.getText();
				try {
					serverRef.leaveGrp(grpSelected, idClient);
					DefaultListModel DLM = new DefaultListModel();
			        for(int i = 0; i< listGrps.getModel().getSize();i++) {
			        	if(!listGrps.getModel().getElementAt(i).toString().equals(grpSelected)) {
			        		DLM.addElement(listGrps.getModel().getElementAt(i).toString());
			        	}
			        }
			        listGrps.setModel(DLM);
			        JOptionPane.showMessageDialog(null, "You have successfully left the group: "+grpSelected);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLeave.setBounds(551, 614, 246, 21);
		frmRmiProject.getContentPane().add(btnLeave);
		
		textUserName = new JTextField();
		textUserName.setBounds(95, 10, 234, 31);
		panelConnect.add(textUserName);
		textUserName.setColumns(10);
		
		textPassword = new JTextField();
		textPassword.setBounds(506, 10, 247, 31);
		panelConnect.add(textPassword);
		textPassword.setColumns(10);
		
		final JLabel lblGrps = new JLabel("Groups");
		lblGrps.setForeground(new Color(255, 255, 255));
		lblGrps.setBounds(655, 147, 45, 13);
		frmRmiProject.getContentPane().add(lblGrps);
		
		final JLabel lblUsers = new JLabel("Users");
		lblUsers.setForeground(new Color(255, 255, 255));
		lblUsers.setBounds(919, 147, 45, 13);
		frmRmiProject.getContentPane().add(lblUsers);
		
		final JButton btnRefreshGrps = new JButton("Refresh");
		btnRefreshGrps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btnAddClient.setVisible(false);
					txtAddClient.setVisible(false);
					btnLeave.setVisible(false);
				String[] listUsers;
				listUsers = serverRef.listConnected().split("\\,|\\[|\\]");
				for(int k=0;k<listUsers.length;k++) {
					System.out.println(listUsers[k]);
					listUsers[k] = listUsers[k].trim();
					System.out.println(listUsers[k]);
				}
				System.out.println(serverRef.listConnected());
				Boolean allMembersConnected = true;
				DefaultListModel DLM1 = new DefaultListModel();
				ArrayList grpsList = serverRef.UserGrps(textUserName.getText());
				ArrayList grpMembers;
				for(int i=0;i<grpsList.size();i++) {
					allMembersConnected = true;
					grpMembers = serverRef.GrpMembers(grpsList.get(i).toString());
					System.out.println(grpMembers.size());
					for(int j=0;j<grpMembers.size();j++) {
						System.out.println(arrayContains(grpMembers.get(j).toString(),listUsers));
						if(!arrayContains(grpMembers.get(j).toString(),listUsers)) allMembersConnected = false;
					}
					if(allMembersConnected) DLM1.addElement(grpsList.get(i));
				}
				listGrps.setModel(DLM1);
				if(listGrps.getModel().getSize() > 0) {
					btnAddClient.setVisible(true);
					txtAddClient.setVisible(true);
					btnLeave.setVisible(true);
				}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefreshGrps.setBounds(551, 665, 246, 21);
		frmRmiProject.getContentPane().add(btnRefreshGrps);
		
		final JButton btnCreateGroup = new JButton("Create new group");
		btnCreateGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!newGrp) {
					textIdGrp.setText("Group name:");
					txtGroupMembers.setText("Group members:");
					textIdGrp.setVisible(true);
					txtGroupMembers.setVisible(true);
					newGrp = true;
				}
				else {
					String idGrp = textIdGrp.getText().trim();
					String GrpMembers = txtGroupMembers.getText().trim() + "-" + textUserName.getText();
					try {
						serverRef.createGroup(idGrp, GrpMembers);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					newGrp = false;
					textIdGrp.setVisible(false);
					txtGroupMembers.setVisible(false);
					
				}
			}
		});
		btnCreateGroup.setBounds(551, 640, 246, 21);
		frmRmiProject.getContentPane().add(btnCreateGroup);
		
		final JButton btnRefreshUsers = new JButton("Refresh");
		
		final JButton btnSendGrp = new JButton("Send to group");
		btnSendGrp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idGrp = listGrps.getSelectedValue().toString();
				String msg = textMsg.getText().trim();
				textArea.append(">> " + textUserName.getText() + "\n" +msg + "\n");
				try {
					serverRef.sendToGroup(msg, idGrp, textUserName.getText());
					textMsg.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSendGrp.setBounds(41, 645, 401, 31);
		frmRmiProject.getContentPane().add(btnSendGrp);
		
		final JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String userName = textUserName.getText().trim();
				String pass = textPassword.getText().trim();
				if(userName.isEmpty() || pass.isEmpty()) {
					JOptionPane.showMessageDialog(null, "User name and Password fields are required!");
					return;
				}
				if (serverRef == null) {
					
	                try {
						serverRef = (IServer) Naming.lookup("rmi://127.0.0.1:2000/serv");
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NotBoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
				if(newUser) {
					try {
						if(serverRef.getAllClients().contains(textUserName.getText())) {
							JOptionPane.showMessageDialog(null, "User name already taken! Try another one.");
						}
						else {
							serverRef.addNewUser(userName,pass,client);
							serverRef.reconnect(userName, client);
							connected = true;
							JOptionPane.showMessageDialog(null, "Operation succeeded! You can start to communicate.");
						}
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					try {
						if(serverRef.getAllClients().contains(userName)) {
							if(serverRef.verifyPass(userName,pass)) {
								serverRef.reconnect(userName, client);
								connected = true;
								System.out.println("Connected");
								JOptionPane.showMessageDialog(null, "Operation succeeded! You can start to communicate.");
							}
							else {
								JOptionPane.showMessageDialog(null, "Incorrect Password!");
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "User name not found!");
						}	
					}catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(connected) {
					
					textUserName.setEnabled(false);
					textPassword.setEnabled(false);
					btnConnect.setEnabled(false);
					btnSend.setVisible(true);
					textMsg.setVisible(true);
					btnBroadcast.setVisible(true);
					btnRefreshUsers.setVisible(true);
					listGrps.setVisible(true);
					lblGrps.setVisible(true);
					lblUsers.setVisible(true);
					btnCreateGroup.setVisible(true);
					btnRefreshGrps.setVisible(true);
					btnSendGrp.setVisible(true);
					DefaultListModel DLM = new DefaultListModel();
					try {
						String[] listUsers = serverRef.listConnected().split("\\,|\\[|\\]");
						String newString;
						for(int i=1;i<listUsers.length;i++) {
							newString = listUsers[i].trim();
							if(!newString.equals(userName)) DLM.addElement(newString);
						}
						listClients.setModel(DLM);
						
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		btnConnect.setBounds(860, 11, 149, 31);
		panelConnect.add(btnConnect);
		
		
		
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(new Color(255, 255, 255));
		lblPassword.setBounds(434, 14, 77, 22);
		panelConnect.add(lblPassword);
		
		final JPanel panelCreateAlreadyUser = new JPanel();
		panelCreateAlreadyUser.setBackground(new Color(192, 192, 192));
		panelCreateAlreadyUser.setBounds(333, 296, 387, 89);
		frmRmiProject.getContentPane().add(panelCreateAlreadyUser);
		panelCreateAlreadyUser.setLayout(null);
		
		JButton btnCreateUser = new JButton("Create new user");
		btnCreateUser.setBounds(10, 10, 151, 68);
		panelCreateAlreadyUser.add(btnCreateUser);
		
		JButton btnAlreadyUser = new JButton("Already a user");
		btnAlreadyUser.setBounds(226, 10, 151, 68);
		panelCreateAlreadyUser.add(btnAlreadyUser);
		
		
		btnAlreadyUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelConnect.setVisible(true);
				panelCreateAlreadyUser.setVisible(false);
				textArea.setVisible(true);
				listClients.setVisible(true);
				listGrps.setVisible(true);
			}
		});
		btnCreateUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelConnect.setVisible(true);
				panelCreateAlreadyUser.setVisible(false);
				textArea.setVisible(true);
				listClients.setVisible(true);
				listGrps.setVisible(true);
				newUser = true;
			}
		});
		
		try {
			client = new ClientImp(textArea,listClients,listGrps);
		} catch (RemoteException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		textMsg = new JTextField();
		textMsg.setBounds(41, 563, 401, 31);
		frmRmiProject.getContentPane().add(textMsg);
		textMsg.setColumns(10);
		
		
		btnRefreshUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel DLM = new DefaultListModel();
				try {
					String[] listUsers = serverRef.listConnected().split("\\,|\\[|\\]");
					String newString;
					for(int i=1;i<listUsers.length;i++) {
						newString = listUsers[i].trim();
						if(!newString.equals(textUserName.getText())) DLM.addElement(newString);
					}
					listClients.setModel(DLM);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnRefreshUsers.setBounds(814, 650, 246, 21);
		frmRmiProject.getContentPane().add(btnRefreshUsers);
		
		
		
		
		
		textIdGrp = new JTextField();
		textIdGrp.setHorizontalAlignment(SwingConstants.CENTER);
		textIdGrp.setText("Group name:");
		textIdGrp.setBounds(590, 552, 170, 19);
		frmRmiProject.getContentPane().add(textIdGrp);
		textIdGrp.setColumns(10);
		
		txtGroupMembers = new JTextField();
		txtGroupMembers.setHorizontalAlignment(SwingConstants.CENTER);
		txtGroupMembers.setText("Group members:");
		txtGroupMembers.setBounds(551, 581, 246, 19);
		frmRmiProject.getContentPane().add(txtGroupMembers);
		txtGroupMembers.setColumns(10);
		
		
		txtAddClient = new JTextField();
		txtAddClient.setHorizontalAlignment(SwingConstants.CENTER);
		txtAddClient.setText("Client:");
		txtAddClient.setBounds(551, 697, 131, 19);
		frmRmiProject.getContentPane().add(txtAddClient);
		txtAddClient.setColumns(10);
		
		
		panelConnect.setVisible(false);
		textArea.setVisible(false);
		listClients.setVisible(false);
		btnSend.setVisible(false);
		textMsg.setVisible(false);
		btnBroadcast.setVisible(false);
		btnRefreshUsers.setVisible(false);
		listGrps.setVisible(false);
		lblGrps.setVisible(false);
		lblUsers.setVisible(false);
		textIdGrp.setVisible(false);
		txtGroupMembers.setVisible(false);
		btnCreateGroup.setVisible(false);
		btnRefreshGrps.setVisible(false);
		btnSendGrp.setVisible(false);
		txtAddClient.setVisible(false);
		btnAddClient.setVisible(false);
		btnLeave.setVisible(false);
		
		
		
		frmRmiProject.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(connected)
					try {
						System.out.println("Disconnected");
						serverRef.disconnect(textUserName.getText());
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		
	}
}
