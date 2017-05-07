import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by furkansahin on 02/05/2017.
 */
public interface Client extends Remote {
    public abstract void handshake(String name)
            throws RemoteException;
}
