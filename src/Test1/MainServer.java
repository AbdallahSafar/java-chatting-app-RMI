package Test1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class MainServer {

	public static void main(String[] args) throws RemoteException {
		IServer server = (IServer) new ServerImp();
		
            try {
				Naming.rebind("rmi://127.0.0.1:2000/serv", server);
			} catch (RemoteException | MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
