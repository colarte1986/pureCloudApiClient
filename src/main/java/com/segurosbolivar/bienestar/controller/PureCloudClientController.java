package com.segurosbolivar.bienestar.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.segurosbolivar.bienestar.model.Body;
import com.segurosbolivar.bienestar.model.Error;
import com.segurosbolivar.bienestar.model.PureCloudRequest;
import com.segurosbolivar.bienestar.model.PureCloudResponse;
import com.segurosbolivar.bienestar.utils.Utilities;

import reactor.core.publisher.Mono;

@RestController
public class PureCloudClientController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PureCloudClientController.class);
	
	private WebClient webClient;
	
	@Value("${purecloud.url}")
	protected String url_api;

	@Autowired
	void PlatformServiceClientImpl(@Qualifier("my-platform") WebClient webClient) {
		this.webClient = webClient;
	}

	@PostMapping(value = "/purecloud/callback", produces = MediaType.APPLICATION_JSON_VALUE)
	public PureCloudResponse getResponse(@RequestBody PureCloudRequest pureCloudRequest) {

		Utilities utils = new Utilities();

		var errorCode = "";
		var errorMessage = "";
		var hasError = false;

		if (pureCloudRequest.getDocumentType() == null || pureCloudRequest.getDocumentType().trim().isBlank()) {
			errorCode = "100";
			errorMessage = "DocumentType is mandatory";
			hasError = true;
		}

		if (pureCloudRequest.getDocumentNumber() == null || pureCloudRequest.getDocumentNumber().trim().isBlank()) {
			errorCode = "101";
			errorMessage = "DocumentNumber is mandatory";
			hasError = true;
		}

		if (pureCloudRequest.getCallbackNumbers() == null || pureCloudRequest.getCallbackNumbers().isEmpty()) {
			errorCode = "102";
			errorMessage = "CallBackNumber is mandatory";
			hasError = true;
		}

		PureCloudResponse pureCloudResponse = null;

		if (!hasError) {
			try {
				String requestBody = utils.getRequestBody(pureCloudRequest);
				Mono<String> response = webClient.post().uri(url_api)
						.body(BodyInserters.fromValue(requestBody)).exchange().flatMap(x -> {
							if (!x.statusCode().is2xxSuccessful())
								return Mono.just("ERROR");
							return Mono.just("SUCCESS");
						});
				
				if (response.block().equals("SUCCESS")) {
					Body body = new Body("SUCCESS");
					pureCloudResponse = new PureCloudResponse(body, null);
				} else {
					Body body = new Body("FAILURE");
					Error error = new Error("500", "We are facing issues to call PureCloud, try later");
					List<Error> errorList = new ArrayList<>();
					errorList.add(error);
					pureCloudResponse = new PureCloudResponse(body, errorList);
				}		
			} catch (Exception e) {
				LOGGER.error("Exception happens while trying to call Pure Cloud service endpoint", e.getMessage());
			}		
		} else {
			Body body = new Body("FAILURE");
			Error error = new Error(errorCode, errorMessage);
			List<Error> errorList = new ArrayList<>();
			errorList.add(error);
			pureCloudResponse = new PureCloudResponse(body, errorList);
		}
		return pureCloudResponse;
	}

}
