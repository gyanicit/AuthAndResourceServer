package in.icitinstitute.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import in.icitinstitute.constant.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private ClientDetailsService clientDetailsService;
	
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
		authenticationManagerBuilder.inMemoryAuthentication()
		.withUser("admin").password("{noop}adminpass").roles("ADMIN","USER")
		.and().withUser("gyan").password("{noop}gyanpass").roles("ADMIN","USER")
		.and().withUser("user").password("{noop}userpass").roles("ADMIN","USER");
	}
	
	@Override
	@Order(Ordered.HIGHEST_PRECEDENCE)
	protected void configure(HttpSecurity httpSecurity) throws Exception{
		httpSecurity
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/oath/token").permitAll()
		.antMatchers("/check_token").permitAll()
		.anyRequest().authenticated()
		.and()
		.httpBasic().realmName(Constants.REALM);
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
	
	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
	TokenStoreUserApprovalHandler approvalHandler=new TokenStoreUserApprovalHandler();
		approvalHandler.setTokenStore(tokenStore);
		approvalHandler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		approvalHandler.setClientDetailsService(clientDetailsService);
		return approvalHandler;
	}
	
	@Bean
	@Autowired
	public ApprovalStore approvalStore(TokenStore tokenStore) {
		TokenApprovalStore store=new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}
}
