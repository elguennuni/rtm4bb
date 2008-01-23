/*
 * StringComparator.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.util.Comparator;


/**
 * 
 * @author Jason Emerick
 */
public class StringComparator implements Comparator
{
    public StringComparator()
    {
    }
    
    public int compare(Object a, Object b)
    {
        return ((String)a).compareTo((String)b);
    }
}
