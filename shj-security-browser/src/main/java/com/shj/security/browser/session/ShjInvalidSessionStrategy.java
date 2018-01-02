/**
 * 
 */
package com.shj.security.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.stereotype.Component;

/**
 * @author zhailiang
 *
 */
public class ShjInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

	public ShjInvalidSessionStrategy(String invalidSessionUrl) {
		super(invalidSessionUrl);
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		onSessionInvalid(request, response);
	}

}
