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
public class WeatherForecast {
    private final String temperature;
    private final String humidity;
    private final String pressure;
    private final String speedWind;
    private final String clouds;
    private final String precipitation;
    private final String weather;
    
    /**
     *
     * @param temperature
     * @param humidity
     * @param pressure
     * @param speedWind
     * @param clouds
     * @param precipitation
     * @param weather
     */
    public WeatherForecast(String temperature, String humidity, String pressure, String speedWind, String clouds, String precipitation, String weather) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.speedWind = speedWind;
        this.clouds = clouds;
        this.precipitation = precipitation;
        this.weather = weather;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String toString() {
         String a = "Temperatura: " + temperature + "\n" +
                   "Umidità: " + humidity + "\n" +
                   "Pressione: " + pressure + "\n" +
                   "Velocità del vento: " + speedWind + "\n" +
                   "Nuvole: " + clouds + "\n" +
                   "Precipitazioni: " + precipitation + "\n" +
                   "Tempo: " + weather;
         return a;
    }
}
