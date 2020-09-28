package com.segurosbolivar.bienestar;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.segurosbolivar.bienestar.model.PureCloudRequest;

@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PureCloudRestClientApplicationTests {

	@Autowired
	private WebTestClient webClient;

	@Test
	void getPureCloudOkResponse() {

		PureCloudRequest pureCloudRequest = new PureCloudRequest();
		pureCloudRequest.setDocumentNumber("1144078370");
		pureCloudRequest.setDocumentType("CC");
		ArrayList<String> callbackNumbers = new ArrayList<>();
		callbackNumbers.add("0033168164208");
		callbackNumbers.add("00743754547");
		pureCloudRequest.setCallbackNumbers(callbackNumbers);

		webClient.post().uri("/purecloud/callback").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(pureCloudRequest)).exchange().expectStatus().is2xxSuccessful();

	}

	@Test
	void getCallBackWithoutDocumentNumber() {
		PureCloudRequest pureCloudRequest = new PureCloudRequest();
		pureCloudRequest.setDocumentType("CC");
		ArrayList<String> callbackNumbers = new ArrayList<>();
		callbackNumbers.add("0033168164208");
		callbackNumbers.add("00743754547");
		pureCloudRequest.setCallbackNumbers(callbackNumbers);

		webClient.post().uri("/purecloud/callback").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(pureCloudRequest)).exchange().expectBody().jsonPath("$.body.status").isEqualTo("FAILURE");

	}
	
	@Test
	void getCallBackWithoutDocumentType() {
		PureCloudRequest pureCloudRequest = new PureCloudRequest();
		pureCloudRequest.setDocumentNumber("1144078370");
		ArrayList<String> callbackNumbers = new ArrayList<>();
		callbackNumbers.add("0033168164208");
		callbackNumbers.add("00743754547");
		pureCloudRequest.setCallbackNumbers(callbackNumbers);

		webClient.post().uri("/purecloud/callback").contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(pureCloudRequest)).exchange().expectBody().jsonPath("$.body.status").isEqualTo("FAILURE");
	}
	
	@Test
	void getCallBackWithoutCallBackNumbers() {
		PureCloudRequest pureCloudRequest = new PureCloudRequest();
		pureCloudRequest.setDocumentNumber("1144078370");
		pureCloudRequest.setDocumentType("CC");
		
		webClient.post().uri("/purecloud/callback").contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromObject(pureCloudRequest)).exchange().expectBody().jsonPath("$.body.status").isEqualTo("FAILURE");
	}

}
