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
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;

public class CoreConnector extends Connector {

    public CoreConnector() {
        Receiver = new HttpRCV(this);
        Receiver.start();
        Receiver.addProcessor(this);
        ready = true;

    }

    @Override
    public void handle(HttpExchange obex) throws IOException {

    }

    @Override
    public void process(Task task) {

        int code = task.getID();
        HashMap<String, String> results;
        System.out.println("Processing: "+task.toString());

        switch (code) {
            case 1: {
                
            }
            break;
        }

        Emittor.send(task);
    }

}
