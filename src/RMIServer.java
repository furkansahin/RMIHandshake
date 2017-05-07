import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by furkansahin on 02/05/2017.
 */
public class RMIServer extends UnicastRemoteObject implements Server{
    static PriorityBlockingQueue<Pair> clients;
    static Object lock;

    RMIServer() throws RemoteException {
    }

    public static void main(String args[]){
        clients = new PriorityBlockingQueue<>(2,   (o1, o2) -> {if (o1.time > o2.time)
                                                                                return 1;
                                                                            else if (o1.time < o2.time)
                                                                                return -1;
                                                                            return 0;
                                                                            });
        lock = new Object();
        try
        {
            Registry reg = LocateRegistry.createRegistry(2020);
            Server host = new RMIServer();
            reg.rebind("host", host);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
    @Override
    public String match(String name, int timeoutSecs) throws RemoteException {
        synchronized (lock) {
            long curTime = System.currentTimeMillis();

            if (clients.size() == 0) {
                clients.add(new Pair(name, timeoutSecs * 1000 + curTime));
                try {
                    lock.wait(timeoutSecs * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!clients.isEmpty() && clients.peek().time <= System.currentTimeMillis()) {
                    clients.clear();
                    lock.notify();
                    return null;
                }
                if (!clients.isEmpty()) {
                    Pair toBeReturned = clients.poll();
                    String returnName = toBeReturned.name;
                    clients.clear();
                    lock.notify();
                    return returnName;
                }
            } else {
                Pair client = clients.poll();
                clients.add(new Pair(name, curTime + timeoutSecs*1000));
                lock.notify();
                return client.name;
            }
            return null;
        }
    }

    public class Pair{
        public String name;
        public long time;
        public Pair(String name, long time)
        {
            this.name = name;
            this.time = time;
        }
    }
}
