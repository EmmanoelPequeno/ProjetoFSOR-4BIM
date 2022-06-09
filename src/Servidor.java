import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor implements AutoCloseable, Runnable {
  private final ServerSocket socketServidor;
  private final ExecutorService threadPool;
  private final Dados dados;

  public Servidor(Dados dados) throws IOException {
    this.socketServidor = new ServerSocket(8000);
    this.threadPool = Executors.newFixedThreadPool(5);
    this.dados = dados;
  }

  @Override
  public void close() throws IOException {
    this.socketServidor.close();
  }

  @Override
  public void run() {
    var conexoes = new WeakHashMap<ConexaoCliente, Void>();

    try {
      while (true) {
        var socket = this.socketServidor.accept();
        var conexao = new ConexaoCliente(socket, this.dados);
        conexoes.put(conexao, null);
        this.threadPool.execute(conexao);
      }
    } catch (SocketException ignored) {
    } catch (IOException e) {
      System.err.println("Erro de conexão:" + e.getMessage());
    }

    this.threadPool.shutdownNow();

    for (var conexao : conexoes.keySet()) {
      conexao.close();
    }
  }
}