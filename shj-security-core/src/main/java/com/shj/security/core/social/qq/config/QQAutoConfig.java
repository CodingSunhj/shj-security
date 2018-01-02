/**
 * 
 */
package com.shj.security.core.social.qq.config;

import com.shj.security.core.properties.QQProperties;
import com.shj.security.core.properties.SecurityProperties;
import com.shj.security.core.social.ShjConnectView;
import com.shj.security.core.social.qq.connet.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import javax.swing.text.View;


/**
 *
 */
@Configuration
@ConditionalOnProperty(prefix = "shj.security.social.qq", name = "appId")	//此注解作用：只有配置中配置了相关信息，才生效
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qqConfig = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qqConfig.getProviderId(), qqConfig.getAppId(), qqConfig.getAppSecret());
	}

	@Bean("connect/qqConnected")
	@ConditionalOnMissingBean(name = "qqConnectedView")
	public ShjConnectView qqConnected(){
		return new ShjConnectView();
	}

}
