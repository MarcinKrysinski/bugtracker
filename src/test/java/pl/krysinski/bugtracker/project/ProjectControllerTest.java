package pl.krysinski.bugtracker.project;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", password = "password1234", authorities = "ROLE_MANAGE_PROJECTS")
    public void should_access_to_projects_list_test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @WithMockUser(username = "admin", password = "password1234", authorities = "ROLE_MANAGE_PROJECTS")
    public void should_return_view_with_projects_test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("project/projects"));
    }

}
