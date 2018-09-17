/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Connectors.CoreConnector;
import Connectors.GroundConnector;
import Connectors.HttpEMT;
import Interfaces.ParamExtraction;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author Enache
 */
public class BingoCore {

    /**
     * @param args the command line arguments
     */
    private static CoreConnector core;
    private static GroundConnector ground;
    private static HttpEMT Provisional_EMT;
    private static boolean Firewall_ON = false;

    public static void main(String[] args) throws Exception {

        

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(35300), 0);
            server.createContext("/", resolver);
            server.setExecutor(null); // creates a default executor
            server.start();

        } catch (Exception e) {
        }

        core = new CoreConnector();
        ground = new GroundConnector();
    }

    private static final HttpHandler resolver = new HttpHandler() {
        @Override
        public void handle(HttpExchange obex) throws IOException {

            if (obex.getRemoteAddress().getHostName().equals("127.0.0.1") || !BingoCore.Firewall_ON) {
                System.out.println(obex);
                System.out.println("check");
                if (core.isReady() && ground.isReady()) {
                    System.out.println("check");
                    HashMap<String, String> maskInfo = extTool.process(obex);
                    int corePort = Integer.valueOf(maskInfo.get("1"));
                    int groundPort = Integer.valueOf(maskInfo.get("2"));
                    core.setUpEmittor(corePort);
                    ground.setUpEmittor(groundPort);
                    JSONObject response_unprocessed = new JSONObject();

                    response_unprocessed.put("1", core.getReceiverPort());
                    response_unprocessed.put("2", ground.getReceiverPort());

                    String processed_response = response_unprocessed.toJSONString();
                    obex.sendResponseHeaders(200, processed_response.length());
                    OutputStream os = obex.getResponseBody();
                    os.write(processed_response.getBytes());
                    os.close();
                    System.out.println("HANDSHAEKe DONE");

                } else {
                    String response = "";
                    obex.sendResponseHeaders(503, response.length());
                    OutputStream os = obex.getResponseBody();
                    os.write(response.getBytes());
                    os.close();

                }
            } else {
                obex.sendResponseHeaders(403, 0);
                OutputStream os = obex.getResponseBody();
                os.close();
            }

        }
    };

    private static final ParamExtraction extTool = (HttpExchange obex) -> {

        HashMap<String, String> parameters = new HashMap<>();
        try {
            InputStream inputStream = obex.getRequestBody();
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
        } catch (Exception e) {
        }
        return parameters;

    };

    public static void firewall_setup(String param) throws Exception {
        if (true) {
            throw new Exception("Unsupported yet !!");
        } else {

            {
                String firewall = param;
                if (firewall == null) {
                    System.out.println("Firewall parameter undetected.\n***** Feature releases will terminate the process with a code 0 ****\nDefault is off.\n");
                } else if (firewall.toLowerCase().trim().equals("enabled")) {
                    Firewall_ON = true;
                    System.out.println("Firewall Enabled!\n **** Note: without an Internet connection  ");
                } else if (firewall.toLowerCase().trim().equals("disabled")) {
                    System.out.println("Firewall is currently disabled");
                } else {
                }
            }
        }
    }

    private static void checkConnection() throws Exception {
        throw new Exception("Unsupported yet!!");
    }

}
