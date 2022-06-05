import java.time.LocalTime;

public class Mesa {
  private final int codigo;
  private final String nomeCliente;
  private final LocalTime horaInicio;
  private LocalTime horaFim;
  private double totalConta;

  public Mesa(int codigo, String nomeCliente) {
    this.codigo = codigo;
    this.nomeCliente = nomeCliente;
    this.horaInicio = LocalTime.now();
  }

  public int getCodigo() {
    return codigo;
  }

  public String getNomeCliente() {
    return nomeCliente;
  }

  public LocalTime getHoraInicio() {
    return horaInicio;
  }

  public LocalTime getHoraFim() {
    return horaFim;
  }

  public double getTotalConta() {
    return totalConta;
  }

  public void setHoraFim(LocalTime horaFim) {
    this.horaFim = horaFim;
  }

  public void adicionarTotalConta(double preco) {
    this.totalConta += preco;
  }
}