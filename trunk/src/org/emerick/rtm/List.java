/*
 * List.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;




/**
 * 
 * @author Jason Emerick
 */
public class List {
    private String listid;
    private String name;
    private String deleted;
    private String locked;
    private String archived;
    private String position;
    private String smart;
    private String filter;
    private String sortorder;
    
    public List() 
    {
        listid = name = deleted = locked = sortorder = "";
        archived = position = smart = filter = "";
    }
    
    public List(String id, String name, String deleted, String locked, String archived, String position, String smart, String sortorder, String filter)
    {
        this.listid = id;
        this.name = name;
        this.deleted = deleted;
        this.locked = locked;
        this.archived = archived;
        this.position = position;
        this.smart = smart;
        this.sortorder = sortorder;
        this.filter = filter;
    }
    
    public String getListID()
    {
        return listid;
    }
    
    public String getName()
    {
        return name;
    }
    
    public boolean isDeleted()
    {
        return (deleted.startsWith("1"));
    }
    
    public boolean isLocked()
    {
        return (locked.startsWith("1"));
    }
    
    public boolean isArchived()
    {
        return (archived.startsWith("1"));
    }
    
    public String getPosition()
    {
        return position;
    }
    
    public boolean isSmart()
    {
        return (smart.startsWith("1"));
    }
    
    public String getFilter()
    {
        return filter;
    }
    
    public void archive()
    {
        archived = "1";
    }
    
    public void delete()
    {
        deleted = "1";
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void unArchive()
    {
        archived = "0";
    }
    
    public String getSortOrder()
    {
        return sortorder;
    }
} 
