package pl.droidsonroids.bootcamp.yo.model;

public class User implements Comparable<User> {
    int id;
    String name;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(User another) {
        return name.toLowerCase().compareTo(another.getName().toLowerCase());
    }
}
