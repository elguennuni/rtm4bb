/*
 * HomeScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.clear;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;


/**
 * 
 * @author Jason Emerick
 */
final class ClearScreen extends MainScreen{
         //org.emerick.rtm.BBRTM
    private static final long KEY = 0x6f57cce61b297d28L;
    
    public ClearScreen() 
    {
        // call the parents constructor
        super();
        
        setTitle(new LabelField("BBRTM Clear", LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        try {
            PersistentObject record = PersistentStore.getPersistentObject(KEY);
            record.setContents(null);
            record.commit();
            add(new LabelField("Cleared"));      
        }
        catch(Exception e)
        {
            add(new LabelField(e.toString()));
        }
        
        
        
        
        add(new SeparatorField());        

    }
}

