/*
 * Settings.java
 *
 * © <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package org.emerick.rtm;

import java.util.TimeZone;


/**
 * 
 */
public class Settings {
    
    private int offset;
    private String dateFormat;
    private String timeFormat;
    private String defaultList;
    private String language;
    
    
    public Settings()
    {
        offset = 0;
        dateFormat = timeFormat = defaultList = language = "";
    }
    
    public Settings(String timezone, String dateFormat, String timeFormat, String defaultList, String language)
    {
        setOffset(timezone);
        this.dateFormat = dateFormat;
        this.timeFormat = timeFormat;
        this.defaultList = defaultList;
        this.language = language;
    }
        
    
    public void setOffset(String timezone)
    {
        offset = TimeZone.getTimeZone(timezone).getRawOffset() / 3600000;
    }
    
    public int getOffset()
    {
        return offset;
    }
    
    public void setDateFormat(String dateFormat)
    {
        this.dateFormat = dateFormat;
    }
    
    public String getDateFormat()
    {
        return this.dateFormat;
    }
    
    public void setTimeFormat(String timeFormat)
    {
        this.timeFormat = timeFormat;
    }
    
    public String getTimeFormat()
    {
        return this.timeFormat;
    }
    
    public void setDefaultList(String list)
    {
        this.defaultList = list;
    }
    
    public String getDefaultList()
    {
        return this.defaultList;
    }
    
    public void setLanguage(String lang)
    {
        this.language = lang;
    }
    
    public String getLanguage()
    {
        return this.language;
    }
    
} 
