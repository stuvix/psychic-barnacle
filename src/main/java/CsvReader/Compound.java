package CsvReader;

public class Compound {
    public String firstName;
    public String lastName;

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Compound(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Compound(String[] array) {
        this(array[0], array[1]);
        assert(array.length == 2);
    }
}
