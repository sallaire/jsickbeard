package org.sallaire.provider.t411;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class AuthenticationInterceptor implements ClientHttpRequestInterceptor {

	private String token = null;

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		if (token != null) {
			request.getHeaders().add("Authorization", token);
		}
		return execution.execute(request, body);

	}

	public void setToken(String token) {
		this.token = token;
	}

}
