package pl.krysinski.bugtracker.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void addAuthority(Person person, Authority authority) {
        person.authorities.add(authority);
        personRepository.save(person);
    }
}
