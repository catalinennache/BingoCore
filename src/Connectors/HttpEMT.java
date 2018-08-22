/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

/**
 *
 * @author Enache
 */
import Decoration.Task;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

public class HttpEMT implements HttpHandler {

    private final String USER_AGENT = "Mozilla/5.0";
    private String PORT;

    public HttpEMT(int PORT) {

        this.PORT = String.valueOf(PORT);
    }

    public void send(Task task) {
      try{  
        String url = "http://localhost:" + PORT;
        String adaptedID = String.valueOf(task.getID());
        HashMap<String, String> results = (HashMap<String, String>) task.getResults();

        ArrayList<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("task_id", adaptedID));

        results.forEach((key, data) -> {
            urlParameters.add(new BasicNameValuePair(key, data));
        });

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        post.setHeader("User-Agent", USER_AGENT);
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("Task: "+task.getID()+" test");
        
        int status_code = response.getStatusLine().getStatusCode();

        if (status_code == 200) {
            System.out.println("Task finalized@"+task.getID());
            task.Finalize();
        }

        /*  BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result);*/
      }catch(Exception e){
          e.printStackTrace();
      }
    }

    @Override
    public void handle(HttpExchange obex) throws IOException {
        String response = "ACK";
        obex.sendResponseHeaders(200, response.length());
        OutputStream os = obex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
