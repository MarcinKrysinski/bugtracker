//package pl.krysinski.bugtracker.project;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.Incubating;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import pl.krysinski.bugtracker.enums.Role;
//import pl.krysinski.bugtracker.person.Person;
//import pl.krysinski.bugtracker.person.PersonService;
//import pl.krysinski.bugtracker.project.Project;
//import pl.krysinski.bugtracker.person.PersonRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.when;
//
//
//@SpringBootTest
////@WebMvcTest(ProjectController.class)
//@AutoConfigureMockMvc
////@ExtendWith(MockitoExtension.class)
//
//class ProjectControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//    @InjectMocks
//    private ProjectController projectController;
//    Person person = new Person(1L, "Marcin", "12345566", "Marcin", "Krysinksi", Role.ADMIN, "test@test.pl");
//
//    Person person2 = new Person(2L, "Test", "12345566", "Marcin", "Kowalski", Role.ADMIN, "test@test.pl");
//
//    @MockBean
//    private ProjectRepository projectRepository;
//    @MockBean
//    private PersonRepository personRepository;
//
//
//
////    private List<Project> mockProjects(){
////        List<Project> projects = new ArrayList<>();
////        projects.add(new Project(1L, "Test", true, "test", person, "<p>test</p>"));
////        projects.add(new Project(2L, "Test2", true, "test2", person, "<p>test2</p>"));
////        projects.add(new Project(3L, "Test3", true, "test3", person, "<p>test3</p>"));
////        return projects;
////    }
////
////    private List<Person> mockPersons(){
////        List<Person> persons = new ArrayList<>();
////        persons.add(person);
////        persons.add(person2);
////        return persons;
////    }
//
//
//    @Test
////    @WithMockUser(username = "admin", password = "password123", authorities = "ROLE_MANAGE_PROJECTS")
//    void should_return_view_with_projects() throws Exception {
//
//        List<Project> projects = new ArrayList<>();
//        projects.add(new Project(1L, "Test", true, "test", person, "<p>test</p>"));
//        projects.add(new Project(2L, "Test2", true, "test2", person, "<p>test2</p>"));
//        projects.add(new Project(3L, "Test3", true, "test3", person, "<p>test3</p>"));
//
//        List<Person> persons = new ArrayList<>();
//        persons.add(person);
//        persons.add(person2);
//
////        when(projectRepository.findAll()).thenReturn(projects);
////        when(personRepository.findAll()).thenReturn(persons);
//
//        given(projectRepository.findAll()).willReturn(projects);
//        given(personRepository.findAll()).willReturn(persons);
//
//        this.mockMvc
//                .perform(MockMvcRequestBuilders.get("/projects"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.view().name("project/projects"))
//                .andExpect(MockMvcResultMatchers.model().attributeExists("filter"));
//
//    }
//}
