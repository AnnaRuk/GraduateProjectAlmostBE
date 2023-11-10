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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public class PostUser {

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
                    .andExpect(jsonPath("$.id", is(4)))
                    .andExpect(jsonPath("$.firstName", is("Alla")))
                    .andExpect(jsonPath("$.lastName", is("Bieliaieva")))
                    .andExpect(jsonPath("$.role", is("USER")));

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
    public class GetUsersProfile {

        @WithUserDetails(value = "anna@gmail.com")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/data.sql"})
        public void return_200_and_information_exists_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.role", is("USER")));
        }


        @Test
        public void return_403_for_unauthorized_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isUnauthorized());
        }

        @Nested
        @DisplayName("POST /api/users/profile/controlKindergarten")
        public class AddControlKindergartenToManager {
            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/manager.sql","/sql/kindergarten.sql"})
            public void return_409_already_kita_with_this_data() throws Exception {
                mockMvc.perform(post("/api/users/profile/controlKindergarten")
                                .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                        "\"title\": \"hi\",\n" +
                                        "\"city\": \"Berlin\",\n" +
                                        "\"address\": \"berlinstrasse\",\n" +
                                        "\"postcode\": \"4654\",\n" +
                                        "\"capacity\": 25,\n" +
                                        "\"linkImg\": \"photo\",\n" +
                                        "\"description\": \"very good\"\n" +
                                        "\n" +
                                        "}")).andExpect(status().isConflict())
                        .andExpect(jsonPath("$.message", is("Kindergarten with this data already exists")));


            }

            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/manager.sql"})
            public void return_201_kindergarten_has_been_added() throws Exception {
                mockMvc.perform(post("/api/users/profile/controlKindergarten")
                                .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                        "\"title\": \"hello2\",\n" +
                                        "\"city\": \"Dusseldorf1\",\n" +
                                        "\"address\": \"berlinstrasse51\",\n" +
                                        "\"postcode\": \"4654\",\n" +
                                        "\"capacity\": 25,\n" +
                                        "\"linkImg\": \"photo\",\n" +
                                        "\"description\": \"very good\"\n" +
                                        "\n" +
                                        "}")).andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title", is("hello2")))
                        .andExpect(jsonPath("$.city", is("Dusseldorf1")))
                        .andExpect(jsonPath("$.address", is("berlinstrasse51")));


            }

        }

        @Nested
        @DisplayName("GET /api/users/profile/controlKindergarten")
        public class GetControlKindergarten {
            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_404_kindergarten_of_manager_not_found() throws Exception {
                mockMvc.perform(get("/api/users/profile/controlKindergarten"))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", is("Kindergarten of manager with id <3> not found")));
            }

            @WithUserDetails(value = "MANAGER")
            @Test
            @Sql(scripts = {"/sql/manager.sql","/sql/kindergarten.sql"})
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_200_kindergarten_of_manager() throws Exception {
                mockMvc.perform(get("/api/users/profile/controlKindergarten"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is("hi")))
                        .andExpect(jsonPath("$.city", is("Berlin")))
                        .andExpect(jsonPath("$.address", is("berlinstrasse")));
            }


        }

        @Nested
        @DisplayName("PUT /api/users/profile/controlKindergarten")
        public class UpdateControlKindergarten {

            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_404_kindergarten_of_manager_not_found() throws Exception {
                mockMvc.perform(put("/api/users/profile/controlKindergarten")
                                .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                        "\"id\": 1,\n" +
                                        "\"title\": \"hello\",\n" +
                                        "\"city\": \"Dusseldorf\",\n" +
                                        "\"address\": \"berlinstrasse5\",\n" +
                                        "\"postcode\": \"4654\",\n" +
                                        "\"capacity\": 25,\n" +
                                        "\"linkImg\": \"photo\",\n" +
                                        "\"description\": \"very good\"\n" +
                                        "\n" +
                                        "}"))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", is("Kindergarten of manager with id <3> not found")));

            }

            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/manager.sql","/sql/kindergarten.sql"})
            public void return_409_kindergarten_with_this_data_already_exists() throws Exception {

                mockMvc.perform(put("/api/users/profile/controlKindergarten")
                                .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                        "\"id\": 2,\n" +
                                        "\"title\": \"hi\",\n" +
                                        "\"city\": \"Berlin\",\n" +
                                        "\"address\": \"strasse50\",\n" +
                                        "\"postcode\": \"4654\",\n" +
                                        "\"capacity\": 25,\n" +
                                        "\"linkImg\": \"photo\",\n" +
                                        "\"description\": \"very good\"\n" +
                                        "\n" +
                                        "}"))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.message", is("Kindergarten with this data already exists")));

            }


            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/manager.sql","/sql/kindergarten.sql"})
            public void return_200_and_kindergartenDto() throws Exception {

                mockMvc.perform(put("/api/users/profile/controlKindergarten")
                                .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                        "\"id\": 1,\n" +
                                        "\"title\": \"hello\",\n" +
                                        "\"city\": \"Koln\",\n" +
                                        "\"address\": \"new address\",\n" +
                                        "\"postcode\": \"1111\",\n" +
                                        "\"capacity\": 10,\n" +
                                        "\"linkImg\": \"new photo\",\n" +
                                        "\"description\": \"new kindergarten\"\n" +
                                        "\n" +
                                        "}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.title", is("hello")))
                        .andExpect(jsonPath("$.city", is("Koln")))
                        .andExpect(jsonPath("$.address", is("new address")));


            }

        }
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

