package CsvReader;

public class Compound {
    public String name;
    public int amount;

    public Compound(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return name;
    }
}
