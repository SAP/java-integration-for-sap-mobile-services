package com.sap.mobile.services.client.push.mock;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PACKAGE)
public class ResponseDefinition<T extends ResponseDefinition<?>> {

	private final MockPushServer pushServer;
	private HttpStatus status = HttpStatus.ACCEPTED;
	private String body;

	public T withStatus(final HttpStatus status) {
		this.status = status;
		return getThis();
	}

	public T withJsonBody(final String jsonString) {
		this.body = jsonString;
		return getThis();
	}

	public T withJsonBodyFromResource(final String resourceName) {
		try (final InputStream stream = getClass().getClassLoader().getResourceAsStream(resourceName)) {
			return withJsonBody(IOUtils.toString(stream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load resource", e);
		}
	}

	private T getThis() {
		return (T) this;
	}

	public MockPushServer and() {
		return pushServer;
	}
}
