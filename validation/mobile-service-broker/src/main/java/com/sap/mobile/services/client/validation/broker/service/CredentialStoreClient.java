package com.sap.mobile.services.client.validation.broker.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.IOUtils;
import com.sap.mobile.services.client.validation.broker.misc.OAuthTokenProvider;

import io.pivotal.cfenv.core.CfEnv;
import lombok.Getter;
import lombok.SneakyThrows;

@Service
public class CredentialStoreClient {

	private final RestTemplate restTemplate;

	public CredentialStoreClient(final CfEnv cfEnv, final RestTemplateBuilder restTemplateBuilder) {
		final Map<String, ?> credentials = cfEnv.findCredentialsByLabel("credstore").getMap();
		this.restTemplate = buildRestTemplate(credentials, restTemplateBuilder);
	}

	public Optional<PasswordCredential> findPasswordCredential(final String name) {
		try {
			final ResponseEntity<PasswordCredential> response = this.restTemplate.getForEntity("/password/?name={name}", PasswordCredential.class, name);
			return Optional.of(response.getBody());
		} catch (HttpClientErrorException.NotFound e) {
			return Optional.empty();
		}
	}

	@SneakyThrows
	private static RestTemplate buildRestTemplate(final Map<String, ?> credentialMap, final RestTemplateBuilder restTemplateBuilder) {
		final URI baseUri = URI.create(MapUtils.getString(credentialMap, "url"));
		final URI tokenUri = URI.create(MapUtils.getString(credentialMap, "oauth_token_url"));
		final String certificate = MapUtils.getString(credentialMap, "certificate");
		final String key = MapUtils.getString(credentialMap, "key");
		final String clientId = MapUtils.getString(credentialMap, "username");

		final String decryptionKey = MapUtils.getString((Map<String, ?>) MapUtils.getMap(credentialMap, "encryption"), "client_private_key");

		final OAuthTokenProvider tokenProvider = OAuthTokenProvider.builder()
				.tokenUrl(tokenUri)
				.clientId(clientId)
				.certificate(certificate)
				.key(key)
				.build();

		final JWK jwk = JWK.parseFromPEMEncodedObjects("-----BEGIN PRIVATE KEY-----\n" + decryptionKey + "\n-----END PRIVATE KEY-----");
		final JWEDecrypter jweDecrypter = new RSADecrypter((RSAKey) jwk);

		return restTemplateBuilder
				.rootUri(baseUri.toString())
				.defaultHeader("sapcp-credstore-namespace", "default")
				.additionalInterceptors((request, body, execution) -> {
					final String token = tokenProvider.getAccessToken();
					request.getHeaders().add(HttpHeaders.AUTHORIZATION, String.format("bearer %s", token));
					return execution.execute(request, body);
				}).additionalInterceptors((request, body, execution) -> {
					final ClientHttpResponse response = execution.execute(request, body);
					if (response.getHeaders().getContentType().includes(MediaType.parseMediaType("application/jose"))) {
						return new DecryptionClientHttpResponseWrapper(response, jweDecrypter);
					} else {
						return response;
					}
				}).build();
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	public static class PasswordCredential {
		@JsonProperty("username")
		private String usermame;

		@JsonProperty("value")
		private String password;
	}

	private static class DecryptionClientHttpResponseWrapper implements ClientHttpResponse {
		private final ClientHttpResponse delegate;
		@Getter
		private final InputStream body;

		private DecryptionClientHttpResponseWrapper(final ClientHttpResponse delegate, final JWEDecrypter decrypter) throws IOException {
			try {
				final String responseBody = IOUtils.readInputStreamToString(delegate.getBody());
				final JWEObject jwe = JWEObject.parse(responseBody);
				jwe.decrypt(decrypter);
				final byte[] decryptedBody = jwe.getPayload().toBytes();

				this.delegate = delegate;
				this.body = new ByteArrayInputStream(decryptedBody);
				delegate.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			} catch (ParseException | JOSEException e) {
				throw new IOException("Failed to decrypt response data", e);
			}
		}

		@Override
		public HttpStatus getStatusCode() throws IOException {
			return delegate.getStatusCode();
		}

		@Override
		public int getRawStatusCode() throws IOException {
			return delegate.getRawStatusCode();
		}

		@Override
		public String getStatusText() throws IOException {
			return delegate.getStatusText();
		}

		@Override
		public void close() {
			delegate.close();
		}

		@Override
		public HttpHeaders getHeaders() {
			return delegate.getHeaders();
		}
	}
}
