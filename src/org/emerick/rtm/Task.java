/*
 * Task.java
 *
 */

package org.emerick.rtm;


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
        
    
    
        
        
} 
