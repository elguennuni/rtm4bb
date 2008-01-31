/*
 * ThisWeekScreen.java
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
final class ThisWeekScreen extends MainScreen{

    private RTM rtm;
    
    public ThisWeekScreen(RTM rtm) 
    {
        // call the parents constructor
        super();
        this.rtm = rtm;
        
        setTitle(new LabelField("BBRTM - This Week", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        add( new ThisWeekListField(rtm) );
        
    }
}

