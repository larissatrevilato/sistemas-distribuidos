
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Properties;

public class Server {
    public static void main(String[] args) throws FileNotFoundException, IOException, InterruptedException {
        Properties p = new Properties();
        FileInputStream inputStream = new FileInputStream("src/propriedades.properties");
        p.load(inputStream);
        DatagramSocket serverSocket = new DatagramSocket(Integer.parseInt(p.getProperty("porta_server")));
        byte[] receiveData = new byte[1400];
        
        Log l = new Log();
        l.getLog();
        
        while(true){
            System.out.println("Server 21");
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println(request);
            
            //Thread para colocar em f1(Requisições)
            System.out.println("Server 21");
            Requisicao requisicao = new Requisicao(receivePacket);
            Thread t1 = new Thread(requisicao);
            t1.start();
            t1.join();
            
            //Thread consumindo de f1(Requisições) para colocar em f2(Disco) e f3(Processamento)
            Consumidor consumidor = new Consumidor();
            Thread t2 = new Thread(consumidor);
            t2.start();
            t2.join();
            
            //Thread consumindo de f2(Disco) e adicionando em disco
            Disco disco = new Disco();
            Thread t3 = new Thread(disco);
            t3.start();
      
            //Thread consumindo de f3(Processamento) e processando as requisições
            System.out.println("Server 46");
            Processamento processamento = new Processamento(serverSocket);
            Thread t4 = new Thread(processamento);
            t4.start();
            
            /*
            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            byte[] sendData = "Mensagem recebida...".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
            */
        }
    }
}

