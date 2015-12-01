/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteo;

/**
 *
 * @author hewlet packard
 */
public class Point {
    private final double lat;
    private final double lng;
    
    public Point(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }
    
    public double getLat() {
        return lat;
    }
    
    public double getLng() {
        return lng;
    }
}
