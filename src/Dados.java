import java.util.ArrayList;
import java.util.List;

public class Dados {
  private final List<Item> itens;
  private final List<Mesa> mesas;

  public Dados() {
    this.itens = new ArrayList<>();
    this.mesas = new ArrayList<>();
  }

  public List<Item> getItens() {
    return this.itens;
  }

  public List<Mesa> getMesas() {
    return this.mesas;
  }

  public Item getItemByCodigo(int codigo) {
    for (var item : this.itens) {
      if (item.getCodigo() == codigo) {
        return item;
      }
    }

    return null;
  }

  public Mesa getMesaByCodigo(int codigo) {
    for (var mesa : this.mesas) {
      if (mesa.getCodigo() == codigo) {
        return mesa;
      }
    }

    return null;
  }
}