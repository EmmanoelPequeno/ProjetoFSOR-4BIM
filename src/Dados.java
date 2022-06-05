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
}