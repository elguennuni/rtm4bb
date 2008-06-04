/*
 * ListsScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.ui.screens;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import org.emerick.rtm.RTM;
import org.emerick.rtm.ui.lists.ListsListField;


/**
 * 
 * @author Jason Emerick
 */
public final class ListsScreen extends MainScreen{
    
    public ListsScreen(RTM rtm) 
    {
        // call the parents constructor
        super();
        setTitle(new LabelField("BBRTM - Lists", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));   
        
        add(new ListsListField(rtm));
             
    }
}

