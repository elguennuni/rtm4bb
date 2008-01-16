/*
 * StringComparator.java
 *
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
