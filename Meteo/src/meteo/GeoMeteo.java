/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
//import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 *
 * @author pollini
 */

public class GeoMeteo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {
            GeoMeteo geo = new GeoMeteo();
            String indirizzo = geo.getUserInput();
            System.out.println(indirizzo);
            System.setProperty("proxySet", "true");
            System.setProperty("http.proxyHost", "192.168.0.1");
            System.setProperty("http.proxyPort", "8080");
            Authenticator.setDefault(new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    
                    return new PasswordAuthentication("NOMEUTENTE","PASSWORD".toCharArray());
                }
            });
            
            URL url = new URL("http://www.google.com/");
            URLConnection con = url.openConnection();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));

// Read it ...
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            
            in.close();
        } catch (MalformedURLException ex) {
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        }

    
    }

    private static class ProxyAuthenticator extends Authenticator {

        public ProxyAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }



    private String userName, password;

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password.toCharArray());
    }

    

    }
    
    
    
    
    
    public GeoMeteo() {
}
    
    public String getUserInput() {
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Inserire un indirizzo:");
        String input = keyboard.nextLine();
        return input;
    }
    
    public boolean isValid(String address) {
        if(address!=null && address.length()!=0) {
            return true;
        }
        else {
            return false;
        }
    }
    
}