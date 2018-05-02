
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Disco implements Runnable{
    static LinkedBlockingQueue logs = new LinkedBlockingQueue();
    
    public Disco(){
    }
    
    @Override
    public void run(){
        try{
            DatagramPacket datagramPacket = (DatagramPacket)logs.poll();
            InetAddress IPAddress = datagramPacket.getAddress();
            int port = datagramPacket.getPort();
            String sentence = new String( datagramPacket.getData(), 0, datagramPacket.getLength() );
        
            Log l = new Log();
            l.setLog(IPAddress+":"+port+":"+sentence);
        } catch (IOException ex){
            Logger.getLogger(Disco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setQueue(DatagramPacket d){
        logs.offer(d);
    }
    
    public DatagramPacket getQueue(){
        return (DatagramPacket)logs.poll();
    }
}
