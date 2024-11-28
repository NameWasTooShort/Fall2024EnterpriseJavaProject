package ca.sheridancollege.zhaoste.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	// Logins:
	// User: user@test.ca 1234
	// Guest: guest@test.ca 123
	// Admin: admin@test.ca 1234
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
		return http
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(mvc.pattern("/secure/**")).hasRole("ADMIN")
						.requestMatchers(mvc.pattern("/")).permitAll()
						.requestMatchers(mvc.pattern("/js/**")).permitAll()
						.requestMatchers(mvc.pattern("/css/**"))
						.permitAll().requestMatchers(mvc.pattern("/images/**")).permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/secure/**")).permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/secure/**")).permitAll()
						.requestMatchers(mvc.pattern("/permission-denied")).permitAll()
						.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
						.requestMatchers(mvc.pattern("/**")).hasRole("USER"))
				.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"), AntPathRequestMatcher.antMatcher("/secure/**"))
						.disable())
				.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
				.formLogin(form -> form.loginPage("/login").permitAll())
				.exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
				.logout(logout -> logout.permitAll()).build();
	}
}
