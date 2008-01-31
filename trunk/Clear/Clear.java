/*
 * Clear.java
 *
 * © <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package org.emerick.clear;

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


/**
 * 
 */
public class Clear  extends UiApplication{

    
    public static void main(String[] args)
    {
       Clear clr = new Clear();
       
       clr.enterEventDispatcher();
    }
    
    public Clear() 
    {
        pushScreen(new ClearScreen());
    }
} 
