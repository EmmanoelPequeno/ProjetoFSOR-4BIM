import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
  private final ServerSocket serverSocket;
  private final ExecutorService pool;

  public static void main(String[] args) {
    try {
      new Servidor().rodar();
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public Servidor() throws IOException {
    this.serverSocket = new ServerSocket(8000);
    this.pool = Executors.newFixedThreadPool(5);
  }

  public void rodar() {
    try {
      while (true) {
        System.out.println("Iniciou!");
        var socket = serverSocket.accept();
        System.out.println("Aceitou!");
        pool.execute(new Conexao(socket));
      }
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}