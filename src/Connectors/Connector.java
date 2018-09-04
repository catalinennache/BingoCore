/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connectors;

import Interfaces.KernelProcessor;
import com.sun.net.httpserver.HttpHandler;
import java.util.Random;

/**
 *
 * @author Enache
 */
abstract class Connector implements HttpHandler,KernelProcessor{

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
    
protected String genToken(int len) {
    int leftLimit = 97; // letter 'a'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = len;
    Random random = new Random();
    StringBuilder buffer = new StringBuilder(targetStringLength);
    for (int i = 0; i < targetStringLength; i++) {
        int randomLimitedInt = leftLimit + (int) 
          (random.nextFloat() * (rightLimit - leftLimit + 1));
        buffer.append((char) randomLimitedInt);
    }
    String generatedString = buffer.toString();
    return generatedString;
}
}
