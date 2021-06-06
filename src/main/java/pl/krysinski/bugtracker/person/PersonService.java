package pl.krysinski.bugtracker.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.krysinski.bugtracker.authority.Authority;
import pl.krysinski.bugtracker.authority.AuthorityRepository;
import pl.krysinski.bugtracker.enums.Role;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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
    @Value("${my.admin.email}")
    private String email;


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
            Person person = new Person(username, password, firstName, lastName, role, email);
            List<Authority> authorities = (List<Authority>) authorityRepository.findAll();
            person.setAuthorities(new HashSet<>(authorities));
            savePerson(person);
        }
    }

    public void addAuthority(Person person, Authority authority) {
        person.authorities.add(authority);
        personRepository.save(person);
    }

    void savePerson(PersonForm personForm) {
        Person person = personRepository.findById(personForm.id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + personForm.id));

        person.setUsername(personForm.getUsername());
        person.setFirstName(personForm.getFirstName());
        person.setLastName(personForm.getLastName());
        person.setEmail(personForm.getEmail());
        person.setAuthorities(personForm.getAuthorities());
        personRepository.save(person);
    }

    void savePerson(Person person){
        String hashedPassword = bCryptPasswordEncoder.encode(person.getPassword());
        person.setPassword(hashedPassword);
        personRepository.save(person);
    }

    void savePassword(PasswordForm passwordForm){
        Person person = personRepository.findById(passwordForm.id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + passwordForm.id));
        String hashedPassword = bCryptPasswordEncoder.encode(passwordForm.getPassword());
        person.setPassword(hashedPassword);
        personRepository.save(person);
    }

    void softDeleteUser(Long id){
        Person user = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user id : " + id));
        user.setEnabled(false);
        String dUsername = user.getUsername() + "_deleted";
        user.setUsername(dUsername);
        personRepository.save(user);
    }

    public Optional<Person> getLoggedUser(Principal principal){
        return personRepository.findByUsername(principal.getName());
    }

}
