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


    }

    @Nested
    @DisplayName("GET/api/users/profile/favorites:")
    public class GetUserFavoritesKindergartens {


        @WithUserDetails(value = "user")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_for_not_existed_favorites_kindergartens() throws Exception {
            mockMvc.perform(get("/api/users/profile/favorites"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Not found favorite kindergartens")));


        }

        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_200_and_list_favorites_kindergartens() throws Exception {
            mockMvc.perform(get("/api/users/profile/favorites"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.kindergartens.size()", is(2)))
                    .andExpect(jsonPath("$.kindergartens[0].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[0].address", is("berlinstrasse")))
                    .andExpect(jsonPath("$.kindergartens[0].description", is("forest kindergarten")))
                    .andExpect(jsonPath("$.kindergartens[1].city", is("Munchen")))
                    .andExpect(jsonPath("$.kindergartens[1].address", is("strasse50")))
                    .andExpect(jsonPath("$.kindergartens[1].description", is("very god kindergarten")));


        }
    }

    @Nested
    @DisplayName("POST/api/users/profile/favorites:")
    public class AddUserFavoritesKindergartens {


        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_409_for_kindergarten_already_added_to_favorites() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"2\"\n" +
                                    "}"))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message", is("This kindergarten has already been added to the user")));

        }

        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_201_and_list_favorites_kindergartens() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"3\"\n" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.kindergartens.size()", is(3)))
                    .andExpect(jsonPath("$.kindergartens[0].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[0].address", is("berlinstrasse")))
                    .andExpect(jsonPath("$.kindergartens[0].description", is("forest kindergarten")))
                    .andExpect(jsonPath("$.kindergartens[1].city", is("Munchen")))
                    .andExpect(jsonPath("$.kindergartens[1].address", is("strasse50")))
                    .andExpect(jsonPath("$.kindergartens[1].description", is("very god kindergarten")))
                    .andExpect(jsonPath("$.kindergartens[2].city", is("Berlin")))
                    .andExpect(jsonPath("$.kindergartens[2].address", is("fake")))
                    .andExpect(jsonPath("$.kindergartens[2].description", is("fake kindergarten")));


        }


        @WithUserDetails(value = "user")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_not_found_kindergarten() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"7\"\n" +
                                    "}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten with id<7> not found")));

        }
    }

    @Nested
    @DisplayName(" DELETE  /api/users/profile/favorites:")
    public class DeleteUserFavoritesKindergartens {

        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_404_not_found_kindergarten() throws Exception {
            mockMvc.perform(post("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"7\"\n" +
                                    "}"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message", is("Kindergarten with id<7> not found")));

        }

        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_400_not_found_in_favorite() throws Exception {
            mockMvc.perform(delete("/api/users/profile/favorites")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"kindergartenId\": \"3\"\n" +
                                    "}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", is("This kindergarten with id<3> not found in favorite")));

        }

        @WithUserDetails(value = "anna")
        @Test
        @Sql(scripts = "/sql/data.sql")
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