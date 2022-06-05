import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements Runnable {
  private final ServerSocket socketServidor;
  private final ExecutorService threadPool;
  private final Dados dados;

  public Servidor(Dados dados) throws IOException {
    this.socketServidor = new ServerSocket(8000);
    this.threadPool = Executors.newFixedThreadPool(5);
    this.dados = dados;
  }

  public void run() {
    try {
      while (true) {
        var socket = this.socketServidor.accept();
        this.threadPool.execute(new Conexao(socket, this.dados));
      }
    } catch (IOException e) {
      System.err.println("Erro de conexão:" + e);
    }
  }
}