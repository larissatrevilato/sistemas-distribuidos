import java.io.*;
import java.net.*;
import java.util.Properties;

class Client{
   public static void main(String args[]) throws Exception{
        Properties p = new Properties();
        FileInputStream in = new FileInputStream("src/propriedades.properties");
        p.load(in);
        
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(p.getProperty("ip_server"));
        Integer porta = Integer.parseInt(p.getProperty("porta_server"));

        ClientSend send = new ClientSend(IPAddress, porta, clientSocket);
        Thread t1 = new Thread(send);
        t1.start();
        
        ClientReceive receive = new ClientReceive(clientSocket);
        Thread t2 = new Thread(receive);
        t2.start();
   }
}
