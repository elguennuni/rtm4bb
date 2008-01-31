/*
 * RTM.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import java.util.Vector;
import net.rim.device.api.util.Arrays;


/**
 * 
 */
public class RTM {
    
    private RTMAPI api;
    Vector tasks;
    Vector lists;
    Vector locations;
    Vector tags;
    
    
    
    public RTM(String authToken)
    {
        // initialize the api class
        api = new RTMAPI("968d8045c952707d8d13f5187a93cb9f", "ODg5ZDhlMjkxNGJiYzkxZg==");
        // set the auth token
        api.setAuthToken(authToken);
        
        // get the tasks, lists, and location
        tasks = api.getTasks("", "status:incomplete");
        lists = api.getLists();
        locations = api.getLocations();
        tags = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            String[] tag = task.getTags();
            for(int y = 0; y < tag.length; ++y)
            {
                if(! tags.contains(tag[y]))
                {
                    tags.addElement(tag[y]);
                }
            }
        }
    }
    
    public String getFrob()
    {
        return api.getFrob();
    }
    
    public String getAuthURL(String frob)
    {
        return api.getAuthURL(frob);
    }
    
    public String getAuthToken(String frob)
    {
        return api.getAuthToken(frob);
    }
    
    public List[] getLists()
    {
        List[] l = new List[lists.size()];
        lists.copyInto(l);
        return l;
    }
    
    public String[] getTags()
    {
        if(tags == null)
        {
            return new String[0];
        }
        String[] t = new String[tags.size()];
        tags.copyInto(t);
        return t;
    }
    
    public Location[] getLocations()
    {
        if(locations == null)
        {
            return new Location[0];
        }
        Location[] l = new Location[locations.size()];
        locations.copyInto(l);
        return l;
    }
    public Task[] dueToday()
    {
        Vector today = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.dueToday() || task.overdue())
            {
                today.addElement(task);
            }
        }
        
        Task[] t = new Task[today.size()];
        today.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
    }
    
    public int countDueToday()
    {
        int count = 0;
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.dueToday() || task.overdue())
            {
                count++;
            }
        }
        return count;
    }
    
    public Task[] dueTomorrow()
    {
        Vector today = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.dueTomorrow())
            {
                today.addElement(task);
            }
        }
        
        Task[] t = new Task[today.size()];
        today.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
    }
    
    public int countDueTomorrow()
    {
        int count = 0;
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.dueTomorrow())
            {
                count++;
            }
        }
        return count;
    }
       
    public String getListName(String id)
    {
        for( int x = 0; x < lists.size(); ++x)
        {
            List list = (List)lists.elementAt(x);
            if( list.getListID().equals(id) )
            {
                return list.getName();
            }
        }     
        
        return id;
    }
} 
