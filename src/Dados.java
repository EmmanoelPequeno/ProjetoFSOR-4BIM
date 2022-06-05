import java.util.ArrayList;
import java.util.List;

public class Dados {
  private final List<Item> items;
  private final List<Mesa> mesas;

  public Dados() {
    this.items = new ArrayList<>();
    this.mesas = new ArrayList<>();
  }

  public List<Item> getItems() {
    return this.items;
  }

  public List<Mesa> getMesas() {
    return this.mesas;
  } 
}