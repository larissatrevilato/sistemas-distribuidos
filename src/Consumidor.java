
import java.net.DatagramPacket;


public class Consumidor implements Runnable{
    
    public Consumidor(){
    }
    
    @Override
    public void run(){
        Requisicao r = new Requisicao();
        DatagramPacket d = r.getQueue();
        
        //Adicionar em f2(Disco)
        Disco disco = new Disco();
        disco.setQueue(d);
        
        //Adicionar em f2(Processamento)
        Processamento processamento = new Processamento();
        processamento.setQueue(d);
    }   
    
}
