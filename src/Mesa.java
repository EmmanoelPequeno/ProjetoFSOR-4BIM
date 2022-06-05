import java.time.LocalTime;

public class Mesa {
  private int codigo;
  private String nomeCliente;
  private LocalTime horaInicio;
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

  public LocalTime getHoraFim() {
    return horaFim;
  }

  public LocalTime getHoraInicio() {
    return horaInicio;
  }

  public String getNomeCliente() {
    return nomeCliente;
  }

  public double getTotalConta() {
    return totalConta;
  }

  public void setHoraFim(LocalTime horaFim) {
    this.horaFim = horaFim;
  }

  public void setHoraInicio(LocalTime horaInicio) {
    this.horaInicio = horaInicio;
  }

  public void setNomeCliente(String nomeCliente) {
    this.nomeCliente = nomeCliente;
  }

  public void adicionarTotalConta(double preco) {
    this.totalConta += preco;
  }
}