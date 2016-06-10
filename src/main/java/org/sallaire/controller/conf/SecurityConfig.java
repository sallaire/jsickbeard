package org.sallaire.controller.conf;

import org.sallaire.dao.db.entity.User.Role;
import org.sallaire.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private UserService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		LOGGER.info("Setting up connection configuration");
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setUserDetailsService(userDetailsService);
		auth.authenticationProvider(authProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.httpBasic() //
				.and().authorizeRequests() //
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll() // allow CORS option calls
				// .antMatchers("/admin/**").hasRole(Role.SYSADMIN.name()) //
				.antMatchers("/settings/**").hasRole(Role.ADMIN.name()) //
				.antMatchers("/user/**").hasRole(Role.USER.name()) //
				.antMatchers("/episode/**").hasRole(Role.USER.name()) //
				.antMatchers("/tvshows/**").hasRole(Role.USER.name()) //
				.antMatchers("/tvshow/**").hasRole(Role.USER.name());
	}
}
