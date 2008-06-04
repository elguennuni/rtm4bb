/*
 * Note.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.beans;




/**
 * 
 * @author Jason Emerick
 */
public class Note {
    
    private String id;
    private String created;
    private String modified;
    private String title;
    private String note;
        
    public Note() 
    {
        id = created = modified = title = note = "";
    }
    
    public Note(String id, String created, String modified, String title, String note)
    {
        this.id = id;
        this.created = created;
        this.modified = modified;
        this.title = title;
        this.note = note;
    }
    
    public String getID()
    {
        return id;
    }
    
    public String getCreated()
    {
        return created;
    }
    
    public String getModified()
    {
        return modified;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getNote()
    {
        return note;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setNote(String note)
    {
        this.note = note;
    }
    
} 
