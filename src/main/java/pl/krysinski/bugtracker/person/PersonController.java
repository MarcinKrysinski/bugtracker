package pl.krysinski.bugtracker.person;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.krysinski.bugtracker.authority.AuthorityRepository;
import pl.krysinski.bugtracker.security.SecurityService;
import javax.validation.Valid;
import java.security.Principal;


@Controller
@Slf4j
@RequestMapping("/users")
public class PersonController {

    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;
    private final PersonService personService;
    private final SecurityService securityService;

    @Autowired
    public PersonController(PersonRepository personRepository, AuthorityRepository authorityRepository, PersonService personService, SecurityService securityService) {
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
        this.personService = personService;
        this.securityService = securityService;
    }


    @GetMapping
    @Secured("ROLE_USERS_TAB")
    public String users(Model model){
        model.addAttribute("users", personRepository.findAll());
        return "user/users";
    }


    @GetMapping("/create")
    @Secured("ROLE_MANAGE_USERS")
    public String showPersonForm(Model model){
        model.addAttribute("authorities", authorityRepository.findAll());
        model.addAttribute("person", new Person());
        return "user/add-user";
    }


    @PostMapping("/save")
    public String savePerson(@ModelAttribute @Valid Person person, BindingResult result, Model model){
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        if(result.hasErrors()){
            model.addAttribute("authorities", authorityRepository.findAll());
            model.addAttribute("person", person);
            log.error("There was a problem. The user: " + person + " was not saved.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            log.debug("Model: {}", model);
            return "user/add-user";
        }
        personService.savePerson(person);

        log.info("Created new user: " + person.getUsername() + " by: " + usernameLoggedPerson);
        log.debug("Create new user: {}", person);
        return "redirect:/users";
    }


    @GetMapping("delete/{id}")
    @Secured("ROLE_MANAGE_USERS")
    public String deleteUser(@PathVariable("id") Long id) {
        personService.softDeleteUser(id);
        return "redirect:/users";
    }

    @GetMapping("edit/{id}")
    @Secured("ROLE_MANAGE_USERS")
    public String showUpdateForm(@PathVariable ("id") Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person id : " + id));

        model.addAttribute("authorities", authorityRepository.findAll());
        model.addAttribute("personForm", new PersonForm(person));
        return "user/details-user";
    }

    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") Long id, @Valid PersonForm personForm, BindingResult result, Model model) {
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();

        if(result.hasErrors()) {
            model.addAttribute("authorities", authorityRepository.findAll());
            personForm.setId(id);
            log.error("There was a problem. The user: " + personForm + " was not update.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "user/details-user";
        }
        personService.savePerson(personForm);

        log.info("Updated user: " + personForm.getUsername() + " by: " + usernameLoggedPerson);
        log.debug("Updated user: {}", personForm);
        return "redirect:/users";
    }

    @GetMapping("edit/password/{id}")
    @Secured("ROLE_MANAGE_USERS")
    public String showUpdatePasswordForm(@PathVariable ("id") Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person id : " + id));

        model.addAttribute("passwordForm", new PasswordForm(person));
        return "user/password-user";
    }

    @PostMapping("update/password/{id}")
    public String updateUserPassword(@PathVariable("id") Long id, @Valid PasswordForm passwordForm, BindingResult result) {
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person id : " + id));

        if(result.hasErrors()) {
            passwordForm.setId(id);
            log.error("There was a problem. The password: " + passwordForm + " was not update.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "user/password-user";
        }
        personService.savePassword(passwordForm);

        log.info("Updated user password: " + person.getUsername() + " by: " + usernameLoggedPerson);
        log.debug("Updated user: {}", passwordForm);
        return "redirect:/users/edit/{id}";
    }

    @GetMapping("/my_account")
    public String showMyAccountForm(Principal principal, Model model) {
        String username = principal.getName();
        Person person = personRepository.findByUsername(username).orElse(null);
        if (person == null){
            log.error("There was a problem. The user was not found.");
            log.error("Error: {}", model);
            log.debug("BindingResult: {}", model);
            return "error/404";
        }
        model.addAttribute("authorities", authorityRepository.findAll());
        model.addAttribute("personForm", new PersonForm(person));
        return "myAccount/my-account-details";
    }

    @GetMapping("my_account/edit/password/{id}")
    public String showUpdateMyAccountPasswordForm(@PathVariable ("id") Long id, Model model) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person id : " + id));

        model.addAttribute("passwordForm", new PasswordForm(person));
        return "myAccount/my-account-password";
    }

    @PostMapping("my_account/update/password/{id}")
    public String updateMyAccountPassword(@PathVariable("id") Long id, @Valid PasswordForm passwordForm, BindingResult result) {
        String usernameLoggedPerson = securityService.getUsernameLoggedUser();
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid person id : " + id));

        if(result.hasErrors()) {
            passwordForm.setId(id);
            log.error("There was a problem. The password: " + passwordForm + " was not update.");
            log.error("Error: {}", result);
            log.debug("BindingResult: {}", result);
            return "myAccount/my-account-password";
        }
        personService.savePassword(passwordForm);

        log.info("Updated user password: " + person.getUsername() + " by: " + usernameLoggedPerson);
        log.debug("Updated user: {}", passwordForm);
        return "redirect:/my_account";
    }
}
