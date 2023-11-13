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

import static org.hamcrest.Matchers.empty;
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
        @Sql(scripts = {"/sql/user.sql"})
        public void return_201_add_created_user() throws Exception {
            mockMvc.perform(post("/api/users/register")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "  \"email\": \"user@gmail.com\",\n" +
                                    "  \"password\": \"Qwerty009!\",\n" +
                                    "  \"firstName\": \"Olha\",\n" +
                                    "  \"lastName\": \"Rukina\",\n" +
                                    "  \"role\": \"USER\"\n" +
                                    "}")).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id", is(3)))
                    .andExpect(jsonPath("$.firstName", is("Olha")))
                    .andExpect(jsonPath("$.lastName", is("Rukina")))
                    .andExpect(jsonPath("$.email", is("user@gmail.com")))
                    .andExpect(jsonPath("$.role", is("USER")));

        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_409_user_with_email_exists() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"email\": \"anna@gmail.com\",\n" +
                            "  \"password\": \"Qwerty009!\",\n" +
                            "  \"firstName\": \"Anna\",\n" +
                            "  \"lastName\": \"Beliaieva\",\n" +
                            "  \"role\": \"USER\"\n" +
                            "}")).andExpect(status().isConflict());

        }

        @Test
        public void return_400_validation_password_error() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"email\": \"anna@gmail.com\",\n" +
                            "  \"password\": \"Qwerty009\",\n" +
                            "  \"firstName\": \"Anna\",\n" +
                            "  \"lastName\": \"Beliaieva\",\n" +
                            "  \"role\": \"USER\"\n" +
                            "}")).andExpect(status().isBadRequest());

        }

        @Test
        public void return_400_validation_email_error() throws Exception {
            mockMvc.perform(post("/api/users/register")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"email\": \"annagmail.com\",\n" +
                            "  \"password\": \"Qwerty009!\",\n" +
                            "  \"firstName\": \"Anna\",\n" +
                            "  \"lastName\": \"Beliaieva\",\n" +
                            "  \"role\": \"USER\"\n" +
                            "}")).andExpect(status().isBadRequest());

        }

    }

    @Nested
    @DisplayName("POST/api/users/profile:")
    public class GetUsersProfile {

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_200_and_information_exists_user_with_role_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.role", is("USER")))
                    .andExpect(jsonPath("$.email", is("user1@gmail.com")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql"})
        public void return_200_and_information_exists_user_with_role_manager() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(3)))
                    .andExpect(jsonPath("$.role", is("MANAGER")))
                    .andExpect(jsonPath("$.email", is("manager3@gmail.com")));
        }


        @Test
        public void return_403_for_unauthorized_user() throws Exception {
            mockMvc.perform(get("/api/users/profile"))
                    .andExpect(status().isUnauthorized());
        }

    }

    @Nested
    @DisplayName("PUT/api/users/profile:")
    public class PutUsersProfile{

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_200_and_update_user_profile_information() throws Exception {
            mockMvc.perform(put("/api/users/profile")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"firstName\": \"Oleg\",\n" +
                            "  \"lastName\": \"Sedakov\",\n" +
                            "  \"dateOfBirth\": \"1990-07-03\",\n" +
                            "  \"email\": \"user88@gmail.com\",\n" +
                            "  \"postcode\": \"10117\",\n" +
                            "  \"address\": \"Berlin str. 8\",\n" +
                            "  \"city\": \"Gamburg\",\n" +
                            "  \"phone\": \"+495451619\",\n" +
                            "  \"gender\": \"MALE\"\n" +
                            "}")).andExpect(status().isOk());
        }
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_400_update_user_profile_validation_error() throws Exception {
            mockMvc.perform(put("/api/users/profile")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"firstName\": \"Sergey\"\n" +
                            "}")).andExpect(status().isBadRequest());


        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_409_update_user_with_email_already_exists() throws Exception {
            mockMvc.perform(put("/api/users/profile")
                    .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                            "  \"firstName\": \"Sergey\",\n" +
                            "  \"lastName\": \"Sedakov\",\n" +
                            "  \"dateOfBirth\": \"1990-05-03\",\n" +
                            "  \"email\": \"anna@gmail.com\",\n" +
                            "  \"postcode\": \"46446\",\n" +
                            "  \"address\": \"Berlin str. 8\",\n" +
                            "  \"city\": \"Berlin\",\n" +
                            "  \"phone\": \"+495451619\",\n" +
                            "  \"gender\": \"MALE\"\n" +
                            "}")).andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("User with email <anna@gmail.com> already exists")));


        }


    }

    @Nested
    @DisplayName("GET/api/users/confirm/{confirm-code}:")
    public class GetConfirm {

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/notComfirmedUser.sql","/sql/confirmcode.sql"})
        public void return_200_and_information_exists_user_with_role_user() throws Exception {
            mockMvc.perform(get("/api/users/confirm/318e0fbf-d167-406b-b4ae-5d163ad82c18"))
                    .andExpect(status().isOk());
        }

        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/notComfirmedUser.sql","/sql/confirmcode.sql"})
        public void return_400_code_not_found() throws Exception {
            mockMvc.perform(get("/api/users/confirm/318e0fbf-d167-406b-b4ae-5d163"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Code: 318e0fbf-d167-406b-b4ae-5d163 not found or is expired")));
        }

    }


        @Nested
        @DisplayName("POST/api/users/profile/controlKindergarten")
        public class AddControlKindergartenToManager {
            @WithUserDetails(value = "MANAGER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql", "/sql/kindergarten.sql"})
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
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql"})
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
        @DisplayName("GET/api/users/profile/controlKindergarten")
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
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql", "/sql/kindergarten.sql"})
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
        @DisplayName("PUT/api/users/profile/controlKindergarten")
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
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql", "/sql/kindergarten.sql"})
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
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql", "/sql/kindergarten.sql"})
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


    @Nested
    @DisplayName("GET/api/users/profile/favorites:")
    public class GetUserFavoritesKindergartens {


        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = "/sql/user.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_for_not_existed_favorites_kindergartens() throws Exception {
            mockMvc.perform(get("/api/users/profile/favorites"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kindergartens", is(empty())));


        }

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql", "/sql/favorites.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_200_and_list_favorites_kindergartens() throws Exception {
            mockMvc.perform(get("/api/users/profile/favorites"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kindergartens.size()", is(2)))
                    .andExpect(jsonPath("$.kindergartens[0].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[0].address", is("berlinstrasse")))
                    .andExpect(jsonPath("$.kindergartens[0].description", is("forest kindergarten")))
                    .andExpect(jsonPath("$.kindergartens[1].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[1].address", is("strasse50")))
                    .andExpect(jsonPath("$.kindergartens[1].description", is("very god kindergarten")));


        }
    }

    @Nested
    @DisplayName("POST/api/users/profile/favorites:")
    public class AddUserFavoritesKindergartens {


        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql", "/sql/favorites.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_409_for_kindergarten_already_added_to_favorites() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"1\"\n" +
                                    "}"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("This kindergarten has already been added to the user")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_201_and_list_favorites_kindergartens() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"1\"\n" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.kindergartens.size()", is(1)))
                    .andExpect(jsonPath("$.kindergartens[0].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[0].address", is("berlinstrasse")))
                    .andExpect(jsonPath("$.kindergartens[0].description", is("forest kindergarten")));
        }


        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = "/sql/user.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_not_found_kindergarten() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"7\"\n" +
                                    "}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten with id <7> not found")));

        }
    }

    @Nested
    @DisplayName("DELETE/api/users/profile/favorites:")
    public class DeleteUserFavoritesKindergartens {

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_not_found_kindergarten() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"7\"\n" +
                                    "}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten with id <7> not found")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_400_not_found_in_favorite() throws Exception {
            mockMvc.perform(delete("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"2\"\n" +
                                    "}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("This kindergarten with id <2> not found in favorite")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql", "/sql/kindergarten.sql", "/sql/favorites.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_200_and_deleted_kindergarten() throws Exception {
            mockMvc.perform(delete("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"1\"\n" +
                                    "}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.city", is("Berlin")))
                    .andExpect(jsonPath("$.address", is("berlinstrasse")))
                    .andExpect(jsonPath("$.description", is("forest kindergarten")));


        }
    }

    @Nested
    @DisplayName("GET/api/users/profile/children:")
    public class GetAllChildren {

        @WithUserDetails(value = "USER")
        @Test
        @Sql(scripts = {"/sql/user.sql", "/sql/children.sql"})
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_200_and_list_children() throws Exception {
            mockMvc.perform(get("/api/users/profile/children"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.children.size()", is(2)))
                    .andExpect(jsonPath("$.children[0].id", is(1)))
                    .andExpect(jsonPath("$.children[0].firstName", is("Oksana")))
                    .andExpect(jsonPath("$.children[0].lastName", is("Petrova")))
                    .andExpect(jsonPath("$.children[1].id", is(3)))
                    .andExpect(jsonPath("$.children[1].firstName", is("Oleg")))
                    .andExpect(jsonPath("$.children[1].lastName", is("Petrov")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_for_not_not_found_user() throws Exception {
            mockMvc.perform(get("/api/users/profile/children"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("User with id <1> not found")));
        }

    }

    @Nested
    @DisplayName("POST/api/users/profile/children:")
    public class AddNewChildToUser {
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_201_and_added_child_to_user() throws Exception {
            mockMvc.perform(post("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"firstName\": \"Alisa\",\n" +
                                    "\"lastName\": \"Petrova\",\n" +
                                    "\"gender\": \"FEMALE\",\n" +
                                    "\"dateOfBirth\": \"2017-08-01\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.children[0].id", is(1)))
                    .andExpect(jsonPath("$.children[0].firstName", is("Alisa")))
                    .andExpect(jsonPath("$.children[0].lastName", is("Petrova")))
                    .andExpect(jsonPath("$.children[0].dateOfBirth", is("2017-08-01")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql", "/sql/children.sql"})
        public void return_409_child_already_exists() throws Exception {
            mockMvc.perform(post("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"firstName\": \"Oksana\",\n" +
                                    "\"lastName\": \"Petrova\",\n" +
                                    "\"gender\": \"FEMALE\",\n" +
                                    "\"dateOfBirth\": \"2020-01-09\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("Child with this data already exists ")));

        }
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_400_validation_error() throws Exception {
            mockMvc.perform(post("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"firstName\": \"Natalia\",\n" +
                                    "\"lastName\": \"\",\n" +
                                    "\"gender\": \"FEMALE\",\n" +
                                    "\"dateOfBirth\": \"2020-01-09\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isBadRequest());

        }

    }

    @Nested
    @DisplayName("PUT/api/users/profile/children:")
    public class UpdateChildInUser {
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql", "/sql/children.sql"})
        public void return_404_child_not_found() throws Exception {
            mockMvc.perform(put("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"id\": 5,\n" +
                                    "\"firstName\": \"Oksana\",\n" +
                                    "\"lastName\": \"Petrova\",\n" +
                                    "\"gender\": \"FEMALE\",\n" +
                                    "\"dateOfBirth\": \"2020-01-09\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Child with id<5> of user with id <1> not found")));


        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql", "/sql/children.sql"})
        public void return_409_child_already_exists() throws Exception {

            mockMvc.perform(put("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"id\": 1,\n" +
                                    "\"firstName\": \"Ivan\",\n" +
                                    "\"lastName\": \"Ivanov\",\n" +
                                    "\"gender\": \"MALE\",\n" +
                                    "\"dateOfBirth\": \"2018-08-12\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("Child with this data already exists ")));


        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/children.sql"})
        public void return_200_update_child_in_user() throws Exception {

            mockMvc.perform(put("/api/users/profile/children")
                            .contentType(MediaType.APPLICATION_JSON).content("{\n" +
                                    "\"id\": 1,\n" +
                                    "\"firstName\": \"Angela\",\n" +
                                    "\"lastName\": \"Isoldovna\",\n" +
                                    "\"gender\": \"FEMALE\",\n" +
                                    "\"dateOfBirth\": \"2018-09-01\"\n" +
                                    "\n" +
                                    "}"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(1)))
                    .andExpect(jsonPath("$.firstName", is("Angela")))
                    .andExpect(jsonPath("$.lastName", is("Isoldovna")))
                    .andExpect(jsonPath("$.dateOfBirth", is("2018-09-01")));
        }
    }

    @Nested
    @DisplayName("POST/api/users/profile/dialogues:")
    public class PostDialogues {

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_400_send_message_to_yourself() throws Exception {
            mockMvc.perform(post("/api/users/profile/dialogues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"recipientId\": 1,\n" +
                                    "  \"messageText\": \"Kurlyk! Kurlyk!\"\n" +
                                    "}")).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("Sorry, you can't send a message to yourself")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql"})
        public void return_404_send_message_to_not_exist_user() throws Exception {
            mockMvc.perform(post("/api/users/profile/dialogues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"recipientId\": 3,\n" +
                                    "  \"messageText\": \"Hello, World!\"\n" +
                                    "}")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("User with id <3> not found")));

        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql", "/sql/manager.sql"})
        public void return_201_for_created_dialogue() throws Exception {
            mockMvc.perform(post("/api/users/profile/dialogues")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"recipientId\": 3,\n" +
                                    "  \"messageText\": \"Hello, World!\"\n" +
                                    "}")).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.dialogues.size()", is(1)))
                    .andExpect(jsonPath("$.dialogues[0].recipient.id", is(3)))
                    .andExpect(jsonPath("$.dialogues[0].messages[0].messageText", is("Hello, World!")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_400_validation_error() throws Exception {
            mockMvc.perform(post("/api/users/profile/dialogues")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"recipientId\": 3,\n" +
                            "  \"messageText\": \"\"\n" +
                            "}")).andExpect(status().isBadRequest());

        }
    }

    @Nested
    @DisplayName("GET/api/users/profile/dialogues:")
    public class GetDialogues{

            @WithUserDetails(value = "USER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/dialogue.sql","/sql/user_dialogue.sql","/sql/message.sql"})
            public void return_200_for_created_dialogue() throws Exception {
                mockMvc.perform(get("/api/users/profile/dialogues"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.dialogues.size()", is(1)))
                        .andExpect(jsonPath("$.dialogues[0].recipient.id", is(3)))
                        .andExpect(jsonPath("$.dialogues[0].messages[0].messageText", is("Hello, World!")));
            }

            @WithUserDetails(value = "USER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            @Sql(scripts = {"/sql/user.sql","/sql/manager.sql"})
            public void return_200_for_empty_dialogues() throws Exception {
                mockMvc.perform(get("/api/users/profile/dialogues"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.dialogues.size()", is(0)));
            }

            @WithUserDetails(value = "USER")
            @Test
            @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
            public void return_404_not_found_user_dialogues() throws Exception {
                mockMvc.perform(get("/api/users/profile/dialogues"))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message", is("User with id <1> not found")));
            }


        }

    @Nested
    @DisplayName("POST/api/users/profile/requests:")
    public class PostRequests{
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql"})
        public void return_201_created_request() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"childId\": 1,\n" +
                                    "  \"kindergartenId\": 1\n" +
                                    "}")).andExpect(status().isCreated())
                    .andExpect(jsonPath("$.requests.size()", is(1)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(1)))
                    .andExpect(jsonPath("$.requests[0].childId", is(1)))
                    .andExpect(jsonPath("$.requests[0].status", is("NOT_CONFIRMED")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_403_forbidden_for_manager() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"childId\": 1,\n" +
                            "  \"kindergartenId\": 1\n" +
                            "}")).andExpect(status().isForbidden());

        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_409_request_with_this_data_already_exists() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"childId\": 1,\n" +
                                    "  \"kindergartenId\": 1\n" +
                                    "}")).andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("Request with this data already exists")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/children.sql"})
        public void return_400_child_not_found() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"childId\": 4,\n" +
                                    "  \"kindergartenId\": 1\n" +
                                    "}")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Child with id <4> not found")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql"})
        public void return_400_kindergarten_not_found() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"childId\": 1,\n" +
                                    "  \"kindergartenId\": 3\n" +
                                    "}")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten with id <3> not found")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql"})
        public void return_400_wrong_match_parent_with_child() throws Exception {
            mockMvc.perform(post("/api/users/profile/requests")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"childId\": 2,\n" +
                                    "  \"kindergartenId\": 1\n" +
                                    "}")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Child with id <2> not found in children of user with id <1>")));
        }






    }

    @Nested
    @DisplayName("GET/api/users/profile/requests:")
    public class GetRequests{

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_200_requests_list_for_user() throws Exception {
            mockMvc.perform(get("/api/users/profile/requests")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(4)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(2)))
                    .andExpect(jsonPath("$.requests[0].childId", is(1)))
                    .andExpect(jsonPath("$.requests[0].status", is("CONFIRMED")));
        }


        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_200_requests_list_for_manager() throws Exception {
            mockMvc.perform(get("/api/users/profile/requests")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(3)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(2)))
                    .andExpect(jsonPath("$.requests[0].childId", is(1)))
                    .andExpect(jsonPath("$.requests[0].status", is("CONFIRMED")));
        }


        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_requests_list_for_user() throws Exception {
            mockMvc.perform(get("/api/users/profile/requests")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("User with id <1> not found")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql"})
        public void return_404_kindergarten_not_fount() throws Exception {
            mockMvc.perform(get("/api/users/profile/requests")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten of manager with id <3>  not found")));
        }
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql"})
        public void return_200_empty_requests_list_for_user() throws Exception {
            mockMvc.perform(get("/api/users/profile/requests")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(0)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(0)));
        }


    }


    @Nested
    @DisplayName("PUT/api/users/profile/requests/{request_id}/reject:")
    public class PutRejectRequest{
        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_200_reject_request_by_user() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/2/reject")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(4)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(2)))
                    .andExpect(jsonPath("$.requests[1].childId", is(1)))
                    .andExpect(jsonPath("$.requests[1].status", is("REJECTED")));
        }


        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_200_reject_request_by_manager() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/2/reject")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(3)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(2)))
                    .andExpect(jsonPath("$.requests[1].childId", is(3)))
                    .andExpect(jsonPath("$.requests[1].status", is("REJECTED")));
        }


        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_404_request_not_found() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/10/reject")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Request with id <10> not found")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_400_request_already_rejected() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/3/reject")).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("Request is already rejected")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_400_no_available_requests_manager_side() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/4/reject")).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("There is no available requests for manager with id <3>")));
        }

        @WithUserDetails(value = "USER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_400_no_available_requests_user_side() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/5/reject")).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("There is no available requests for user with id <1>")));
        }


    }

    @Nested
    @DisplayName("PUT/api/users/profile/requests/{request_id}/confirm:")
    public class PutConfirmRequest{


        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_200_confirm_request_by_manager() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/2/confirm")).andExpect(status().isOk())
                    .andExpect(jsonPath("$.requests.size()", is(3)))
                    .andExpect(jsonPath("$.childWithUserList.size()", is(2)))
                    .andExpect(jsonPath("$.requests[1].childId", is(3)))
                    .andExpect(jsonPath("$.requests[1].status", is("CONFIRMED")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_manager_with_id_not_found() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/2/confirm")).andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("User with id <3> not found")));
        }

        @WithUserDetails(value = "MANAGER")
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        @Sql(scripts = {"/sql/user.sql","/sql/manager.sql","/sql/children.sql","/sql/kindergarten.sql","/sql/request.sql"})
        public void return_400_request_already_rejected() throws Exception {
            mockMvc.perform(put("/api/users/profile/requests/3/confirm")).andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("Request is already rejected")));
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

