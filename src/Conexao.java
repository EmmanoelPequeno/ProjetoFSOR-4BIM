import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalTime;

public class Conexao implements Runnable {
  private static final Requisicao[] FUNCOES_REQUISICOES = new Requisicao[] {
      Conexao::cadastrarItem, Conexao::listarItens, Conexao::adicionarItem, Conexao::fecharConta
  };

  private final Socket socket;
  private final Dados dados;
  private DataInputStream sockEntrada;
  private DataOutputStream sockSaida;
  private Mesa mesa;
  private boolean continuar;

  public Conexao(Socket socket, Dados dados) {
    this.socket = socket;
    this.dados = dados;
    this.continuar = true;
  }

  public void run() {
    try {
      this.sockEntrada = new DataInputStream(this.socket.getInputStream());
      this.sockSaida = new DataOutputStream(this.socket.getOutputStream());

      while (this.continuar) {
        var tipoReq = this.sockEntrada.readByte();

        if (tipoReq >= 0 && tipoReq < FUNCOES_REQUISICOES.length) {
          FUNCOES_REQUISICOES[tipoReq].accept(this);
        } else {
          this.sockSaida.writeBoolean(false);
          this.sockSaida.writeUTF("Tipo de requisição inexistente");
        }

        this.sockSaida.flush();
      }

      this.socket.close();
    } catch (EOFException ignored) {
    } catch (IOException e) {
      System.err.println("Erro de conexão: " + e);
    }
  }

  private void cadastrarItem() throws IOException {
    var codigoMesa = this.sockEntrada.readInt();
    var nomeCliente = this.sockEntrada.readUTF();

    this.mesa = new Mesa(codigoMesa, nomeCliente);
    this.dados.getMesas().add(this.mesa);
    this.sockSaida.writeBoolean(true);
  }

  private void listarItens() throws IOException {
    var itens = this.dados.getItens();
    this.sockSaida.writeBoolean(true);
    this.sockSaida.writeInt(itens.size());

    for (var item : itens) {
      this.sockSaida.writeInt(item.getCodigo());
      this.sockSaida.writeUTF(item.getDescricao());
      this.sockSaida.writeDouble(item.getPreco());
    }
  }

  private void adicionarItem() throws IOException {
    var codigoItem = this.sockEntrada.readInt();
    var itens = this.dados.getItens();

    for (var item : itens) {
      if (item.getCodigo() == codigoItem) {
        this.mesa.adicionarTotalConta(item.getPreco());
        break;
      }
    }

    this.sockSaida.writeBoolean(true);
  }

  private void fecharConta() throws IOException {
    this.continuar = false;

    var horaFim = LocalTime.now();
    this.mesa.setHoraFim(horaFim);

    this.sockSaida.writeBoolean(true);
    this.sockSaida.writeDouble(this.mesa.getTotalConta());
    this.sockSaida.writeUTF(this.mesa.getHoraInicio().toString());
    this.sockSaida.writeUTF(horaFim.toString());
  }

  @FunctionalInterface
  private interface Requisicao {
    void accept(Conexao conexao) throws IOException;
  }
}