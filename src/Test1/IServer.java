package Test1;

import java.lang.reflect.Array;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IServer extends Remote {
	public void reconnect(String id, IClient client) throws RemoteException;
    public void disconnect(String id) throws RemoteException;
    public void sendToClient(String msg, String idClient, String idSender) throws RemoteException;
    public void sendToGroup(String msg, String idGroup, String idSender) throws RemoteException;
    public String listConnected() throws RemoteException;
    public void subscribe(String id) throws RemoteException;
    public void createGroup(String idGroup, String idMembers) throws RemoteException;
    public String getAllClients() throws RemoteException;
    public void addNewUser(String userName, String password, IClient client) throws RemoteException;
	public Boolean verifyPass(String user, String password) throws RemoteException;
	public void showChat(String idSender, String idReceiver) throws RemoteException;
	public void SendBroadcast(String msg,String idSender) throws RemoteException;
	public ArrayList GrpMembers(String idGrp) throws RemoteException;
	public ArrayList UserGrps(String idUser) throws RemoteException;
	public void showGrpChat(String idGrp, String idClient) throws RemoteException;
	public void leaveGrp(String idGrp,String idClient) throws RemoteException;
	public void addClientToGrp(String idGrp, String idClient) throws RemoteException;
}
