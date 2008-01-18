/*
 * TaskListField.java
 *
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.Bitmap;

/**
 * @author Jason Emerick
 */
public class TaskListField extends ListField implements ListFieldCallback
{
    
    private Task[] tasks;
    private TableRowManager[] rows;
    private Bitmap p1;
    private Bitmap p2;
    private Bitmap p3;
    
    public TaskListField(Task[] tasks)
    {
        this.tasks = tasks;
        int numRows = tasks.length;
        numRows = 8;
        
        rows = new TableRowManager[numRows];
        
        p1 = Bitmap.getBitmapResource("p1.png");
        p2 = Bitmap.getBitmapResource("p2.png");
        p3 = Bitmap.getBitmapResource("p3.png");
        
    
        rows[0] = new TableRowManager();
        rows[0].add(new BitmapField(p1));
        LabelField boldField = new LabelField("A task due today", DrawStyle.ELLIPSIS);
        boldField.setFont(Font.getDefault().derive(Font.BOLD));
        rows[0].add(boldField);
        rows[0].add(new FontColorField("Inbox", DrawStyle.ELLIPSIS, 0x00878787));
        rows[0].add(new FontColorField("Jan 14, 1:00 PM", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        rows[1] = new TableRowManager();
        rows[1].add(new BitmapField(p2));
        rows[1].add(new LabelField("Another task name here", DrawStyle.ELLIPSIS));
        rows[1].add(new FontColorField("Inbox", DrawStyle.ELLIPSIS, 0x00878787));
        rows[1].add(new FontColorField("Feb 23, 12:00 PM", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        rows[2] = new TableRowManager();
        rows[2].add(new BitmapField(p3));
        rows[2].add(new LabelField("A really really really long task name", DrawStyle.ELLIPSIS));
        rows[2].add(new FontColorField("School", DrawStyle.ELLIPSIS, 0x00878787));
        rows[2].add(new FontColorField("Mar 19, 7:00 AM", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        rows[3] = new TableRowManager();
        rows[3].add(new NullField());
        rows[3].add(new LabelField("task w/ no due date/priority", DrawStyle.ELLIPSIS));
        rows[3].add(new FontColorField("Shopping", DrawStyle.ELLIPSIS, 0x00878787));
        rows[3].add(new NullField());
        rows[4] = new TableRowManager();
        rows[4].add(new NullField());
        rows[4].add(new LabelField("A task w/ no due time", DrawStyle.ELLIPSIS));
        rows[4].add(new FontColorField("ECE 4560", DrawStyle.ELLIPSIS, 0x00878787));
        rows[4].add(new FontColorField("Apr 31", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));  
        rows[5] = new TableRowManager();
        rows[5].add(new NullField());
        rows[5].add(new LabelField("A task w/ no due time", DrawStyle.ELLIPSIS));
        rows[5].add(new FontColorField("ECE 4560", DrawStyle.ELLIPSIS, 0x00878787));
        rows[5].add(new FontColorField("Apr 31", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        rows[6] = new TableRowManager();
        rows[6].add(new NullField());
        rows[6].add(new LabelField("A task w/ no due time", DrawStyle.ELLIPSIS));
        rows[6].add(new FontColorField("ECE 4560", DrawStyle.ELLIPSIS, 0x00878787));
        rows[6].add(new FontColorField("Apr 31", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        rows[7] = new TableRowManager();
        rows[7].add(new NullField());
        rows[7].add(new LabelField("A task w/ no due time", DrawStyle.ELLIPSIS));
        rows[7].add(new FontColorField("ECE 4560", DrawStyle.ELLIPSIS, 0x00878787));
        rows[7].add(new FontColorField("Apr 31", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));      
        
        int height = Font.getDefault().getHeight() * 2 + 4;
        
        setRowHeight(height);
        setEmptyString("* No Tasks *", DrawStyle.HCENTER);
        setSize(numRows);
        setCallback(this);
        
        
    }      
    
    private class TableRowManager extends Manager
    {
        public TableRowManager()
        {
            super(0);
        }
        
        // Causes the fields within this row manager to be layed out then
        // painted.
        public void drawRow(Graphics g, int x, int y, int width, int height)
        {
            // Arrange the cell fields within this row manager.
            layout(width, height);

            // Place this row manager within its enclosing list.
            setPosition(x, y);

            // Apply a translating/clipping transformation to the graphics
            // context so that this row paints in the right area.
            g.pushRegion(getExtent());

            // Paint this manager's controlled fields.
            subpaint(g);
            
            g.setColor(0x00CACACA);
            g.drawLine(0, 0, getPreferredWidth(), 0);
            g.drawLine(10, 0, 10, getPreferredHeight());

            // Restore the graphics context.
            g.popContext();
        }
        
        // Arrages this manager's controlled fields from left to right within
        // the enclosing table's columns.
        protected void sublayout(int width, int height)
        {
            // set the size and position of each field.
            int fontHeight = Font.getDefault().getHeight();
            int preferredWidth = getPreferredWidth();
            
            // start with the Bitmap Field of the priority icon
            Field field = getField(0);
            layoutChild(field, 10, 40);
            setPositionChild(field, 0, 0);
            
            // set the task name label field 
            field = getField(1);
            layoutChild(field, preferredWidth - 16, fontHeight+1);
            setPositionChild(field, 16, 3);
                        
            // set the list name label field 
            field = getField(2);
            layoutChild(field, 150, fontHeight+1);
            setPositionChild(field, 16, fontHeight+6);
            
            // set the due time name label field 
            field = getField(3);
            layoutChild(field, 150, fontHeight+1);
            setPositionChild(field, preferredWidth - 152, fontHeight+6);

            setExtent(preferredWidth, getPreferredHeight());
        }
        
        // The preferred width of a row is defined by the list renderer.
        public int getPreferredWidth()
        {
            return Graphics.getScreenWidth();
        }

        // The preferred height of a row is the "row height" as defined in the
        // enclosing list.
        public int getPreferredHeight()
        {
            return getRowHeight();
        }
    }
        
        

    // ListFieldCallback Implementation
    public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width)
    {
        TaskListField list = (TaskListField) listField;
        TableRowManager rowManager = list.rows[index];
        rowManager.drawRow(graphics, 0, y, width, list.getRowHeight());
    }
    
    public Object get(ListField list, int index)
    {
        return tasks[index];
    }
    
    public int indexOfList(ListField list, String p, int s)
    {
        return -1;
    }
    
    public int getPreferredWidth(ListField list)
    {
        return Graphics.getScreenWidth();
    }
    
    public ContextMenu getContextMenu()
    {
        ContextMenu menu = super.getContextMenu();
        
        menu.addItem(new CompleteTaskMenuItem());
        menu.addItem(new EditTaskMenuItem());
        menu.addItem(new DeleteTaskMenuItem());
        menu.addItem(new PostponeTaskMenuItem());
        menu.addItem(new SnoozeTaskMenuItem());
        menu.addItem(new UndoMenuItem());
        
        return menu;        
    }
    
    private final class CompleteTaskMenuItem extends MenuItem
    {
        public CompleteTaskMenuItem()
        {
            super("Complete", 100000, 1);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Completing Task Index: " + index);
        }
    }
    
    private final class EditTaskMenuItem extends MenuItem
    {
        public EditTaskMenuItem()
        {
            super("Edit", 100001, 2);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Editing Task Index: " + index);
        }
    }
    
    private final class DeleteTaskMenuItem extends MenuItem
    {
        public DeleteTaskMenuItem()
        {
            super("Delete", 100002, 3);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Deleting Task Index: " + index);
        }
    }
    
    private final class PostponeTaskMenuItem extends MenuItem
    {
        public PostponeTaskMenuItem()
        {
            super("Postpone", 100004, 4);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Postponing Task Index: " + index);
        }
    }
    
    private final class SnoozeTaskMenuItem extends MenuItem
    {
        public SnoozeTaskMenuItem()
        {
            super("Snooze", 100005, 5);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Snoozing Task Index: " + index);
        }
    }
    
    private final class UndoMenuItem extends MenuItem
    {
        public UndoMenuItem()
        {
            super("Undo", 200000, 6);
        }
        
        public void run()
        {
            int index = getSelectedIndex();
            Dialog.alert("Undoing");
        }
    }
    
 
            
        
} 
