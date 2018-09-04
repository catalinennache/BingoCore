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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Enache
 */
public class GroundConnector extends Connector {

    private Connection conn;
    private String USER = "root";
    private String PASS = "";
    private String DB_URL = "jdbc:mysql://localhost:3306/bingo";
    private Random genTool = new Random();

    public GroundConnector() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        System.out.println("Connection established.");
        Receiver = new HttpRCV(this);
        System.out.println("Starting Receiver.. ");
        Receiver.start();
        Receiver.addProcessor(this);
        ready = true;
        System.out.println("GROUND READY");

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
        try {
            int code = task.getStatus();
            HashMap<String, String> results;
            System.out.println("Processing: " + task.toString());

            switch (code) {
                case 1: {
                    HashMap<String, String> hash = new HashMap<>();
                    HashMap<String, String> givenParams = (HashMap<String, String>) task.getParams();
                    String response = this.Auth(givenParams.get("email"), givenParams.get("password"));

                    hash.put("response", String.valueOf(response));

                    task.setResults(hash);
                }
                break;
                case 2: {
                    HashMap<String, String> hash = new HashMap<>();
                    HashMap<String, String> givenParams = (HashMap<String, String>) task.getParams();
                    String isOK = this.Register(givenParams.get("nickname"),givenParams.get("email"), givenParams.get("password"), givenParams.get("passconf"));

                    hash.put("ok", String.valueOf(isOK));

                    task.setResults(hash);

                }
                break;
                
                case 3:{
                    
                    HashMap<String, String> hash = new HashMap<>();
                    HashMap<String, String> givenParams = (HashMap<String, String>) task.getParams();
                    int isOk = this.checkNickName(givenParams.get("nickname"));
                    hash.put("ok", String.valueOf(isOk));
                    task.setResults(hash);
                    
                }break;
            }

            Emittor.send(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String Auth(String email, String password) throws SQLException {

        Statement stmt = conn.createStatement();
        String sql;
        sql = "SELECT * FROM customers WHERE email= '"+email + "' AND password = '" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        boolean exists = rs.first();
        String response="";
        if(exists){
           response = rs.getString("nickname")+"<>"; 
           response +=  rs.getString("token");
        }
        return exists ? response : String.valueOf(0);
    }
    

    ;
    
    private String Register(String nickname,String email, String password, String confirmPass) {
         String user_token = this.genToken(16);
        try {
           
            Statement stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM customers WHERE email= '" + email + "' AND pass = '" + password + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.first()) {

                stmt = conn.createStatement();
                sql = "INSERT INTO customers "
                        + "VALUES ('" + email + "', '" + password + "' , '" + user_token+"'";
                stmt.executeUpdate(sql);

            }

        } catch (Exception e) {
            return String.valueOf(0);
        }
        
        return nickname+"<>"+user_token;
    }
    
    private int checkNickName(String nickName){
        
          try {
           
            Statement stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM customers WHERE nickname= '" + nickName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (!rs.first()) {

                    return 1;
             

            }

        } catch (Exception e) {
            return 0;
        }
        
         return 0; 
        
    }  
}
