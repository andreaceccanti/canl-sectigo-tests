package it.infn.test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

public class CanlUrlTester {

  final String url;

  public CanlUrlTester(String url) {
    this.url = url;
  }

  public void openUrl() throws NoSuchAlgorithmException, ClientProtocolException, IOException {

    HttpClient client = new CanlHttpClientFactory().buildHttpClient();

    HttpGet get = new HttpGet(url);
    HttpResponse response = client.execute(get);

  }

  public static void main(String[] args)
      throws NoSuchAlgorithmException, ClientProtocolException, IOException {
    new CanlUrlTester(args[0]).openUrl();
  }
}
