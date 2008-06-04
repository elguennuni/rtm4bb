/*
 * ErrorScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;


/**
 * 
 * @author Jason Emerick
 */
final class ErrorScreen extends MainScreen{
    
    public ErrorScreen(String from, Exception e, String data ) 
    {
        // call the parents constructor
        super(DEFAULT_CLOSE | DEFAULT_MENU);
        
        setTitle(new LabelField("Error - " + from, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        add(new LabelField(e.toString(), LabelField.USE_ALL_WIDTH | Field.FOCUSABLE));
        add(new SeparatorField());
        add(new LabelField(data, LabelField.USE_ALL_WIDTH | Field.FOCUSABLE));
    }
}

