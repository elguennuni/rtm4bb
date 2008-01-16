/*
 * Transaction.java
 *
 */

package org.emerick.rtm;




/**
 * 
 * @author Jason Emerick
 */
public class Transaction {
    private String id;
    private String undoable;
    
    public Transaction(String id, String undoable)
    {
        this.id = id;
        this.undoable = undoable;
    }
    
    public String getID()
    {
        return id;
    }
    
    public boolean isUndoable()
    {
        return (undoable.equals("1"));
    }
} 
