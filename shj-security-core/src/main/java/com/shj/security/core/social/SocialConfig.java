/**
 * 
 */
package com.shj.security.core.social;

import javax.sql.DataSource;

import com.shj.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.social.SocialWebAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;


/**
 * @author zhailiang
 *
 */
@EnableSocial
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecurityProperties securityProperties;
	
	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;

	@Autowired(required = false)
	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		return usersConnectionRepository(connectionFactoryLocator);
	}

	@Bean
	public SpringSocialConfigurer shjSocialSecurityConfigure() {
		String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
		ShjSpringSocialConfigurer configurer = new ShjSpringSocialConfigurer(filterProcessesUrl);
		configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
		configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl()); //sign-up的页面，回调后，如果需要到注册页，此处设置自己的注册页面
		return configurer;
	}

	//解决了在注册过程中拿到第三方返回的用户信息,并将用户的id（唯一标识）返回给SpringSocial
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
		return new ProviderSignInUtils(connectionFactoryLocator,
				getUsersConnectionRepository(connectionFactoryLocator)) {
		};
	}

	@Bean
	public UsersConnectionRepository usersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator){
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		repository.setTablePrefix("shj_");	//表名userconnection不能改变但是可以加前缀
		if(connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}
}
