package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private  ImplementacaoUserDetailService impl;
	// mapeamentos de urls // Controle do ususario /// especificar algum bloqueio

	
	// 
	@Override // Configura as solicitações de acesso por HTTP
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable() // Desabilitando as configurações padrão de memória do sistema.
				.authorizeRequests()// Permitir restringir acessos
				.antMatchers(HttpMethod.GET, "/").permitAll() // Qualque usuario acessa a página inicial
				.antMatchers(HttpMethod.GET, "/telefones.html").hasAnyRole("ADMIN")
				.anyRequest().authenticated().and().formLogin().permitAll() // Permite qualquer usuario
				.and().logout() // Mapeia url de sair do sistema e invalida usuario autenticado
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(impl)
		.passwordEncoder(new BCryptPasswordEncoder());
		
//		// Cria autenticação do usuário com o banco de dados ou em memória
//		auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("vinicius")
//				.password("$2a$10$5YTLXTmPSpxrK72b1Ecyh.N5Qo1y9aT0qEJGrtiu/zA3qwIdgU20.").roles("ADMIN");

	}

	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/materialize/**");
	}

}
