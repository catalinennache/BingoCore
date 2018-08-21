/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

import Decoration.Task;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Enache
 */
public class GroundConnector extends Connector{

    public GroundConnector(){
        Receiver = new HttpRCV(this);
        Receiver.start();
        Receiver.addProcessor(this);
        ready=true;
      
    }
    
    @Override
    public void handle(HttpExchange obex) throws IOException {
           String response = "This is the response";
        obex.sendResponseHeaders(200, response.length());
        OutputStream os = obex.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    @Override
    public void process(Task task) {
       
    
    }
    
}
