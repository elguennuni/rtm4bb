/*
 * LocationsScreen.java
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
final class LocationsScreen extends MainScreen{

    public LocationsScreen(Location[] locations) 
    {
        // call the parents constructor
        super();
        setTitle(new LabelField("BBRTM - Locations", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        add(new LocationsListField(locations));
        
    }
}

