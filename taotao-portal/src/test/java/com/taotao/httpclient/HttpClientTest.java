package com.taotao.httpclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

public class HttpClientTest {
	@Test
	public void doGet() throws Exception {
		// Create a HttpClient object, think of it as a browser
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// Create a GET object and pass a url to it
		HttpGet get = new HttpGet("http://www.google.com");
		// make a request
		CloseableHttpResponse response = httpClient.execute(get);
		// get response as result
		System.out.println(response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
		// close HttpClient
		response.close();
		httpClient.close();
	}

	@Test
	public void doGetWithParams() throws Exception {
		// Create a HttpClient object, think of it as a browser
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// Create a GET object and pass a url to it
		URIBuilder uriBuilder = new URIBuilder("http://www.google.com/search");
		uriBuilder.addParameter("query", "Bingyang Wei");
		HttpGet get = new HttpGet(uriBuilder.build());

		// make a request
		CloseableHttpResponse response = httpClient.execute(get);
		// get response as result
		System.out.println(response.getStatusLine().getStatusCode());
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
		// close HttpClient
		response.close();
		httpClient.close();
	}
	@Test
	public void doPost() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
	
		//Create a Post object
		HttpPost post = new HttpPost("http://localhost:8082/httpclient/post.action");
		//Execute
		CloseableHttpResponse response = httpClient.execute(post);
		String string = EntityUtils.toString(response.getEntity());
		System.out.println("Hello! " + string);
		response.close();
		httpClient.close();
	}
	
	@Test
	public void doPostWithParam() throws Exception{
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//Create a post object, make sure post.action, not a html requst
		HttpPost post = new HttpPost("http://localhost:8082/httpclient/post.action");
		//Simulate a form post
		List<NameValuePair> kvList = new ArrayList<>();
		kvList.add(new BasicNameValuePair("username", "zhangsan"));
		kvList.add(new BasicNameValuePair("password", "123"));
		
		//package into an entity object
		StringEntity entity = new UrlEncodedFormEntity(kvList, "utf-8");
		post.setEntity(entity);
		
		//Execute post request
		CloseableHttpResponse response = httpClient.execute(post);
		String string = EntityUtils.toString(response.getEntity());
		System.out.println(string);
		response.close();
		httpClient.close();
	}


}
