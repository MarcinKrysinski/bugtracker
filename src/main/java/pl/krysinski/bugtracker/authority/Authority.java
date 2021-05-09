
package pl.krysinski.bugtracker.authority;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.krysinski.bugtracker.enums.AuthorityName;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Authority {

    public static final String ROLE_PREFIX = "ROLE_";

    @Id
    @GeneratedValue
    public Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    AuthorityName authority;

    public Authority(AuthorityName authority) {
        this.authority = authority;
    }

}
