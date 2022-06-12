import java.io.IOException;

public class ProgCozinha {
  private static final String[] DESCRICOES_OPCOES = new String[] {
      "Cadastrar item", "Listar mesas", "Sair do programa"
  };

  private static final Opcao[] FUNCOES_OPCOES = new Opcao[] {
      ProgCozinha::cadastrarItem, ProgCozinha::listarMesas, ProgCozinha::sairDoPrograma
  };

  private int codigoAtual;
  private final Entrada entrada;
  private final Dados dados;
  private boolean continuar;

  public static void main(String[] args) {
    var dados = new Dados();

    try (var entrada = new Entrada(); var servidor = new Servidor(dados);) {
      System.out.println("Iniciando servidor na porta 8000...");
      new Thread(servidor).start();

      new ProgCozinha(entrada, dados).run();
    } catch (IOException e) {
      System.err.println("Erro no servidor: " + e.getMessage());
      System.exit(1);
      return;
    }
  }

  private ProgCozinha(Entrada entrada, Dados dados) {
    this.entrada = entrada;
    this.dados = dados;
    this.continuar = true;
  }

  private void run() {
    System.out.println("Inicindo programa da cozinha...");

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
    System.out.println();

    var lock = this.dados.getLock().writeLock();
    lock.lock();
    try {
      this.dados.getItens().add(item);
    } finally {
      lock.unlock();
    }

    System.out.println("Item cadastrado com sucesso");
  }

  private void listarMesas() {
    var lock = this.dados.getLock().readLock();
    lock.lock();

    try {
      var mesas = this.dados.getMesas();

      if (mesas.isEmpty()) {
        System.out.println("Sem mesas cadastradas");
        return;
      }

      for (var mesa : mesas) {
        System.out.println("== Mesa " + mesa.getCodigo());
        System.out.println("Nome do(a) cliente: " + mesa.getNomeCliente());
        System.out.printf("Total da mesa: R$ %.2f\n", mesa.getTotalConta());
        System.out.println("Horário de início da mesa: " + mesa.getHorarioEntrada());

        if (mesa.getHorarioSaida() == null) {
          System.out.println("Ainda em atendimento");
        } else {
          System.out.println("Horário de saída da mesa: " + mesa.getHorarioSaida());
        }

        System.out.println();
      }
    } finally {
      lock.unlock();
    }
  }

  private void sairDoPrograma() {
    System.out.println("Saindo...");
    this.continuar = false;
  }

  @FunctionalInterface
  public interface Opcao {
    void accept(ProgCozinha main);
  }
}
