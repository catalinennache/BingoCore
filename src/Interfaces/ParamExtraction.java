/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;

/**
 *
 * @author Enache
 */
public interface ParamExtraction {
    public HashMap<String,String> process (HttpExchange obex);
}
