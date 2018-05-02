
import java.net.DatagramPacket;
import java.util.concurrent.LinkedBlockingQueue;

public class Requisicao implements Runnable {
    private DatagramPacket receivePacket;
    static LinkedBlockingQueue requisicoes = new LinkedBlockingQueue();

    public Requisicao() {
    }
    
    public Requisicao(DatagramPacket receivePacket){
        this.receivePacket = receivePacket;
    }
    
    @Override
    public void run(){
        requisicoes.offer(receivePacket);
    }
    
    public void setQueue(DatagramPacket d){
        requisicoes.offer(d);
//        System.out.println(requisicoes);
    }
    
    public DatagramPacket getQueue(){
        return (DatagramPacket)requisicoes.poll();
    }

    public DatagramPacket getReceivePacket(){
        return receivePacket;
    }

    public void setReceivePacket(DatagramPacket receivePacket) {
        this.receivePacket = receivePacket;
    }
    
    public DatagramPacket getPeek(){
        return (DatagramPacket)requisicoes.peek();
    }
    
    public DatagramPacket getPoll(){
        return (DatagramPacket) requisicoes.poll();
    }
}
