/*
 * ListsScreen.java
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
final class ListsScreen extends MainScreen{

    public ListsScreen(List[] lists) 
    {
        // call the parents constructor
        super();
        setTitle(new LabelField("BBRTM - Lists", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));   
        
        add(new ListsListField(lists));
             
    }
}

