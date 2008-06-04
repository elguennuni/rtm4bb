/*
 * Task.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.beans;


import java.util.Calendar;
import java.util.Vector;

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
    private Calendar cal;
    
    public Task()
    {
        listid = taskseriesid = taskid = created = modified = name = source = url = locationid = "";
        tags = new Vector();
        notes = new Vector();
        participants = new Vector();
        due = hasduetime = added = completed = deleted = priority = postponed = estimate = "";
    }
    
    public Task(Task task)
    {
        listid = task.listid;
        taskseriesid = task.taskseriesid;
        taskid = task.taskid;
        created = task.created;
        modified = task.modified;
        name = task.name;
        source = task.source;
        url = task.url;
        locationid = task.locationid;
        tags = task.tags;
        notes = task.notes;
        participants = task.participants;
        due = task.due;
        hasduetime = task.hasduetime;
        added = task.added;
        completed = task.completed;
        deleted = task.deleted;
        priority = task.priority;
        postponed = task.postponed;
        estimate = task.estimate;
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
    
    public void setDue(String due, int offset)
    {
        this.due = due;
        setCalendar();
        if(cal != null)
        {
            cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + offset);
        }  
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
        return cal;
    }
    
    private Calendar setCalendar()
    {
        if( due.length() == 0 )
        {
            return null;
        }
            
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(due.substring(0,4)));
        cal.set(Calendar.MONTH, Integer.parseInt(due.substring(5,7))-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(due.substring(8,10)));        
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(due.substring(11,13)));
        cal.set(Calendar.MINUTE, Integer.parseInt(due.substring(14,16)));
        cal.set(Calendar.SECOND, Integer.parseInt(due.substring(17,19)));
        cal.set(Calendar.MILLISECOND, 0);  
        
        return cal;
    }
    
    
    public boolean overdue()
    {        
        if( cal == null )
            return false;
        Calendar today = Calendar.getInstance();
        Calendar notime = Calendar.getInstance();
        notime.set(Calendar.HOUR_OF_DAY, 0);
        notime.set(Calendar.MINUTE, 0);
        notime.set(Calendar.SECOND, 0);
        notime.set(Calendar.MILLISECOND, 0);
        
        if( hasDueTime() )
        {
            return today.after(cal);
        }
        else
        {
            return notime.after(cal);
        }
    }
    
    public boolean dueToday()
    {
        if( cal == null )
            return false;
            
        Calendar today = Calendar.getInstance();
        
        return (today.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH) && today.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && today.get(Calendar.YEAR) == cal.get(Calendar.YEAR));
    }
    
    public boolean dueTomorrow()
    {
        if( cal == null )
            return false;
            
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.set(Calendar.DAY_OF_MONTH, tomorrow.get(Calendar.DAY_OF_MONTH) + 1);        
        
        return ((tomorrow.get(Calendar.DAY_OF_MONTH)) == cal.get(Calendar.DAY_OF_MONTH) && tomorrow.get(Calendar.MONTH) == cal.get(Calendar.MONTH) && tomorrow.get(Calendar.YEAR) == cal.get(Calendar.YEAR));
    }
    
    public boolean dueThisWeek()
    {
        if( cal == null )
            return false;
        Calendar oneWeek = Calendar.getInstance();
        //Calendar today = Calendar.getInstance();
        oneWeek.set(Calendar.HOUR_OF_DAY, 0);
        oneWeek.set(Calendar.MINUTE, 0);
        oneWeek.set(Calendar.SECOND, 0);
        oneWeek.set(Calendar.MILLISECOND, 0);
        oneWeek.set(Calendar.DAY_OF_MONTH, oneWeek.get(Calendar.DAY_OF_MONTH) + 7);
        //today.set(Calendar.HOUR_OF_DAY, 0);
        //today.set(Calendar.MINUTE, 0);
        //today.set(Calendar.SECOND, 0);
        //today.set(Calendar.MILLISECOND, 0);
        
        return (oneWeek.after(cal)); //&& today.before(cal));
    }
        
    
    public boolean hasDueTime()
    {
        return hasduetime.equals("1");
    }
    
    public String getFormattedDue()
    {
        if(cal == null)
            return "";
            
        boolean today = dueToday();
        boolean time = hasDueTime();
        String format = "";
        
        if(today)
        {
            if(! time)
            {
                format += "Today";
            }
        }
        else if(dueThisWeek() && !overdue())
        {
            int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
            if( dayofweek == Calendar.MONDAY )
            {
                format += "Monday";
            }
            else if( dayofweek == Calendar.TUESDAY )
            {
                format += "Tuesday";
            }
            else if( dayofweek == Calendar.WEDNESDAY )
            {
                format += "Wednesday";
            }
            else if( dayofweek == Calendar.THURSDAY )
            {
                format += "Thursday";
            }
            else if( dayofweek == Calendar.FRIDAY )
            {
                format += "Friday";
            }
            else if( dayofweek == Calendar.SATURDAY )
            {
                format += "Saturday";
            }
            else // if( dayofweek == Calendar.SUNDAY )
            {
                format += "Sunday";
            }
        }
        else
        {
            int month = cal.get(Calendar.MONTH);
            if( month == Calendar.JANUARY )
            {
                format += "Jan ";
            }
            else if( month == Calendar.FEBRUARY )
            {
                format += "Feb ";
            }
            else if( month == Calendar.MARCH )
            {
                format += "Mar ";
            }
            else if( month == Calendar.APRIL )
            {
                format += "Apr ";
            }
            else if( month == Calendar.MAY )
            {
                format += "May ";
            }
            else if( month == Calendar.JUNE )
            {
                format += "Jun ";
            }
            else if( month == Calendar.JULY )
            {
                format += "Jul ";
            }
            else if( month == Calendar.AUGUST )
            {
                format += "Aug ";
            }
            else if( month == Calendar.SEPTEMBER )
            {
                format += "Sep ";
            }
            else if( month == Calendar.OCTOBER )
            {
                format += "Oct ";
            }
            else if( month == Calendar.NOVEMBER )
            {
                format += "Nov ";
            }
            else // if( month == Calendar.DECEMBER )
            {
                format += "Dec ";
            }
                    
            //format += (cal.get(Calendar.MONTH) + 1);
            //format += "/";
            format += cal.get(Calendar.DAY_OF_MONTH);
            //format += "/";
            //format += cal.get(Calendar.YEAR);
        }            
        
        if(time)
        {
            if(! today)
            {
                format += " @ ";
            }

            int hour = cal.get(Calendar.HOUR);
            if( hour == 0)
            { 
                hour = 12;
            }
            format += hour;
            format += ":";
            String min = "";
            min += cal.get(Calendar.MINUTE);;
            if(min.length() == 1)
            {
                min = "0" + min;
            }
            format += min;
            if( cal.get(Calendar.AM_PM) == Calendar.AM)
            {
                format += "am";
            }
            else
            {
                format += "pm";
            }
        }
        
        return format;
    }
        
        
} 
