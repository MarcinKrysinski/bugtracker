
package pl.krysinski.bugtracker.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Authority {

    static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false, unique = true)
    String authority;

    protected Authority() {
    }

    public Authority(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
