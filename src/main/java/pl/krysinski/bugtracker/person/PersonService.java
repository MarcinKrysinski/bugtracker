package pl.krysinski.bugtracker.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.authority.Authority;
import pl.krysinski.bugtracker.authority.AuthorityRepository;
import pl.krysinski.bugtracker.enums.Role;

import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {

    @Value("${my.admin.username}")
    private String username;
    @Value("${my.admin.password}")
    private String password;
    @Value("${my.admin.firstName}")
    private String firstName;
    @Value("${my.admin.lastName}")
    private String lastName;
    @Value("${my.admin.role}")
    private Role role;


    private final AuthorityRepository authorityRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public PersonService(AuthorityRepository authorityRepository, PersonRepository personRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.authorityRepository = authorityRepository;
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void prepareAdminUser(){
        if(personRepository.findByUsername(username).isEmpty()) {
            Person person = new Person(username, password, firstName, lastName, role);
            List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
            person.setAuthorities(new HashSet<>(authorities));
            savePerson(person);
        }
    }

    public void addAuthority(Person person, Authority authority) {
        person.authorities.add(authority);
        personRepository.save(person);
    }

    private void savePerson(Person person){
        String hashedPassword = bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(hashedPassword);
        personRepository.save(person);
    }
}
