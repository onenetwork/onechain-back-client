
package com.onenetwork.backchain.client.hyp;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.onenetwork.backchain.client.ContentBackchainClient;

/**
 * Implementation of {@link ContentBackchainClient} for an
 * <a href="https://https://www.hyperledger.org/projects/fabric/">Hyperledger
 * fabric</a>-based Backchain.
 */
public class HyperledgerContentBackchainClient implements ContentBackchainClient {

  private HttpClient client;
  private Header authHeader;
  private String baseUrl;

  public HyperledgerContentBackchainClient(HyperledgerConfig config) {
    client = HttpClientBuilder.create().build();
    authHeader = new BasicHeader("Authorization", "Bearer " + config.getPrivateKey());
    baseUrl = config.getUrl();
  }

  /**
   * Build HttpGet request with URL and authorization header
   * 
   * @param url
   * @return
   * @throws UnsupportedEncodingException
   * @throws URISyntaxException
   */
  private HttpGet buildHttpGetRequest(String fcn, String args) throws UnsupportedEncodingException, URISyntaxException {
    URIBuilder builder = new URIBuilder(baseUrl + "/query");
    builder.setParameter("fcn", fcn).setParameter("args", args);

    HttpGet httpGetReq = new HttpGet(builder.build());
    httpGetReq.addHeader(authHeader);

    return httpGetReq;
  }

  /**
   * Build HttpPost request with URL and authorization header
   * 
   * @param url
   * @param param
   * @return
   */
  private HttpPost buildHttpPostRequest(String args)  {
    HttpPost httpPostReq = new HttpPost(baseUrl + "/invoke");
    httpPostReq.addHeader(authHeader);

    List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
    urlParameters.add(new BasicNameValuePair("fcn", "post"));
    urlParameters.add(new BasicNameValuePair("args", args));
    httpPostReq.setEntity(new UrlEncodedFormEntity(urlParameters, Charset.forName("UTF-8")));

    return httpPostReq;
  }

  @Override
  public long hashCount() {
    try {
      HttpResponse response = client.execute(buildHttpGetRequest("hashCount", " "));
      if (response.getStatusLine().getStatusCode() == 200) {
        HttpEntity entity = response.getEntity();
        return Long.parseLong(EntityUtils.toString(entity));
      }

      throw new RuntimeException(response.getStatusLine().toString());
    }
    catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void post(String hash) {
    try {
      HttpResponse response = client.execute(buildHttpPostRequest(hash));
      if (response.getStatusLine().getStatusCode() != 200) {
        throw new RuntimeException(response.getStatusLine().toString());
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public boolean verify(String hash) {
    try {
      HttpResponse response = client.execute(buildHttpGetRequest("verify", hash));
      if (response.getStatusLine().getStatusCode() == 200) {
        HttpEntity entity = response.getEntity();
        return Boolean.parseBoolean(EntityUtils.toString(entity));
      }

      throw new RuntimeException(response.getStatusLine().toString());
    }
    catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public String getHash(long index) {
    throw new RuntimeException("Not supported for Hyperledger fabric implementatoion.");
  }

  @Override
  public String getOrchestrator() {
    throw new RuntimeException("Not supported for Hyperledger fabric implementatoion.");
  }
}
