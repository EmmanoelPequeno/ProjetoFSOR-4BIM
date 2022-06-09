import java.util.Scanner;

public class Entrada implements AutoCloseable {
  private final Scanner scanner;

  public Entrada() {
    this.scanner = new Scanner(System.in);
  }

  @Override
  public void close() {
    this.scanner.close();
  }

  public <T> T escolherOpcao(String[] descricoes, T[] opcoes) {
    System.out.println("\n== Opções disponíveis:");

    for (var i = 0; i < descricoes.length; i++) {
      System.out.printf("%d. %s\n", i + 1, descricoes[i]);
    }

    var i = lerIntValidar("Escolha uma: ", 1, descricoes.length) - 1;
    System.out.println();

    var opcao = opcoes[i];
    return opcao;
  }

  public void aguardarEnter() {
    System.out.print("Aperte Enter para continuar...");
    this.scanner.nextLine();
  }

  public String lerString(String msg) {
    while (true) {
      System.out.print(msg);
      var entrada = this.scanner.nextLine().strip();

      if (!entrada.isEmpty() && Character.isLetter(entrada.charAt(0))) {
        return entrada;
      } else {
        System.out.println("Entrada inválida!");
      }
    }
  }

  public int lerInt(String msg) {
    while (true) {
      System.out.print(msg);
      var entrada = this.scanner.nextLine();

      try {
        return Integer.parseInt(entrada);
      } catch (NumberFormatException e) {
        System.out.println("Entrada inválida!");
      }
    }
  }

  public int lerIntValidar(String msg, int min, int max) {
    while (true) {
      System.out.print(msg);
      var entrada = this.scanner.nextLine();
      int numero;

      try {
        numero = Integer.parseInt(entrada);
      } catch (NumberFormatException e) {
        System.out.println("Entrada inválida!");
        continue;
      }

      if (numero >= min && numero <= max) {
        return numero;
      } else {
        System.out.println("Entrada inválida!");
      }
    }
  }

  public double lerDoubleValidar(String msg) {
    while (true) {
      System.out.print(msg);
      var entrada = this.scanner.nextLine();
      double numero;

      try {
        numero = Double.parseDouble(entrada);
      } catch (NumberFormatException e) {
        System.out.println("Entrada inválida!");
        continue;
      }

      if (numero >= 0) {
        return numero;
      } else {
        System.out.println("Entrada inválida!");
      }
    }
  }
}
