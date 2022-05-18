package pl.aswit.theatre.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // prettier-ignore
        http
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/**").permitAll()
            .and()
            .formLogin().disable()
            .httpBasic().disable()
            .logout().disable();
    }
}
