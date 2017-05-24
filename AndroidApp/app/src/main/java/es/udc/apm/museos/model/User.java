package es.udc.apm.museos.model;

/**
 * Created by 4m1g0 on 8/05/17.
 */

public class User {
    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String name;
    public String email;
}
