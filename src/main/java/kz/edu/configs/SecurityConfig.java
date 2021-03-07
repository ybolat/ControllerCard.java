package kz.edu.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    @Qualifier("userDetailsService")
    UserDetailsService userDetailsService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity security) throws Exception
    {
        security.authorizeRequests()
                .antMatchers("/", "/registration").permitAll()
                .antMatchers("/question").hasAnyRole("USER", "ADMIN")
                .antMatchers("/question/create").hasRole("ADMIN")
                .antMatchers("/question/change").hasRole("ADMIN")
                .antMatchers("/question/delete").hasRole("ADMIN")
                .antMatchers("/question/vote").hasAnyRole("ADMIN", "USER")
                .antMatchers("/change_password").hasAnyRole("USER", "ADMIN")
                .antMatchers("/profile").hasAnyRole("USER", "ADMIN")
                .antMatchers("/change_role").hasRole("ADMIN")
                .antMatchers("/question/fullStatistic").hasAnyRole("USER", "ADMIN")
            .and()
                //.formLogin().permitAll()
                .formLogin().loginPage("/login").permitAll()
            .and()
                .logout().permitAll()
            .and()
                .csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}