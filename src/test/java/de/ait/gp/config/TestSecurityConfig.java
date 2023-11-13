package de.ait.gp.config;

import de.ait.gp.dto.Role;
import de.ait.gp.models.User;
import de.ait.gp.secutity.details.AuthenticatedUser;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.time.LocalDate;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    public static final String USER = "USER";

    public static final String MANAGER = "MANAGER";


    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                if (username.equals(USER)) {
                    return new AuthenticatedUser(User.builder()
                            .id(1L)
                            .email("user1@gmail.com")
                            .role(Role.USER)
                            .firstName("Sergey")
                            .lastName("Sedakov")
                            .address("Berlinstrasse7")
                            .city("Berlin")
                            .dateOfBirth(LocalDate.of(1998, 3, 8))
                            .phone("+4912451219")
                            .postcode("45129")
                            .hashPassword("$2a$04$YFDmGcB9H317JRzplPdezOll9xdkNyJtfX.eU/dNGlT4.aHho63nW")
                            .state(User.State.CONFIRMED)
                            .build());
                } else if (username.equals(MANAGER)) {
                    return new AuthenticatedUser(User.builder()
                            .id(3L)
                            .email("manager3@gmail.com")
                            .role(Role.MANAGER)
                            .firstName("Ivan")
                            .lastName("Petrov")
                            .address("Essenstrasse15")
                            .city("Essen")
                            .dateOfBirth(LocalDate.of(1975, 5, 1))
                            .phone("+4912354657")
                            .postcode("1111")
                            .hashPassword("$2a$04$YFDmGcB9H317JRzplPdezOll9xdkNyJtfX.eU/dNGlT4.aHho63nW")
                            .state(User.State.CONFIRMED)
                            .build());
                } else throw new UsernameNotFoundException("User not found");
            }
        };
    }
}