/*
 * ThisWeekScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.ui.screens;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import org.emerick.rtm.RTM;
import org.emerick.rtm.ui.lists.ThisWeekListField;


/**
 * 
 * @author Jason Emerick
 */
public class ThisWeekScreen extends MainScreen{

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

