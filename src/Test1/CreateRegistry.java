package Test1;

import java.lang.System.Logger.Level;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;
import java.util.logging.Logger;

public class CreateRegistry {

	public static void main(String[] args) {
            // TODO code application logic here
            int port = 2000;

            try {
				LocateRegistry.createRegistry(port);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            Scanner key = new Scanner(System.in);
            key.nextLine();

	}

}
