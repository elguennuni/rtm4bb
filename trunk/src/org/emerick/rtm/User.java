/*
 * User.java
 *
 */

package org.emerick.rtm;




/**
 * 
 * @author Jason Emerick
 */
public class User {
    private String username;
    private String fullname;
    private String userid;
    
    public User()
    {
        username = fullname = userid = "";
    }
    
    public User(String username, String fullname, String userid)
    {
        this.username = username;
        this.fullname = fullname;
        this.userid = userid;
    }
    
    public String getUserName()
    {
        return username;
    }
    
    public String getUserID()
    {
        return userid;
    }
    
    public String getFullName()
    {
        return fullname;
    }
} 
