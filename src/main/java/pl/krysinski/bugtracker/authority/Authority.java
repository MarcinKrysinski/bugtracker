
package pl.krysinski.bugtracker.authority;

import pl.krysinski.bugtracker.enums.AuthorityName;

import javax.persistence.*;

@Entity
public class Authority {

    public static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    AuthorityName authority;

    protected Authority() {
    }

    public Authority(AuthorityName authority) {
        this.authority = authority;
    }

    public AuthorityName getAuthority() {
        return authority;
    }

    public void setAuthority(AuthorityName authority) {
        this.authority = authority;
    }
}
