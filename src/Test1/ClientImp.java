package Test1;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class ClientImp extends UnicastRemoteObject implements IClient{
	JTextArea area;
	JList listUsers, listGrps;
//	String idSender;
    public ClientImp(JTextArea area,JList listUsers, JList listGrps) throws RemoteException
    {
        this.area = area;
        this.listUsers = listUsers;
        this.listGrps = listGrps;
    }

    public void addGrpToClient(String idGrp) throws RemoteException {
    	DefaultListModel DLM = new DefaultListModel();
        for(int i = 0; i< listGrps.getModel().getSize();i++){
        	DLM.addElement(listGrps.getModel().getElementAt(i).toString());
        }
        DLM.addElement(idGrp);
        listGrps.setModel(DLM);
    }
    
    public void notifier(String idSender,Boolean GrpChat) throws RemoteException {
//        area.append("\n"+msg);
    	JList list;
    	if(!GrpChat) {
//    		System.out.println(idSender);
    		list = listUsers;
    		
    	}
    	else {
    		list = listGrps;
    	}
    	
            DefaultListModel DLM = new DefaultListModel();
            for(int i = 0; i< list.getModel().getSize();i++){
            	String[] user = list.getModel().getElementAt(i).toString().split(" ");
                System.out.println(user[0]);
                if(user.length > 1) {
                	int msgsNmbr = Integer.parseInt(user[1]);
                	if(user[0].equals(idSender)) {
                		msgsNmbr++;
                    	DLM.addElement(user[0] + " " + msgsNmbr);
                    }
                	else {
                		DLM.addElement(user[0] + " " + msgsNmbr);
                	}
                }
                else {
                	if(user[0].equals(idSender)) {
                		DLM.addElement(user[0] + " 1" );
                	}
                	else {
                		DLM.addElement(user[0]);
                	}
                }
            }
            
            list.setModel(DLM);
    }
    
    public void showMsgs(String msg,String idSender,String broad) throws RemoteException {
    	if(broad.equals("0")) {
    	area.append(">> " + idSender + "\n" +msg + "\n");
    	}
    	else {
    		area.append(">> " + idSender + " (BROADCAST)" + "\n" +msg + "\n");
    	}
    }
    
}
