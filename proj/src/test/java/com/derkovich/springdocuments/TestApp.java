package com.derkovich.springdocuments;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class TestApp {
    @Test
    public void givenUserDoesNotExists_whenUserInfoIsRetrieved_then404IsReceived()
            throws ClientProtocolException, IOException {

        // Given
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                                    .setUri("http://localhost:8080/api/documents")
                                    .setHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYwNDk1NTYwMH0._Kh_07XvJeKLu9g1nPn17GFhRNjPywj8TPXrb4Ywr1fAo59wlhiIvIp0g9fFEa0JJeDcMAGqDEQxHoLX0-Rxmw")
                                    .build();


        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void testLogin() throws IOException {
        // Given

        StringEntity requestEntity = new StringEntity(
                "{\"username\": \"admin\"," +
                        "" +
                        "\"password\": \"admin\"}",
                ContentType.APPLICATION_JSON);
        HttpClient client = HttpClients.custom().build();
        HttpPost postMethod = new HttpPost("http://localhost:8080/api/auth/login");
        postMethod.setEntity(requestEntity);


        // When
        HttpResponse httpResponse = client.execute(postMethod);

        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println(responseString);
        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void testRegister() throws IOException {
        // Given

        StringEntity requestEntity = new StringEntity(
                "{\"username\": \"sdfsfskdfdksfjk\"," +
                        "" +
                        "\"password\": \"sdfkdfjksdjfksdfj\"}",
                ContentType.APPLICATION_JSON);
        HttpClient client = HttpClients.custom().build();
        HttpPost postMethod = new HttpPost("http://localhost:8080/api/auth/register");
        postMethod.setEntity(requestEntity);


        // When
        HttpResponse httpResponse = client.execute(postMethod);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void documentDoesntExist()
            throws ClientProtocolException, IOException {

        // Given
        HttpClient client = HttpClients.custom().build();
        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://localhost:8080/api/documents/1234")
                .setHeader(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYwNDk1NTYwMH0._Kh_07XvJeKLu9g1nPn17GFhRNjPywj8TPXrb4Ywr1fAo59wlhiIvIp0g9fFEa0JJeDcMAGqDEQxHoLX0-Rxmw")
                .build();


        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void testLoginAndRating() throws IOException, JSONException {
        // Given

        StringEntity requestEntity = new StringEntity(
                "{\"username\": \"admin\"," +
                        "" +
                        "\"password\": \"admin\"}",
                ContentType.APPLICATION_JSON);
        HttpClient client = HttpClients.custom().build();
        HttpPost postMethod = new HttpPost("http://localhost:8080/api/auth/login");
        postMethod.setEntity(requestEntity);


        // When
        HttpResponse httpResponse = client.execute(postMethod);

        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        JSONObject obj = new JSONObject(responseString);
        String token = obj.getString("token");

        HttpUriRequest request = RequestBuilder.get()
                .setUri("http://localhost:8080/api/documents")
                .setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .build();


        // When
        HttpResponse httpResp = HttpClientBuilder.create().build().execute( request );

        // Then
        assertThat(
                httpResp.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }

    @Test
    public void testLoginAndComment() throws IOException, JSONException {
        // Given

        StringEntity requestEntity = new StringEntity(
                "{\"username\": \"admin\"," +
                        "" +
                        "\"password\": \"admin\"}",
                ContentType.APPLICATION_JSON);
        HttpClient client = HttpClients.custom().build();
        HttpPost postMethod = new HttpPost("http://localhost:8080/api/auth/login");
        postMethod.setEntity(requestEntity);


        // When
        HttpResponse httpResponse = client.execute(postMethod);

        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        JSONObject obj = new JSONObject(responseString);
        String token = obj.getString("token");

        StringEntity requestPost = new StringEntity(
                        "{\"comment\": \"Test comment\"}", ContentType.APPLICATION_JSON);
        HttpPost postComment = new HttpPost("http://localhost:8080/api/documents/4/comments");
        postComment.setHeader(HttpHeaders.AUTHORIZATION, "Bearer "+ token);
        postComment.setEntity(requestPost);
        HttpResponse httpCommentResponse = client.execute(postComment);

        // Then
        assertThat(
                httpCommentResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));
    }
}
