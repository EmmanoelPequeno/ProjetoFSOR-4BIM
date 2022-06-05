import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class Main {
  private int codigoAtual;
  private final Entrada entrada;
  private final Dados dados;

  private Main(Entrada entrada, Dados dados) {
    this.entrada = entrada;
    this.dados = dados;
  }

  private static final String[] DESCRICOES_OPCOES = new String[] {
    "Cadastrar item", "Listar mesas", "Sair do programa"
  };

  private static final List<Consumer<Main>> FUNCS_OPCOES = List.of(
      Main::cadastrarItem, Main::listarMesas, Main::sairDoPrograma);

  public static void main(String[] args) {
    try (var entrada = new Entrada()) {
      var dados = new Dados();
      new Thread(new Servidor(dados)).run();
      new Main(entrada, dados).rodar();
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

  private void cadastrarItem() {
    var descricao = entrada.lerString("Digite a descrição do item: ");
    var preco = entrada.lerDoubleValidar("Digite o preço de venda do item: ");
    var qtd = entrada.lerInt("Digite a quantidade inicial do item: ");
    var item = new Item(codigoAtual++, descricao, preco, qtd);

    this.dados.getItems().add(item);
    System.out.println("Item cadastrado");
  }

  private void listarMesas() {
    for (var mesa : this.dados.getMesas()) {
      System.out.printf("== Mesa %d\n", mesa.getCodigo());
      System.out.printf("Nome do cliente: %s\n", mesa.getNomeCliente());
      System.out.printf("Total da mesa: R$ %.2f\n", mesa.getTotalConta());
      System.out.printf("Horário de início da mesa: %s\n", mesa.getHoraInicio());

      if (mesa.getHoraFim() == null) {
        System.out.println("Ainda em atendimento");
      } else {
        System.out.printf("Horário de fim da mesa: %s\n", mesa.getHoraFim());
      }

      System.out.println();
    }
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    System.exit(0);
  }
}