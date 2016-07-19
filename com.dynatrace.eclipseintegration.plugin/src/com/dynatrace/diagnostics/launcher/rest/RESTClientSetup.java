package com.dynatrace.diagnostics.launcher.rest;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlRootElementProvider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * @author Michal.Weyer
 * @since 2016-04-08
 * @see <a href="http://stackoverflow.com/questions/18894860/for-rest-easy-https-calls-how-to-accept-all-certs" />
 */
class RESTClientSetup {

	ResteasyClient createClient() {
		ResteasyClient client = new ResteasyClientBuilder()
				.connectionPoolSize(2)
				.httpEngine( new ApacheHttpClient4Engine(trustAllCerts()))
				.build();

		ResteasyProviderFactory instance = ResteasyProviderFactory.getInstance();
		RegisterBuiltin.register(instance);
		instance.registerProvider(JAXBXmlRootElementProvider.class);

		return client;
	}

	private HttpClient trustAllCerts() {

		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			return HttpClientBuilder.create()
					.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
					.setSSLContext(sslContext).build();
		} catch (Exception e) {
			throw new RuntimeException("[ErrorLocation-35]", e);
		}
	}
}
