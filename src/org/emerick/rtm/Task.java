/*
 * Task.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;


import java.util.Vector;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;
import net.rim.device.api.util.DateTimeUtilities;

/**
 * 
 * @author Jason Emerick
 */
public class Task 
{
    private String listid;
    private String taskseriesid;
    private String taskid;
    private String created;
    private String modified;
    private String name;
    private String source;
    private String url;
    private String locationid;
    private Vector tags;
    private Vector participants;
    private Vector notes;
    private String due;
    private String hasduetime;
    private String added;
    private String completed;
    private String deleted;
    private String priority;
    private String postponed;
    private String estimate;
    
    public Task()
    {
        listid = taskseriesid = taskid = created = modified = name = source = url = locationid = "";
        tags = new Vector();
        notes = new Vector();
        participants = new Vector();
        due = hasduetime = added = completed = deleted = priority = postponed = estimate = "";
    }
    
    public Task(String listid)
    {
        this.listid = listid;
        
        taskseriesid = taskid = created = modified = name = source = url = locationid = "";
        tags = new Vector();
        notes = new Vector();
        participants = new Vector();
        due = hasduetime = added = completed = deleted = priority = postponed = estimate = "";
    }
    
    public String getListID()
    {
        return listid;
    }
    
    public String getTaskseriesID()
    {
        return taskseriesid;
    }
    
    public String getTaskID()
    {
        return taskid;
    }
    
    public String getAdded()
    {
        return added;
    }
    
    public String getCompleted()
    {
        return completed;
    }
    
    public String getCreated()
    {
        return completed;
    }
    
    public String getDeleted()
    {
        return deleted;
    }
    
    public String getDue()
    {
        return due;
    }
    
    public String getEstimate()
    {
        return estimate;
    }
    
    public String getHasDueTime()
    {
        return hasduetime;
    }
    
    public String getLocationID()
    {
        return locationid;
    }
    
    public String getModified()
    {
        return modified;
    }
    
    public String getName()
    {
        return name;
    }
    
    public Note[] getNotes()
    {
        Note[] theNotes = new Note[notes.size()];
        notes.copyInto(theNotes);
        return theNotes;
    }
    
    public User[] getParticipants()
    {
        User[] users = new User[participants.size()];
        participants.copyInto(users);
        return users;
    }
    
    public String getPostponed()
    {
        return postponed;
    }
    
    public String getPriority()
    {
        return priority;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public String[] getTags()
    {
        String[] tagArray = new String[tags.size()];
        tags.copyInto(tagArray);
        return tagArray;
    }
    
    public String getURL()
    {
        return url;
    }
    
    public void setAdded(String added)
    {
        this.added = added;
    }
    
    public void setCompleted(String completed)
    {
        this.completed = completed;
    }
    
    public void setCreated(String created)
    {
        this.created = created;
    }
    
    public void setDeleted(String deleted)
    {
        this.deleted = deleted;
    }
    
    public void setDue(String due)
    {
        this.due = due;
    }
    
    public void setEstimate(String estimate)
    {
        this.estimate = estimate;
    }
    
    public void setHasDueTime(String hasduetime)
    {
        this.hasduetime = hasduetime;
    }
    
    public void setListID(String listid)
    {
        this.listid = listid;
    }
    
    public void setLocationID(String locationid)
    {
        this.locationid = locationid;
    }
    
    public void setModified(String modified)
    {
        this.modified = modified;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void addNote(Note note)
    {
        notes.addElement(note);
    }
    
    public void addParticipant(User user)
    {
        participants.addElement(user);
    }
    
    public void setPostponed(String postponed)
    {
        this.postponed = postponed;
    }
    
    public void setPriority(String priority)
    {
        this.priority = priority;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public void addTag(String tag)
    {
        tags.addElement(tag);
    }
    
    public void setTaskID(String taskid)
    {
        this.taskid = taskid;
    }
    
    public void setTaskseriesID(String taskseriesid)
    {
        this.taskseriesid = taskseriesid;
    }
    
    public void setURL(String url)
    {
        this.url = url;
    }
        
    public Calendar getCalendar()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        cal.set(Calendar.YEAR, Integer.parseInt(due.substring(0,4)));
        cal.set(Calendar.MONTH, Integer.parseInt(due.substring(5,7))-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(due.substring(8,10)));
        int hour = Integer.parseInt(due.substring(11,13));
        int ampm = Calendar.AM;
        if( hour > 11 )
        {
            // set to PM
            hour-=12;
            ampm = Calendar.PM;
        }      
        cal.set(Calendar.AM_PM, ampm);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, Integer.parseInt(due.substring(14,16)));
        cal.set(Calendar.SECOND, Integer.parseInt(due.substring(17,19)));
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal;
    }
    
    public boolean dueToday()
    {
        Calendar cal = getCalendar();
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        System.out.println(today.getTime().toString());
        
        return DateTimeUtilities.isSameDate(cal.getTime().getTime(), today.getTime().getTime(), TimeZone.getTimeZone("UTC"), null);
    }
    
    public boolean dueTomorrow()
    {
        Calendar cal = getCalendar();
        Calendar tomorrow = DateTimeUtilities.getNextDate(5 * DateTimeUtilities.ONEHOUR);
        
        System.out.println(tomorrow.getTime().toString());
        
        return DateTimeUtilities.isSameDate(cal.getTime().getTime(), tomorrow.getTime().getTime(), TimeZone.getTimeZone("UTC"), null);
    }
        
        
        
} 
