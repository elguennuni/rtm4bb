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
        
        Settings settings = api.getSettings();
        
        // get the tasks, lists, and location
        tasks = api.getTasks("", "status:incomplete", settings.getOffset());
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
    
    public Task[] getAllTasks()
    {
        Task[] t = new Task[tasks.size()];
        tasks.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
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
    
    public Task[] getTasksByList(String listid)
    {
        Vector listTasks = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.getListID().equals(listid))
            {
                listTasks.addElement(task);
            }
        }
        
        Task[] t = new Task[listTasks.size()];
        listTasks.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
    }
    
    public Task[] getTasksByLocation(String locationid)
    {
        Vector locationTasks = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.getLocationID().equals(locationid))
            {
                locationTasks.addElement(task);
            }
        }
        
        Task[] t = new Task[locationTasks.size()];
        locationTasks.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
    }
    
    public int countByLocation(String locationid)
    {
        int count = 0;
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.getLocationID().equals(locationid))
            {
                count++;
            }
        }
        
        return count;
    }
    
    public int countByList(String listid)
    {
        int count = 0;
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.getListID().equals(listid))
            {
                count++;
            }
        }
        
        return count;
    }
    
    public Task[] getTasksByTag(String tag)
    {
        Vector tagged = new Vector();
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            String[] tags = task.getTags();
            for( int y = 0; y < tags.length; ++y)
            {
                if(tags[y].equals(tag))
                {
                    tagged.addElement(task);
                }
            }
        }
        
        Task[] t = new Task[tagged.size()];
        tagged.copyInto(t);
        Arrays.sort(t, new TaskComparator());
        return t;
    }
    
    public int countByTag(String tag)
    {
        int count = 0;
        
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            String[] tags = task.getTags();
            for( int y = 0; y < tags.length; ++y)
            {
                if(tags[y].equals(tag))
                {
                    count++;
                }
            }
        }
        
        return count;
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
    
    public int countDueThisWeek()
    {
        int count = 0;
        for( int x = 0; x < tasks.size(); ++x)
        {
            Task task = (Task)tasks.elementAt(x);
            if(task.dueThisWeek())
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
