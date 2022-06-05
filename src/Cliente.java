import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.function.Consumer;

public class Cliente {
  private final Entrada entrada;
  private final Socket socket;
  private final DataInputStream sockEntrada;
  private final DataOutputStream sockSaida;

  private Cliente(Entrada entrada, Socket socket) throws IOException {
    this.entrada = entrada;
    this.socket = socket;
    this.sockEntrada = new DataInputStream(socket.getInputStream());
    this.sockSaida = new DataOutputStream(socket.getOutputStream());
  }

  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar mesa", "Listar items", "Sair do programa"
  };

  private static final List<Consumer<Cliente>> FUNCS_OPCOES = List.of(
      Cliente::cadastrarMesa, Cliente::listarItems, Cliente::sairDoPrograma);

  public static void main(String[] args) {
    try (var entrada = new Entrada()) {
      var socket = new Socket("localhost", 8000);
      new Cliente(entrada, socket).rodar();
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  private void rodar() {
    System.out.println("Iniciando...");

    while (true) {
      System.out.println("\n== Opções disponíveis:");

      var qtdOpcoes = DESCRICOES_OPCOES.length;
      for (var i = 0; i < qtdOpcoes; i++) {
        System.out.printf("%d. %s\n", i + 1, DESCRICOES_OPCOES[i]);
      }

      var opcao = entrada.lerIndice("Escolha uma: ", qtdOpcoes);
      var func = FUNCS_OPCOES.get(opcao);

      func.accept(this);
      entrada.lerEnter("\nAperte Enter para continuar...");
    }
  }

  private void cadastrarMesa() {
    var codigo = entrada.lerInt("Digite o código da mesa: ");
    var nomeCliente = entrada.lerString("Digite o nome do cliente: ");
    
    try {
      this.sockSaida.writeByte(0);
      this.sockSaida.writeInt(codigo);
      this.sockSaida.writeUTF(nomeCliente);
      this.sockSaida.flush();

      var tipoRes = this.sockEntrada.readByte();
      if (tipoRes == 1) {
        System.out.println(this.sockEntrada.readUTF());
      }
    } catch (IOException e) {
      System.err.println("Ferrou"); 
    }
  }

  private void listarItems() {
    try {
      this.sockSaida.writeByte(1);
      this.sockSaida.flush();

      this.sockEntrada.readByte();
      var qtdItems = this.sockEntrada.readInt();
      
      for (var i = 0; i < qtdItems; i++) {
        System.out.println("Código: " + this.sockEntrada.readInt());
        System.out.println("Descrição: " + this.sockEntrada.readUTF());
        System.out.println("Preço: " + this.sockEntrada.readDouble());
      }
    } catch (Exception e) {
      System.err.println("Ferrou");
    }
  }

  private void adicionarItem() {
    var codigo = entrada.lerInt("Digite o código do item: ");

    try {
      this.sockSaida.writeByte(2);
      this.sockSaida.writeInt(codigo);
      this.sockSaida.flush();

      this.sockEntrada.readByte();
    } catch (Exception e) {
      System.err.println("Ferrou");
    }
  }

  private void fecharConta() {
    try {
      this.sockSaida.writeByte(3);
      this.sockSaida.flush();

      this.sockEntrada.readByte();

      System.out.println("Preço da conta: " + this.sockEntrada.readDouble());
      System.out.println("Hora de entrada: " + this.sockEntrada.readUTF());
      System.out.println("Hora de saída: " + this.sockEntrada.readUTF());      
    } catch (Exception e) {
      System.err.println("Ferrou");
    }
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    System.exit(0);
  }
}