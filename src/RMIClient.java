import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by furkansahin on 02/05/2017.
 */
public class RMIClient  extends UnicastRemoteObject implements Client {
    static String peerName;
    static Object lock;
    static boolean handshaked;
    static ConcurrentHashMap<String, Boolean> registrationList;
    static String name;
    protected RMIClient() throws RemoteException {
    }

    public static void main(String args[]){
        try {
            registrationList = new ConcurrentHashMap<>();
            handshaked = false;
            lock = new Object();

            name = args[0];
            int timeout = Integer.parseInt(args[1]);
            System.out.println("My name is: " + name);

            String host = "host";
            Registry registry = LocateRegistry.getRegistry(2020);
            Client myself = new RMIClient();
            Server fObject = (Server) registry.lookup(host);
            registry.rebind(name, myself);

            peerName = fObject.match(name, timeout);
            if (peerName!=null) {
                Client peerClient = (Client) registry.lookup(peerName);
                System.out.println("my peer's name is: " + peerName);

                if (peerName.compareTo(name) > 0) {
                    handshaked = true;
                    peerClient.handshake(name);
                }
            }
            else {
                registry.unbind(name);
                System.exit(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handshake(String name) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(2020);
        if (name.equals(peerName) && !handshaked)
        {
            System.out.println("My peer sent me selaminaleykum");
            Client peer = null;

            try {
                peer = (Client) registry.lookup(peerName);
                UnicastRemoteObject.unexportObject(this,true);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
            peer.handshake(name);
        }
        else if(handshaked) {
            System.out.println("I got aleykumselam from my peer.");
            try {
                registry.unbind(this.name);
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }
}
