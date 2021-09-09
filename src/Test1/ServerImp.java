package Test1;

import java.awt.List;
import java.lang.reflect.Array;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ServerImp extends UnicastRemoteObject implements IServer  {
	LinkedHashMap<String, IClient> listConnected = new LinkedHashMap();
    HashMap<String, String> offLineMsg = new HashMap();
    
    LinkedHashMap<String, IClient> usersList = new LinkedHashMap();
    LinkedHashMap<String, String> namePassList = new LinkedHashMap();
    ArrayList<String> usersMsgs = new ArrayList();
    HashMap<String, ArrayList<String>> grps = new HashMap();
    ArrayList<String> grpsMsgs = new ArrayList();

    public ServerImp() throws RemoteException {

    }

    public void reconnect(String id, IClient client) throws RemoteException {
        listConnected.put(id, client);
        System.out.println("connect "+id);
    }

    public void disconnect(String id) throws RemoteException {
        listConnected.remove(id);
    }

    public void addNewUser(String userName, String password, IClient client) throws RemoteException {
    	namePassList.put(userName, password);
    	usersList.put(userName, client);
    }
    
    public String listConnected() throws RemoteException {
        return listConnected.keySet().toString();
    }

    public void showChat(String idSender, String idReceiver) throws RemoteException {
    	String[] item;
    	IClient cl = listConnected.get(idSender);
    	for(int i=0;i<usersMsgs.size();i++) {
    		item = usersMsgs.get(i).split("-");
    		if(item[0].equals(idSender) && item[1].equals(idReceiver)) {
    			cl.showMsgs(item[3], idSender,item[2]);
    			System.out.println(item[2] + item[3]);
    		}
    		if(item[0].equals(idReceiver) && item[1].equals(idSender)) {
    			cl.showMsgs(item[3], idReceiver,item[2]);
    			System.out.println(item[2] + item[3]);
    		}
    	}
    	
    }
    
    
	public void sendToClient(String msg, String idClient, String idSender) throws RemoteException {
		IClient cl = listConnected.get(idClient);
		String senderReceiver = idSender + "-" + idClient + "-0";
        if (cl != null) {
            // client online
            cl.notifier(idSender,false);
            usersMsgs.add(senderReceiver + "-" + msg);
            System.out.println("send "+idClient);
        } else {
            //client offline
            String oldmsg = offLineMsg.get(idClient);
            if (oldmsg == null) {
                oldmsg = "";
            }
            oldmsg += "'" + msg;
            offLineMsg.put(idClient, oldmsg);
            System.out.println("put "+idClient);
        }
		
	}
	
	public void SendBroadcast(String msg,String idSender) throws RemoteException {
		String senderReceiver;
		for (IClient client : listConnected.values()) {
			senderReceiver = idSender + "-" +getKey(listConnected,client) + "-1" ;
			client.notifier(idSender,false);
            usersMsgs.add(senderReceiver + "-" + msg);
            System.out.println("send "+getKey(listConnected,client));
		}
	}

	
	public static <K, V> K getKey(Map<K, V> map, V value)
    {
        for (Map.Entry<K, V> entry: map.entrySet())
        {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

	
	public void subscribe(String id) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
	public void createGroup(String idGroup, String idMembers) throws RemoteException {
		String[] members = idMembers.split("-");
		IClient cl;
		ArrayList<String> arr = new ArrayList();
		for(int i=0;i<members.length;i++) {
			arr.add(members[i]);
			cl=listConnected.get(members[i]);
			cl.addGrpToClient(idGroup);
		}
		grps.put(idGroup, arr);
		
	}

	public ArrayList GrpMembers(String idGrp) throws RemoteException {
		return grps.get(idGrp);
	}
	
	public ArrayList UserGrps(String idUser) throws RemoteException {
		ArrayList<String> arr;
		ArrayList<String> userGrps = new ArrayList();
		for (String key : grps.keySet()) {
			arr = grps.get(key);
			if(arr.contains(idUser)) userGrps.add(key);
		}
		return userGrps;
	}
	
	public String getAllClients() throws RemoteException{
		return usersList.keySet().toString();
	}
	
	
	public Boolean verifyPass(String user, String password) throws RemoteException {
		if(namePassList.get(user).equals(password)) return true;
		else return false;
	}
	

	public void sendToGroup(String msg, String idGroup, String idSender) throws RemoteException {
		ArrayList<String> grpMembers= grps.get(idGroup);
		ArrayList<IClient> members = new ArrayList();
		for(int i=0; i<grpMembers.size();i++) {
			if(!grpMembers.get(i).equals(idSender)) members.add(listConnected.get(grpMembers.get(i)));
		}
		for(int i=0; i<members.size();i++) {
			members.get(i).notifier(idGroup, true);
		}
		grpsMsgs.add(idGroup + "-" + idSender + "-" + msg);
	}
	
	public void leaveGrp(String idGrp,String idClient) throws RemoteException {
		ArrayList<String> members = grps.get(idGrp);
		ArrayList<String> newMembers = new ArrayList();
		for(int i=0;i<members.size();i++) {
			if(!idClient.equals(members.get(i))) newMembers.add(members.get(i));
		}
		grps.put(idGrp, newMembers);
	}
	
	public void addClientToGrp(String idGrp, String idClient) throws RemoteException {
		ArrayList<String> members = grps.get(idGrp);
		members.add(idClient);
		grps.put(idGrp, members);
	}
	
	public void showGrpChat(String idGrp, String idClient) throws RemoteException {
		String[] item;
		IClient cl = listConnected.get(idClient);
		for(int i=0;i<grpsMsgs.size();i++) {
    		item = grpsMsgs.get(i).split("-");
    		if(item[0].equals(idGrp)) {
    			cl.showMsgs(item[2], item[1],"0");
    		}
		}
	}
}
