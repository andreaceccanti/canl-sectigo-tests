package it.infn.test;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.italiangrid.voms.util.CertificateValidatorBuilder;

import eu.emi.security.authn.x509.NamespaceCheckingMode;
import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.impl.SocketFactoryCreator;

public class CanlHttpClientFactory {

  public static final String TLS_1_2 = "TLSv1.2";

  String tlsVersion = TLS_1_2;
  NamespaceCheckingMode nsMode = NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS;

  public CanlHttpClientFactory() {}


  public HttpClient buildHttpClient() throws NoSuchAlgorithmException {

    SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext());

    Registry<ConnectionSocketFactory> socketFactoryRegistry =
        RegistryBuilder.<ConnectionSocketFactory>create()
          .register("https", sf)
          .register("http", PlainConnectionSocketFactory.getSocketFactory())
          .build();

    PoolingHttpClientConnectionManager connectionManager =
        new PoolingHttpClientConnectionManager(socketFactoryRegistry);
    connectionManager.setMaxTotal(10);
    connectionManager.setDefaultMaxPerRoute(10);

    return HttpClientBuilder.create()
      .setConnectionManager(connectionManager)
      .disableAuthCaching()
      .build();
  }


  X509CertChainValidatorExt certificateValidator() {

    return new CertificateValidatorBuilder().lazyAnchorsLoading(false)
      .namespaceChecks(nsMode)
      .build();
  }

  SSLContext sslContext() throws NoSuchAlgorithmException {

    SecureRandom r = new SecureRandom();

    try {
      SSLContext context = SSLContext.getInstance(tlsVersion);

      X509TrustManager tm = SocketFactoryCreator.getSSLTrustManager(certificateValidator());
      context.init(null, new TrustManager[] {tm}, r);

      return context;

    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  public void setNsMode(NamespaceCheckingMode nsMode) {
    this.nsMode = nsMode;
  }

  public void setTlsVersion(String tlsVersion) {
    this.tlsVersion = tlsVersion;
  }
}
