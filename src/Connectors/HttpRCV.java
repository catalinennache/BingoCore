/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Enache
 */
public class HttpRCV {
    Random genTool=new Random();
    Connector context;
    private int PORT;

    HttpRCV(Connector cxt) {
        context = cxt;
    }

    public int start() {
        boolean scsFlag = false;
        PORT=1000+genTool.nextInt(39000);
        while (!scsFlag) {
            try {
                HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
                server.createContext("/", context);
                server.setExecutor(null); // creates a default executor
                server.start();
                scsFlag = true;
                
            } catch (Exception e) { PORT=1000+genTool.nextInt(39000);

            }
        }
        System.out.println(context.getClass().getName()+" -> HttpReceiver started @"+PORT);
        return PORT;
    }
    public int getActivePort(){
        
        return PORT;
    }
    
    
    public  HashMap<String, String> getParameters(HttpExchange httpExchange) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        InputStream inputStream = httpExchange.getRequestBody();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }
        String[] keyValuePairs = byteArrayOutputStream.toString().split("&");
        for (String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split("=");
            if (keyValue.length != 2) {
                continue;
            }
            parameters.put(keyValue[0], keyValue[1]);
        }
        return parameters;
    }
}
