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
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class CoreConnector extends Connector implements HttpHandler {

   

    public CoreConnector() {
        Receiver = new HttpRCV(this);
        Receiver.start();
        ready=true;
        Emittor = new HttpEMT(3000);
        
    }

    @Override
    public void handle(HttpExchange obex) throws IOException {
        String response = "This is the response";
        obex.sendResponseHeaders(200, response.length());
        OutputStream os = obex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    
   
   

}
