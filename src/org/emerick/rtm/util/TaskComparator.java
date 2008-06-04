/*
 * TaskComparator.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.util;

import net.rim.device.api.util.Comparator;

import org.emerick.rtm.beans.Task;


/**
 * 
 * @author Jason Emerick
 */
public class TaskComparator implements Comparator
{
    public TaskComparator()
    {
    }
    
    public int compare(Object a, Object b)
    {
        int comparePriority = ((Task)a).getPriority().compareTo(((Task)b).getPriority());
        
        int compareDue = ((Task)a).getDue().compareTo(((Task)b).getDue());
        
        if(((Task)a).getDue().length() == 0 && ((Task)b).getDue().length() > 0)
        {
            compareDue = 1;
        }
        else if(((Task)b).getDue().length() == 0 && ((Task)a).getDue().length() > 0)
        {
            compareDue = -1;
        }

        if( comparePriority == 0 )
            return compareDue;
        else
            return comparePriority;
    }
}
