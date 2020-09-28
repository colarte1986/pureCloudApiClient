package com.segurosbolivar.bienestar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.InMemoryReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.*;

@Configuration
public class PureCloudClientConfig {
	
	@Bean
	ReactiveClientRegistrationRepository getRegistration(
			@Value("${spring.security.oauth2.client.provider.local.token-uri}") String tokenUri,
			@Value("${spring.security.oauth2.client.registration.local.client-id}") String clientId,
			@Value("${spring.security.oauth2.client.registration.local.client-secret}") String clientSecret) {
		
		ClientRegistration registration = ClientRegistration.withRegistrationId("local").tokenUri(tokenUri)
				.clientId(clientId).clientSecret(clientSecret)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS).build();
		return new InMemoryReactiveClientRegistrationRepository(registration);
	
	}

	@Bean(name = "my-platform")
	WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations) {
		
		InMemoryReactiveOAuth2AuthorizedClientService clientService = new InMemoryReactiveOAuth2AuthorizedClientService(
				clientRegistrations);
		AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(
				clientRegistrations, clientService);
		ServerOAuth2AuthorizedClientExchangeFilterFunction oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
				authorizedClientManager);
		oauth.setDefaultClientRegistrationId("local");
		return WebClient.builder().filter(oauth)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	
	}

}
