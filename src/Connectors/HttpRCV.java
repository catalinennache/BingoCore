/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

import Decoration.Task;
import Interfaces.KernelProcessor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Enache
 */
public class HttpRCV implements HttpHandler {
    Random genTool=new Random();
    Connector context;
    List<KernelProcessor> listeners=new ArrayList<>();
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
                server.createContext("/", this);
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

    @Override
    public void handle(HttpExchange obex) throws IOException {
       //if(obex.getLocalAddress().getHostName().equals("127.0.0.1")){;
        HashMap<String,String> params= this.getParameters(obex);
       
        obex.sendResponseHeaders(200, 0);
        OutputStream os = obex.getResponseBody();
      
        os.close();
        System.out.println(params);
        int code=Integer.valueOf(params.get("code"));
        System.out.println(code);
        params.remove("code");
        int id=Integer.valueOf(params.get("id"));
        params.remove("id");
        System.out.println(id);
        System.out.println(params);
        Task task;
       try{  task= Task.createTask(code,id,params); 
        System.out.println("TASK CREATED");
        listeners.forEach((listener)->{
                System.out.println("PRocessing task");
                listener.process(task);
        });
        } catch(Exception e) {e.printStackTrace(); }
     //  }else{
     //   obex.sendResponseHeaders(403, 0);
     //   OutputStream os = obex.getResponseBody();
      //  os.close();
     //  }
    
    }
    
    public void addProcessor(KernelProcessor prc){
        listeners.add(prc);
    }
}
