/*
 * Location.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.beans;




/**
 * 
 * @author Jason Emerick
 */
public class Location {
    
    private String id;
    private String name;
    private String longitude;
    private String latitude;
    private String zoom;
    private String address;
    private String viewable;    
    
    public Location()
    {
        id = name = longitude = latitude = "";
        zoom = address = viewable = "";
    }
    
    public Location(String id, String name, String longitude, String latitude, String zoom, String address, String viewable)
    {
        this.id = id;
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.zoom = zoom;
        this.address = address;
        this.viewable = viewable;
    }
    
    public String getID()
    {
        return id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public String getLongitude()
    {
        return longitude;
    }
    
    public String getLatitude()
    {
        return latitude;
    }
    
    public String getZoom()
    {
        return zoom;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getViewable()
    {
        return viewable;
    }
    
    
} 
