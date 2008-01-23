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
import java.lang.String;

import net.rim.blackberry.api.browser.*;



/**
 * 
 */
final public class BBRTM extends UiApplication{
    
    public static void main(String[] args)
    {
       BBRTM bbrtm = new BBRTM();
       
       bbrtm.enterEventDispatcher();
    }
    
    public BBRTM()
    {
        
        
        //BrowserSession bs = Browser.getDefaultSession();
        //bs.displayPage("http://emerick.org");
             
        
        pushScreen(new HomeScreen());
        
        
        
    }
} 
