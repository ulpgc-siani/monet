package org.monet.mocks.singlesignon;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.monet.mocks.singlesignon.core.Configuration;
import org.monet.mocks.singlesignon.service.federation.FederationSetupLayer;
import org.scribe.model.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class ServiceTest {

	private static final String AUTHORIZATION_URL_PARAMS = "&username=admin&password=1234&deviceId=1000";

	@Test
	public void requestToken() {
		Service service = getService();
		Token requestToken = service.requestToken();
		assertNotNull(requestToken);
	}

	@Test
	public void accessToken() {
		Service service = getService();
		Token requestToken = service.requestToken();
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		String verifier = getVerifier(navigate(getAutoAuthorizationUrl(authorizationUrl), requestToken));
		Token accessToken = service.getAccessToken(requestToken, verifier);
		assertNotNull(accessToken);
	}

	@Test
	public void isLogged() {
		Service service = getService();
		Token requestToken = service.requestToken();
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		String verifier = getVerifier(navigate(getAutoAuthorizationUrl(authorizationUrl), requestToken));
		Token accessToken = service.getAccessToken(requestToken, verifier);
		assertTrue(service.isLogged(accessToken, "127.0.0.1", "apache-httpclient/4.3.2 (java 1.5)"));
	}

	@Test
	public void notLogged() {
		Service service = getService();
		Token requestToken = service.requestToken();
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		String verifier = getVerifier(navigate(getAutoAuthorizationUrl(authorizationUrl), requestToken));
		Token accessToken = service.getAccessToken(requestToken, verifier);
		service.logout(accessToken, "127.0.0.1", "apache-httpclient/4.3.2 (java 1.5)");
		assertFalse(service.isLogged(accessToken, "127.0.0.1", "apache-httpclient/4.3.2 (java 1.5)"));
	}

	private String getAutoAuthorizationUrl(String authorizationUrl) {
		return authorizationUrl.replace("/authorization/", "/autoauthorization/");
	}

	private String navigate(String authorizationUrl, Token requestToken) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response;

		try {
			authorizationUrl += AUTHORIZATION_URL_PARAMS;

			HttpPost post = new HttpPost(authorizationUrl);
			response = client.execute(post);

			int status = response.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300)
				throw new HttpException(String.format("%s => %d - %s", authorizationUrl, status, response.getStatusLine().getReasonPhrase()));

			return getReaderContent(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}

		return null;
	}

	private String getVerifier(String httpResult) {
		Pattern pattern = Pattern.compile("<verifier>([^ >]*)</verifier>");
		Matcher matcher = pattern.matcher(httpResult);
		if (!matcher.find()) return null;
		return matcher.group(1);
	}

	private Service getService() {
		Configuration configuration = getConfiguration();
		return new SingleSignOnService(configuration, new FederationSetupLayer(configuration));
	}

	private String getReaderContent(Reader oReader) throws IOException {
		StringBuffer sbContent = new StringBuffer();
		BufferedReader oBufferedReader;
		String sLine;

		oBufferedReader = new BufferedReader(oReader);
		while ((sLine = oBufferedReader.readLine()) != null) {
			sbContent.append(sLine + "\n");
		}

		return sbContent.toString();
	}

	private Configuration getConfiguration() {
		return new Configuration() {

			@Override
			public String getFederationUrl() {
				return "http://localhost:8080/federation";
			}

			@Override
			public String getCertificateFilename() {
				return "/Users/mcaballero/.monet/certificates/businessunit-monet.p12";
			}

			@Override
			public String getCertificatePassword() {
				return "1234";
			}

			@Override
			public String getUnitName() {
				return "monet";
			}

			@Override
			public String getUnitUrl() {
				return "http://localhost:8080/monet";
			}

			@Override
			public String getUnitCallbackUrl() {
				return "http://localhost:8080/monet/office";
			}

			@Override
			public String getUnitKey() {
				return "monet";
			}

			@Override
			public String getUnitSecret() {
				return "1234";
			}

		};
	}
}
