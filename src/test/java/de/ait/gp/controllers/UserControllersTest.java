package de.ait.gp.controllers;


import de.ait.gp.config.TestSecurityConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = TestSecurityConfig.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DisplayName("Endpoint /users is works:")
@DisplayNameGeneration(value =
        DisplayNameGenerator.ReplaceUnderscores.class)
class UserControllersTest {

    @Autowired
    private MockMvc mockMvc;


    @Nested
    @DisplayName("POST/api/users/register:")
    public class PostUser{

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/data.sql"})
        public void return_201_add_created_user() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "\"firstName\": \"Alla\",\n" +
                            "\"lastName\": \"Bieliaieva\",\n" +
                            "\"email\": \"natalia@gmail.com\",\n" +
                            "\"hashPassword\": \"Qwerty555!\"\n" +
                            "\n" +
                            "}")).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id",is(4)))
                    .andExpect(jsonPath("$.firstName",is("Alla")))
                    .andExpect(jsonPath("$.lastName",is("Bieliaieva")))
                    .andExpect(jsonPath("$.role",is("USER")));

        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/data.sql"})
        public void return_409_user_with_email_exists() throws Exception {
            mockMvc.perform(post("/api/users/register")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"firstName\": \"Anna\",\n" +
                                    "\"lastName\": \"Bieliaieva\",\n" +
                                    "\"email\": \"anna@gmail.com\",\n" +
                                    "\"hashPassword\": \"Qwerty555!\"\n" +
                                    "\n" +
                                    "}")).andExpect(status().isConflict());

        }
        @Test
        public void return_400_validation_password_error() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "\"firstName\": \"Anna\",\n" +
                            "\"lastName\": \"Bieliaieva\",\n" +
                            "\"email\": \"anna33@gmail.com\",\n" +
                            "\"hashPassword\": \"Qw\"\n" +
                            "\n" +
                            "}")).andExpect(status().isBadRequest());

        }

        @Test
        public void return_400_validation_email_error() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "\"firstName\": \"Anna\",\n" +
                            "\"lastName\": \"Bieliaieva\",\n" +
                            "\"email\": \"anna33gmail.com\",\n" +
                            "\"hashPassword\": \"Qwerty004!\"\n" +
                            "\n" +
                            "}")).andExpect(status().isBadRequest());

        }

    }

    @Nested
    @DisplayName("POST/api/users/profile:")
    public class GetUsersProfile{

        @WithUserDetails(value = "anna@gmail.com")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/data.sql"})
        public void return_200_and_information_exists_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id",is(1)))
                    .andExpect(jsonPath("$.role", is("USER")));
        }


        @Test
        public void return_403_for_unauthorized_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                   .andExpect(status().isUnauthorized());
        }



    }

//
//    @Nested
//    @DisplayName("GET/users:")
//    public class GetUsers{
//
//        @WithUserDetails(value = "admin")
//        @Test
//        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
//        @Sql(scripts = {"/sql/data.sql"})
//        public void return_status_200_for_list_of_users() throws Exception {
//            mockMvc.perform(get("/api/users")).andExpect(status().isOk());
//
//
//        }
//
//        @Test
//        @WithUserDetails(value = "admin")
//        public void return_status_200_for_empty_list_of_users() throws Exception {
//            mockMvc.perform(get("/api/users")).andExpect(status().isOk())
//                    .andExpect(jsonPath("$.size()",is(0)));
//
//        }
//
//        @Test
//        @WithUserDetails(value = "user")
//        public void return_status_403_for_not_admin() throws Exception {
//            mockMvc.perform(get("/api/users")).andExpect(status().isForbidden());
//        }
//
//    }

}