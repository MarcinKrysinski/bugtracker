package pl.krysinski.bugtracker.person;


import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(nullable = false, unique = true, length = 100)
    private String username;

    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @ColumnDefault("false")
    private Boolean passwordExpired;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    private Date dateCreated;

    @ManyToMany
    @JoinTable(name = "person_authorities",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    Set<Authority> authorities;

    public Person(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.dateCreated = new Date();
        this.passwordExpired = false;
    }

    protected Person() {
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return String.format("%s (#%s)", username, id);
    }


}
