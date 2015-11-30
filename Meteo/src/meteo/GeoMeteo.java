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
import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.xpath.XPathExpression;
import org.xml.sax.SAXException;

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
            Point p = geo.getPoint(indirizzo);
            if (p!=null) {
                System.out.println(p.getLat());
                System.out.println(p.getLng());
            }
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
    
    public Point getPoint(String address) {
        
        try {
            address=address.replaceAll("\\s+","");
            URL myUrl = new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=" + address + "&sensor=false");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(myUrl.openStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            javax.xml.xpath.XPathExpression expr = xpath.compile("/GeocodeResponse/result/geometry/location/lat");
            String swLat = expr.evaluate(doc, XPathConstants.STRING).toString();
            System.out.println("swLat: " + swLat );
            javax.xml.xpath.XPathExpression expr2 = xpath.compile("/GeocodeResponse/result/geometry/location/lng");
            String swLng = expr2.evaluate(doc, XPathConstants.STRING).toString();
            System.out.println("swLng: " + swLng );
            Double lat = Double.parseDouble(swLat);
            Double lng = Double.parseDouble(swLng);
            Point p = new Point(lat, lng);
            return p;
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            ex.printStackTrace();
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            ex.printStackTrace();
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        return null;
}
    
    
    
 
    
    
}