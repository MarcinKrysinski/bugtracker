package pl.krysinski.bugtracker.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.authority.Authority;
import pl.krysinski.bugtracker.authority.AuthorityRepository;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public PersonController(PersonService personService, PersonRepository personRepository, AuthorityRepository authorityRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/list")
    public Iterable<Person> list() {
        return personRepository.findAll();
    }

//    @PostMapping("/save")
//    public Person save(@RequestParam String username, @RequestParam String password) {
//        Person person = new Person(username, password, true);
//        return personRepository.save(person);
//    }


    @GetMapping("/get")
    public Optional<Person> get(@RequestParam Long id) {
        return personRepository.findById(id);
    }

    @GetMapping("/show")
    public Optional<Person> get(@RequestParam String username) {
        return personRepository.findByUsernameAndEnabled(username, true);
    }

    /**
     * GET http://localhost:8080/people/list-sorted?dateString=2021-02-13T10:30-0000
     *
     * @param dateString data ze strefą czasową, np. 2021-02-13T10:30-0000
     * @return lista rekordów `person`
     * @throws ParseException w przypadku błędnego formatu daty
     */
    @GetMapping("/list-created-after")
    public Iterable<Person> listCreatedAfter(@RequestParam String dateString)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");

        return personRepository.findEnabledUsersCreatedAfter(sdf.parse(dateString));
    }

    @PostMapping("/disable")
    public Optional<Person> disable(@RequestParam String username) {
        Optional<Person> person = personRepository.findByUsernameAndEnabled(username, true);
        person.ifPresent((value) -> {
            value.setEnabled(false);
            personRepository.save(value);
        });
        return person;
    }

    @GetMapping("{username}/authorities")
    public Iterable<Authority> getAuthorities(@PathVariable String username) {
        return authorityRepository.findAllByPersonUsername(username);
    }

    @PostMapping("{username}/authorities")
    public Person addAuthority(@PathVariable String username, @RequestParam String authority) {
        Optional<Person> optPerson = personRepository.findByUsernameAndEnabled(username, true);

        if (optPerson.isEmpty()) {
            throw new InvalidParameterException("No user found");
        }

        Optional<Authority> optAuthority = authorityRepository.findByAuthority(Authority.ROLE_PREFIX + authority);

        if (optAuthority.isEmpty()) {
            throw new InvalidParameterException("No authority found");
        }

        Person person = optPerson.get();

        personService.addAuthority(person, optAuthority.get());

        return person;
    }
}
