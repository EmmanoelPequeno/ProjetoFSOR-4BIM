import java.io.IOException;

public class ProgGerencia {
  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar item", "Listar mesas", "Sair do programa"
  };

  private static final Opcao[] FUNCOES_OPCOES = new Opcao[] {
      ProgGerencia::cadastrarItem, ProgGerencia::listarMesas, ProgGerencia::sairDoPrograma
  };

  private int codigoAtual;
  private final Entrada entrada;
  private final Dados dados;
  private boolean continuar;

  private ProgGerencia(Entrada entrada, Dados dados) {
    this.entrada = entrada;
    this.dados = dados;
  }

  public static void main(String[] args) {
    var dados = new Dados();

    try {
      new Thread(new Servidor(dados)).start();
    } catch (IOException e) {
      System.err.println("Erro de conexão: " + e);
      System.exit(1);
    }

    try (var entrada = new Entrada()) {
      new ProgGerencia(entrada, dados).run();
    }
  }

  private void run() {
    System.out.println("Iniciando...\n");
    this.continuar = true;

    while (true) {
      var funcao = this.entrada.escolherOpcao(DESCRICOES_OPCOES, FUNCOES_OPCOES);
      funcao.accept(this);

      if (this.continuar) {
        this.entrada.aguardarEnter();
      } else {
        break;
      }
    }
  }

  private void cadastrarItem() {
    var descricao = this.entrada.lerString("Digite a descrição do item: ");
    var preco = this.entrada.lerDoubleValidar("Digite o preço de venda do item: ");
    var quantidade = this.entrada.lerInt("Digite a quantidade inicial do item: ");
    var item = new Item(this.codigoAtual++, descricao, preco, quantidade);

    this.dados.getItens().add(item);
    System.out.println("Item cadastrado");
  }

  private void listarMesas() {
    var mesas = this.dados.getMesas();

    if (mesas.isEmpty()) {
      System.out.println("Sem mesas cadastradas");
      return;
    }

    for (var mesa : mesas) {
      System.out.printf("== Mesa %d\n", mesa.getCodigo());
      System.out.printf("Nome do cliente: %s\n", mesa.getNomeCliente());
      System.out.printf("Total da mesa: R$ %.2f\n", mesa.getTotalConta());
      System.out.printf("Horário de início da mesa: %s\n", mesa.getHorarioEntrada());

      if (mesa.getHorarioSaida() == null) {
        System.out.println("Ainda em atendimento");
      } else {
        System.out.printf("Horário de saída da mesa: %s\n", mesa.getHorarioSaida());
      }

      System.out.println();
    }
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    this.continuar = false;
  }

  @FunctionalInterface
  public interface Opcao {
    void accept(ProgGerencia main);
  }
}