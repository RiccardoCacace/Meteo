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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
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
            //System.out.println(indirizzo);
            Point p = geo.getPoint(indirizzo);
            if (p!=null) {
                System.out.println("Latitudine: " + p.getLat());
                System.out.println("Longitudine: " + p.getLng());
                WeatherForecast w = geo.getMeteo(p.getLat(), p.getLng());
                if (w!=null) {
                    System.out.println(w.toString());
                }
                else {
                    System.out.println("Impossibile reperire meteo.");
                }
            }
            else {
                System.out.println("Impossibile trovare l'indirizzo.");
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
            address=address.replaceAll("\\s+",""); //rimuove i white space
            URL myUrl = new URL("http://maps.googleapis.com/maps/api/geocode/xml?address=" + address + "&sensor=false"); //crea URL per coordinate
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //utilizzo XPath per estrarre la latitudine e la longitudine
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(myUrl.openStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            javax.xml.xpath.XPathExpression expr = xpath.compile("/GeocodeResponse/result/geometry/location/lat");
            String swLat = expr.evaluate(doc, XPathConstants.STRING).toString();
            javax.xml.xpath.XPathExpression expr2 = xpath.compile("/GeocodeResponse/result/geometry/location/lng");
            String swLng = expr2.evaluate(doc, XPathConstants.STRING).toString();
            double lat = Double.parseDouble(swLat); //trasformo stringhe in double
            double lng = Double.parseDouble(swLng);
            Point p = new Point(lat, lng); //creo oggetto di tipo Point
            return p;
        } catch (ParserConfigurationException | IOException | SAXException | XPathExpressionException ex) {
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
}
    
    public WeatherForecast getMeteo (double lat, double lng) {
        try {
            URL myUrl = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + Double.toString(lat) + "&lon=" + Double.toString(lng) + "&mode=xml&appid=2de143494c0b295cca9337e1e96b00e0"); //crea URL per meteo
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //utilizzo XPath per estrarre il meteo
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(myUrl.openStream());
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            javax.xml.xpath.XPathExpression temperatureXPath = xpath.compile("/current/temperature/@value");
            javax.xml.xpath.XPathExpression humidityXPath = xpath.compile("/current/humidity/@value");
            javax.xml.xpath.XPathExpression pressureXPath = xpath.compile("/current/pressure/@value");
            javax.xml.xpath.XPathExpression speedWindXPath = xpath.compile("/current/wind/speed/@value");
            javax.xml.xpath.XPathExpression cloudsXPath = xpath.compile("/current/clouds/@value");
            javax.xml.xpath.XPathExpression precipitationXPath = xpath.compile("/current/precipitation/@mode");
            javax.xml.xpath.XPathExpression weatherXPath = xpath.compile("/current/weather/@value");
            String temperature = temperatureXPath.evaluate(doc, XPathConstants.STRING).toString();
            String humidity = humidityXPath.evaluate(doc, XPathConstants.STRING).toString();
            String pressure = pressureXPath.evaluate(doc, XPathConstants.STRING).toString();
            String speedWind = speedWindXPath.evaluate(doc, XPathConstants.STRING).toString();
            String clouds = cloudsXPath.evaluate(doc, XPathConstants.STRING).toString();
            String precipitation = precipitationXPath.evaluate(doc, XPathConstants.STRING).toString();
            String weather = weatherXPath.evaluate(doc, XPathConstants.STRING).toString();
            WeatherForecast w = new WeatherForecast(temperature, humidity, pressure, speedWind, clouds, precipitation, weather); //creo oggetto di tipo WeatherForecast
            return w;
        } catch (MalformedURLException ex) {
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException ex) {
            Logger.getLogger(GeoMeteo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
 
    
    //"http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=2de143494c0b295cca9337e1e96b00e0"
    //"http://api.openweathermap.org/data/2.5/weather?q=London&mode=xml&appid=2de143494c0b295cca9337e1e96b00e0"
}