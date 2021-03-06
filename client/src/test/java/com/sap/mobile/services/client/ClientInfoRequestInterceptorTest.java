package com.sap.mobile.services.client;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

public class ClientInfoRequestInterceptorTest {

	private ClientInfoRequestInterceptor testee;

	@Before
	public void setUp() throws Exception {
		testee = new ClientInfoRequestInterceptor(BuildProperties.getInstance());
	}

	@Test
	public void testIntercept() throws Exception {
		HttpRequest request = Mockito.mock(HttpRequest.class);
		ClientHttpRequestExecution execution = Mockito.mock(ClientHttpRequestExecution.class);
		ClientHttpResponse response = Mockito.mock(ClientHttpResponse.class);

		HttpHeaders headers = new HttpHeaders();

		Mockito.when(request.getHeaders()).thenReturn(headers);
		Mockito.when(execution.execute(request, null)).thenReturn(response);

		Assert.assertSame(response, testee.intercept(request, null, execution));
		Assert.assertNotNull(headers.getFirst(Constants.Headers.CLIENT_VERSION_HEADER_NAME));
		Assert.assertEquals("Java", headers.getFirst(Constants.Headers.CLIENT_LANGUAGE_HEADER_NAME));

		Mockito.verify(execution).execute(request, null);
	}
}
