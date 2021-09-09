package Test1;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote{
	public void notifier(String idSender,Boolean GrpChat) throws RemoteException;
	public void showMsgs(String msg,String idSender,String broad) throws RemoteException;
	public void addGrpToClient(String idGrp) throws RemoteException;
}
