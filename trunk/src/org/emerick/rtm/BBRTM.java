/*
 * BBRTM.java
 *
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.i18n.*;
import net.rim.device.api.system.*;
import net.rim.device.api.collection.util.*;


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
        pushScreen(new TaskListScreen(new Task[2]));
    }
} 
