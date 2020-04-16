package in.icitinstitute.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import in.icitinstitute.constant.Constants;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter{
	
	@Override
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.anyRequest().authenticated()
		.and()
		.httpBasic().realmName(Constants.CRM_REALM);
	}
	
	@Override
	public void configure(ResourceServerSecurityConfigurer resourceServerSecurityConfigurer) throws Exception{
		resourceServerSecurityConfigurer.resourceId(null);
	}

}
