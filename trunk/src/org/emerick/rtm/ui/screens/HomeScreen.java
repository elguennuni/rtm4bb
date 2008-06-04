/*
 * HomeScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.ui.screens;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import org.emerick.rtm.RTM;
import org.emerick.rtm.ui.lists.HomeMenuListField;


/**
 * 
 * @author Jason Emerick
 */
public class HomeScreen extends MainScreen{

    private RTM rtm;
    
    public HomeScreen(RTM rtm ) 
    {
        // call the parents constructor
        super(DEFAULT_CLOSE | DEFAULT_MENU);
        
        this.rtm = rtm;
        
        setTitle(new LabelField("BBRTM", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        add(new HomeMenuListField(rtm));

    }
}

