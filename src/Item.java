public class Item {
  private final int codigo;
  private final String descricao;
  private final double preco;
  private int quantidade;

  public Item(int codigo, String descricao, double preco, int quantidade) {
    this.codigo = codigo;
    this.descricao = descricao;
    this.preco = preco;
    this.quantidade = quantidade;
  }

  public int getCodigo() {
    return this.codigo;
  }

  public String getDescricao() {
    return this.descricao;
  }

  public double getPreco() {
    return this.preco;
  }

  public int getQuantidade() {
    return this.quantidade;
  }

  public void diminuirQuantidade() {
    this.quantidade -= 1;
  }
}
