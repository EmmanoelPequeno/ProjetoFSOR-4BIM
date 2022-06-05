import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.time.LocalTime;

public class Conexao implements Runnable {
  private final Socket socket;
  private final Dados dados;
  private boolean continuar;
  private Mesa mesa;

  public Conexao(Socket socket, Dados dados) {
    this.socket = socket;
    this.dados = dados;
    this.continuar = true;
  }

  public void run() {
    try {
      var in = new DataInputStream(socket.getInputStream());
      var out = new DataOutputStream(socket.getOutputStream());

     while (this.continuar) {
        this.processar(in, out);
        out.flush();
      }

      socket.close();
    } catch (IOException e) {
      System.err.println(e);
    }
  }

  public void processar(DataInputStream in, DataOutputStream out) throws IOException {
    var tipoReq = in.readByte();
    List<Item> items;

    switch (tipoReq) {
      case 0:
        var codigoMesa = in.readInt();
        var nomeCliente = in.readLine();
        this.mesa = new Mesa(codigoMesa, nomeCliente);
        this.dados.getMesas().add(this.mesa);
        out.writeByte(0);
        break;

      case 1:
        items = this.dados.getItems();
        out.writeByte(0);
        out.writeInt(items.size());
        
        for (var item : items) {
          out.writeInt(item.getCodigo());
          out.writeUTF(item.getDescricao());
          out.writeDouble(item.getPreco());
        }

        break;

      case 2:
        var codigoItem = in.readInt();
        items = this.dados.getItems();

        for (var item : items) {
          if (item.getCodigo() == codigoItem) {
            this.mesa.adicionarTotalConta(item.getPreco());
            break;
          }
        }

        out.writeByte(0);
        break;

      case 3:
        var horaFim = LocalTime.now();
        this.mesa.setHoraFim(horaFim);
        this.continuar = false;
        
        out.writeByte(0);
        out.writeDouble(this.mesa.getTotalConta());
        out.writeUTF(this.mesa.getHoraInicio().toString());
        out.writeUTF(horaFim.toString());
        break;

      default:
        out.writeByte(1);
        break;
    }

    out.flush();
  }
}