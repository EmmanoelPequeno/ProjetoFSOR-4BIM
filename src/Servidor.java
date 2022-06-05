import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements Runnable {
  private final ServerSocket serverSocket;
  private final ExecutorService pool;
  private final Dados dados;

  public Servidor(Dados dados) throws IOException {
    this.serverSocket = new ServerSocket(8000);
    this.pool = Executors.newFixedThreadPool(5);
    this.dados = dados;
  }

  public void run() {
    try {
      while (true) { 
        var socket = serverSocket.accept();
        pool.execute(new Conexao(socket, dados));
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}