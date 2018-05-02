
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;


public class Log {

    public Log() {
    }
    
    public void setLog(String linha) throws IOException{
            FileOutputStream file = new FileOutputStream(new File("src/log.txt"),true);
            linha = linha + "\n";
                byte[] contentInBytes = linha.getBytes();
                file.write(contentInBytes);

                file.flush();
//            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/log.txt"));
//            bufferedWriter.append(linha+"\n");
//            bufferedWriter.close();
        }
    
    public void getLog() throws IOException, InterruptedException{
        System.out.println("Recuperando log...");
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/log.txt"));
//      String linha = null;

//        String linha = bufferedReader.readLine();
//        while(true){
//        for(int i=0; i<bufferedReader.lines().count();i++){
          for (String linha = bufferedReader.readLine(); linha != null; linha = bufferedReader.readLine()){

                String dados[] = linha.split(":");
                InetAddress IPAddress = InetAddress.getByName(dados[0].replace("/", ""));
                int port = Integer.parseInt(dados[1]);
                String sentence = dados[2];
                byte[] sendData = sentence.getBytes();
                DatagramPacket dp = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                
                //Adiciona na f1(Requisições)
                Requisicao requisicao = new Requisicao();
                requisicao.setQueue(dp);
                
                System.out.println(linha);
          }
        bufferedReader.close();
        System.out.println("Log recuperado!");
    }
}
