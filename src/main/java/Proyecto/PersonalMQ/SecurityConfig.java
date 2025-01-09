package Proyecto.PersonalMQ;

import Proyecto.PersonalMQ.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// Desactiva la protección CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/css/**","/js/**").permitAll()//Rutas publicas
                        .anyRequest().authenticated()//Todas las demas requieren autenticacion
                )
                .formLogin(form -> form
                .loginPage("/login")//Pagina de login
                                .defaultSuccessUrl("/",true)//Redireccion tras login exitoso
                                .failureUrl("/login?error")//Redireccion tras error
                                .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")//Url para cerrar sesion
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );
        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        //Desabilitar la encriptacion temporalmente
        return NoOpPasswordEncoder.getInstance();
    }

    /*
    @Bean
    public AuthenticationManagerBuilder authenticationManagerBuilder(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("1234")
                .roles("ADMIN");
        return auth;
    }
    */

}
