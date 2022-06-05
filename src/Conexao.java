import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Conexao implements Runnable {
  private final Socket socket;

  public Conexao(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      var entrada = new DataInputStream(socket.getInputStream());
      var saida = new DataInputStream(socket.getOutputStream());

      

      saida.flush();
      socket.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }
}