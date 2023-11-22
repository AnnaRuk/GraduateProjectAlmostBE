package de.ait.gp.controllers;

import de.ait.gp.config.TestSecurityConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestSecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Endpoint /kindergarten is works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class KindergartenIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("GET /Kindergartens:")
    public class GetKindergartens {


        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_empty_list_of_kindergartens_for_empty_database() throws Exception {
            mockMvc.perform(get("/api/kindergartens"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kindergartenDTOList.size()",is(0)))
                    .andExpect(jsonPath("$.cities.size()",is(0)));

        }

        @Test
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/kindergarten.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_list_of_kindergartens_for_not_empty_database() throws Exception {
            mockMvc.perform(get("/api/kindergartens"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kindergartenDTOList.size()",is(2)))
                    .andExpect(jsonPath("$.cities.size()",is(1)));

        }


        @Nested
        @DisplayName("GET /kindergartens/{kindergartens-id}:")
        public class GetKindergarten {



            @Test
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/kindergarten.sql"})
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_existed_kindergarten() throws Exception {
                mockMvc.perform(get("/api/kindergartens/1"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id", is(1)))
                        .andExpect(jsonPath("$.city",is("Berlin")))
                        .andExpect(jsonPath("$.address",is("berlinstrasse")))
                        .andExpect(jsonPath("$.title",is("hi")));

            }


            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_404_for_not_existed_kindergarten() throws Exception {
                mockMvc.perform(get("/api/kindergartens/5"))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message",is("kindergarten with id <5> not found")));
            }

        }
    }
}
