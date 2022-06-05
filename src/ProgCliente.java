import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ProgCliente implements AutoCloseable {
  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar mesa", "Listar itens", "Adicionar item", "Fechar conta", "Sair do programa"
  };

  private static final Opcao[] FUNCS_OPCOES = new Opcao[] {
      ProgCliente::cadastrarMesa, ProgCliente::listarItens, ProgCliente::adicionarItem, ProgCliente::fecharConta,
      ProgCliente::sairDoPrograma
  };

  private final Entrada entrada;
  private final Socket socket;
  private final DataInputStream sockEntrada;
  private final DataOutputStream sockSaida;
  private boolean continuar;

  public static void main(String[] args) {
    try (var cliente = new ProgCliente()) {
      cliente.run();
    } catch (IOException e) {
      System.err.println("Erro de conexão: " + e);
      System.exit(1);
    }
  }

  private ProgCliente() throws IOException {
    this.entrada = new Entrada();
    this.socket = new Socket("localhost", 8000);
    this.sockEntrada = new DataInputStream(this.socket.getInputStream());
    this.sockSaida = new DataOutputStream(this.socket.getOutputStream());
  }

  @Override
  public void close() throws IOException {
    this.entrada.close();
    this.sockEntrada.close();
    this.sockSaida.close();
    this.socket.close();
  }

  private void run() throws IOException {
    System.out.println("Iniciando...\n");
    this.continuar = true;

    while (true) {
      var funcao = this.entrada.escolherOpcao(DESCRICOES_OPCOES, FUNCS_OPCOES);
      funcao.accept(this);
      
      if (this.continuar) {
        this.entrada.aguardarEnter();
      } else {
        break;
      }
    }
  }

  private boolean checarErro() throws IOException {
    var ok = this.sockEntrada.readBoolean();

    if (!ok) {
      System.out.println(this.sockEntrada.readUTF());
    }

    return ok;
  }

  private void cadastrarMesa() throws IOException {
    var codigo = this.entrada.lerInt("Digite o código da mesa: ");
    var nomeCliente = this.entrada.lerString("Digite o nome do cliente: ");

    this.sockSaida.writeByte(0);
    this.sockSaida.writeInt(codigo);
    this.sockSaida.writeUTF(nomeCliente);
    this.sockSaida.flush();

    if (this.checarErro()) {
      System.out.println("Mesa cadastrada com sucesso");
    }
  }

  private void listarItens() throws IOException {
    this.sockSaida.writeByte(1);
    this.sockSaida.flush();

    if (!this.checarErro()) {
      return;
    }

    var qtdItens = this.sockEntrada.readInt();
    if (qtdItens == 0) {
      System.out.println("Sem itens disponíveis");
    }

    for (var i = 0; i < qtdItens; i++) {
      if (i != 0) {
        System.out.println();
      }

      System.out.println("Código: " + this.sockEntrada.readInt());
      System.out.println("Descrição: " + this.sockEntrada.readUTF());
      System.out.println("Preço: " + this.sockEntrada.readDouble());
    }
  }

  private void adicionarItem() throws IOException {
    var codigo = this.entrada.lerInt("Digite o código do item: ");

    this.sockSaida.writeByte(2);
    this.sockSaida.writeInt(codigo);
    this.sockSaida.flush();
    this.checarErro();
  }

  private void fecharConta() throws IOException {
    this.sockSaida.writeByte(3);
    this.sockSaida.flush();

    if (!this.checarErro()) {
      return;
    }

    System.out.println("Preço da conta: " + this.sockEntrada.readDouble());
    System.out.println("Horário de entrada: " + this.sockEntrada.readUTF());
    System.out.println("Horário de saída: " + this.sockEntrada.readUTF());
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    this.continuar = false;
  }

  @FunctionalInterface
  private interface Opcao {
    void accept(ProgCliente cliente) throws IOException;
  }
}