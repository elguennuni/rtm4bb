/*
 * BBRTM.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;
import net.rim.device.api.io.*;
import java.io.IOException;
import java.lang.*;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.util.Persistable;

import net.rim.blackberry.api.browser.*;



/**
 * 
 */
final public class BBRTM extends UiApplication{
    
    //org.emerick.rtm.BBRTM
    private static final long KEY = 0x6f57cce61b297d28L;
    
    private RTM rtm;
    
    public static void main(String[] args)
    {
       BBRTM bbrtm = new BBRTM();
       
       bbrtm.enterEventDispatcher();
    }
    
    public BBRTM()
    {
        String token = "";
        
        try {
            PersistentObject record = PersistentStore.getPersistentObject(KEY);
            AuthenticationData data = (AuthenticationData) record.getContents();
            if( data == null)
            {
                data = new AuthenticationData();
                data.authToken = data.frob = "";
            }
            
            if(data.frob.length() == 0)
            {
                RTMAPI api = new RTMAPI("968d8045c952707d8d13f5187a93cb9f", "ODg5ZDhlMjkxNGJiYzkxZg==");
                data.frob = api.getFrob();
                data.authToken = "";
                record.setContents(data);
                record.commit();
                BrowserSession bs = Browser.getDefaultSession();
                bs.displayPage(api.getAuthURL(data.frob));
                System.exit(0);
            }
            else if ( data.authToken.length() == 0 )
            {
                RTMAPI api = new RTMAPI("968d8045c952707d8d13f5187a93cb9f", "ODg5ZDhlMjkxNGJiYzkxZg==");
                data.authToken = api.getAuthToken(data.frob);
                //rtm = new RTM(data.authToken);
                token = data.authToken;
                record.setContents(data);
                record.commit();
            }
            else
            {
                // set the auth token
                //rtm = new RTM(data.authToken);
                token = data.authToken;
            }
        }
        catch(Exception e)
        {
            System.out.println(e.toString());
        }
                   
           
           rtm = new RTM(token);
           
        
        pushScreen(new HomeScreen(rtm));
        
        //BrowserSession bs = Browser.getDefaultSession();
        //bs.displayPage("http://emerick.org");
        
        
        
    }
} 
