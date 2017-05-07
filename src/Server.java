import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by furkansahin on 02/05/2017.
 */
public interface Server extends Remote {
    public abstract String match(String name, int timeoutSecs)
            throws RemoteException;
}
