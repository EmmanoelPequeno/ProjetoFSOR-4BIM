import java.time.LocalTime;

public class Mesa {
  private final int codigo;
  private final String nomeCliente;
  private final LocalTime horarioEntrada;
  private LocalTime horarioSaida;
  private double totalConta;

  public Mesa(int codigo, String nomeCliente) {
    this.codigo = codigo;
    this.nomeCliente = nomeCliente;
    this.horarioEntrada = LocalTime.now();
  }

  public int getCodigo() {
    return this.codigo;
  }

  public String getNomeCliente() {
    return this.nomeCliente;
  }

  public LocalTime getHorarioEntrada() {
    return this.horarioEntrada;
  }

  public LocalTime getHorarioSaida() {
    return this.horarioSaida;
  }

  public double getTotalConta() {
    return this.totalConta;
  }

  public void setHorarioSaida(LocalTime horarioSaida) {
    this.horarioSaida = horarioSaida;
  }

  public void adicionarTotalConta(double preco) {
    this.totalConta += preco;
  }
}