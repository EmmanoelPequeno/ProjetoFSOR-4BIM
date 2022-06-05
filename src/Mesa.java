import java.time.LocalTime;

public class Mesa {
  private int codigo;
  private int qtdDisponivel;
  private String nomeCliente;
  private LocalTime horaInicio;
  private LocalTime horaFim;
  private double totalConta;

  public Mesa(int codigo, String nomeCliente, int qtdDisponivel, LocalTime horaInicio) {
    this.codigo = codigo;
    this.qtdDisponivel = qtdDisponivel;
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

  public int getQtdDisponivel() {
    return qtdDisponivel;
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

  public void setQtdDisponivel(int qtdDisponivel) {
    this.qtdDisponivel = qtdDisponivel;
  }

  public void setTotConta(double totConta) {
    this.totalConta = totConta;
  }
}