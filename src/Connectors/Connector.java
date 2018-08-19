/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

import com.sun.net.httpserver.HttpHandler;

/**
 *
 * @author Enache
 */
abstract class Connector implements HttpHandler {

     protected boolean ready = false;
    protected HttpRCV Receiver;
    protected HttpEMT Emittor;

    
    public void setUpEmittor(int PORT){
        Emittor=new HttpEMT(PORT);
    };
    
    public boolean isReady() {
        return ready;
    }
    
     public int getReceiverPort(){
      return  Receiver.getActivePort();
    }
    
}
