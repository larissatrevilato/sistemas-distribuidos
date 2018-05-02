
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Processamento implements Runnable{
    DatagramSocket serverSocket;
    static LinkedBlockingQueue processamentos = new LinkedBlockingQueue();
    static Map<BigInteger, String> map = new HashMap();

    public Processamento() {
    }
    
    public Processamento(DatagramSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    
    public void setQueue(DatagramPacket d){
        processamentos.offer(d);
    }
    
    public DatagramPacket getQueue(){
        return (DatagramPacket)processamentos.poll();
    }
    
    @Override
    public void run(){
        System.out.println("Processamento size: "+processamentos.size());
        byte[] sendData;
        String response;
        while(processamentos.size() > 0){
            DatagramPacket receivePacket = (DatagramPacket)processamentos.poll();
            String request = new String(receivePacket.getData(), 0, receivePacket.getLength());
                DatagramPacket sendPacket;
                String dados[] = request.split(" ");
                switch( dados[0] ){
                  case "insert":
                      System.out.println("Processamento 46");
                      BigInteger chave = new BigInteger(dados[1]);
                      String valor = dados[2];
                      response = insert(chave, valor);
                      InetAddress IPAddress = receivePacket.getAddress();
                      int port = receivePacket.getPort();
                      sendData = response.getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            System.out.println("Server Socket 55");
                            serverSocket.send(sendPacket);
                            System.out.println("Server Socket 57");
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                  case "delete":
                      response = remove(new BigInteger(dados[1]));
                      IPAddress = receivePacket.getAddress();
                      port = receivePacket.getPort();
                      sendData = response.getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            serverSocket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                  case "update":
                      response = update(new BigInteger(dados[1]), dados[2]);
                      IPAddress = receivePacket.getAddress();
                      port = receivePacket.getPort();
                      sendData = response.getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            serverSocket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                  case "select":
                      String retorno = select(new BigInteger(dados[1]));
                      IPAddress = receivePacket.getAddress();
                      port = receivePacket.getPort();
                      sendData = retorno.getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            serverSocket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                   case "list":
                      IPAddress = receivePacket.getAddress();
                      port = receivePacket.getPort();
                      sendData = list().getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            serverSocket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                  default:
                      IPAddress = receivePacket.getAddress();
                      port = receivePacket.getPort();
                      sendData = "Comando não reconhecido".getBytes();
                      sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                      try {
                            serverSocket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(Processamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                      break;
                  }
        }
    }
    
    //Insere objeto no HashMap
    private String insert(BigInteger bigInteger, String s){
        if(map.get(bigInteger) == null){
            map.put(bigInteger, s);
            return "inserido com sucesso!!";
        }else{
            return "Indice existente!!";
        } 
    }

    //Atualiza objeto no HashMap
   private String update(BigInteger bigInteger, String s){
       if(map.get(bigInteger) != null){
            map.replace(bigInteger, s);
            return "Atualizado com sucesso!!";
        }else{
            return "Indice inexistente!!";
        } 
    }
    
    //Remove objeto no HashMap
    private String remove(BigInteger bigInteger){
         if(map.get(bigInteger) != null){
            map.remove(bigInteger);
            return "Removido com sucesso!!";
        }else{
            return "Indice inexistente!!";
        }  
    }

    //Seleciona objeto do HashMap
    private String select(BigInteger bigInteger){
        if(map.get(bigInteger) != null){
            return bigInteger.intValue()+" - "+ map.get(bigInteger) +"\n";
        }else{
            return "Indice inexistente!!";
        } 
    }

    //Lista objetos do HashMap
    private String list(){
        Set<BigInteger> set = map.keySet();
        String retorno = "";
        if(!set.isEmpty()){
            for(BigInteger chave: set){
                if(chave != null){
                    retorno += chave.intValue()+" - "+map.get(chave) +"\n";
                }
            }
        }else{
            retorno = "Lista vazia!!";
        }
        return retorno;
    }
}
