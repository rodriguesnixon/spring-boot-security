package io.javabrains.springbootsecurity.client;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;


public class HttpFormClient {

    public static void main(String... args) throws Exception {
        String user = "nixon";
        String pass = "nixon";

        if (args != null && args.length == 2) {
            user = args[0];
            pass = args[1];
        }

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {

            List<NameValuePair> form = new ArrayList<>();
            form.add(new BasicNameValuePair("username", user));
            form.add(new BasicNameValuePair("password", pass));

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, Consts.UTF_8);

            HttpPost httpPost = new HttpPost("http://localhost:8080/login");
            httpPost.setEntity(entity);
            // System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                System.out.println("status =>" + status);
                if (status >= 200 && status <= 302) {
                    HttpEntity responseEntity = response.getEntity();
                    String resp = responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                    return "Authentication Successful => " + resp;

                } else if (status == 401 || status == 403) {
                    HttpEntity responseEntity = response.getEntity();
                    String error = responseEntity != null ? EntityUtils.toString(responseEntity) : null;

                    return "Authentication Failed => " + error;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

            };

            String responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("-------------RESPONSE-------------------");
            System.out.println(responseBody);
            System.out.println("----------------------------------------");

        }
    }

}
