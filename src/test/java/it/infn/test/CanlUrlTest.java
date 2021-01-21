package it.infn.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;

import eu.emi.security.authn.x509.NamespaceCheckingMode;

public class CanlUrlTest {

  public static final String SECTIGO_CERT_URL =
      "https://chnet-iam.cloud.cnaf.infn.it/.well-known/openid-configuration";

  public static final String DIGICERT_URL =
      "https://iam-escape.cloud.cnaf.infn.it/.well-known/openid-configuration";


  private HttpClient canlNsHttpClient;
  private HttpClient canlNoNsHttpClient;
  private HttpClient plainHttpClient;

  private HttpGet sectigoReq = new HttpGet(SECTIGO_CERT_URL);
  private HttpGet digicertReq = new HttpGet(DIGICERT_URL);

  @Before
  public void setup() throws NoSuchAlgorithmException {

    CanlHttpClientFactory chcf = new CanlHttpClientFactory();
    chcf.setNsMode(NamespaceCheckingMode.EUGRIDPMA_AND_GLOBUS);

    canlNsHttpClient = chcf.buildHttpClient();

    chcf.setNsMode(NamespaceCheckingMode.IGNORE);

    canlNoNsHttpClient = chcf.buildHttpClient();

    plainHttpClient = HttpClients.createDefault();
  }


  @Test
  public void testSectigoUrlCanlEugridPMANamespaces()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {

    canlNsHttpClient.execute(sectigoReq);

  }

  @Test
  public void testSectigoUrlCanlIgnoreNamespaces()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {

    canlNoNsHttpClient.execute(sectigoReq);

  }

  @Test
  public void testSectigoUrl()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {

    plainHttpClient.execute(sectigoReq);
  }

  @Test
  public void testDigicertUrl()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {
    plainHttpClient.execute(digicertReq);

  }

  @Test
  public void testDigicertUrlCanlEugridPMANamespaces()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {
    canlNsHttpClient.execute(digicertReq);
  }

  @Test
  public void testDigicertUrlCanlIgnoreNamespaces()
      throws ClientProtocolException, NoSuchAlgorithmException, IOException {
    canlNoNsHttpClient.execute(digicertReq);
  }
}
