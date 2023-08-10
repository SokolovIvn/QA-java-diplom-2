package praktikum.User;

public class User {
    private String email;
    private String password;
    private String name;

    public User() {
    }

    public User(String email, String password, String name) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}

