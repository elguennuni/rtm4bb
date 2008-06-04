/*
 * TaskListField.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.ui.lists;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.util.Arrays;

import org.emerick.rtm.RTM;
import org.emerick.rtm.beans.Task;
import org.emerick.rtm.ui.field.FontColorField;

/**
 * @author Jason Emerick
 */
public class TaskListField extends ListField implements ListFieldCallback
{
    
    private Task[] tasks;
    private Vector rows;
    private Bitmap p1;
    private Bitmap p2;
    private Bitmap p3;
    private RTM rtm;
    
    public TaskListField(RTM rtm, Task[] tasks)
    {
        super(0, ListField.MULTI_SELECT);
        setRowHeight(40);
        setEmptyString("Hooray, no tasks here!", DrawStyle.HCENTER);
        setCallback(this);
        
        this.rtm = rtm;
        
       
        p1 = Bitmap.getBitmapResource("p1.png");
        p2 = Bitmap.getBitmapResource("p2.png");
        p3 = Bitmap.getBitmapResource("p3.png");
        
        this.tasks = tasks;
        rows = new Vector();
        
        for(int x = 0; x < tasks.length; ++x)
        {
            TableRowManager row = new TableRowManager();
            
            
            // SET THE PRIORITY BITMAP FIELD
            // if high priority, display p1 bitmap
            if(tasks[x].getPriority().equals("1"))
            {
                row.add(new BitmapField(p1));
            }
            // if priority is 2, set p2 bitmap
            else if(tasks[x].getPriority().equals("2"))
            {
                row.add(new BitmapField(p2));
            }
            // if priority is 3, set p3 bitmap
            else if(tasks[x].getPriority().equals("3"))
            {
                row.add(new BitmapField(p3));
            }
            // no priority set
            else
            {
                row.add( new NullField());
            }
            
            // SET THE TASK NAME LABELFIELD
            // if overdue, bold/underline
            LabelField task = new LabelField(tasks[x].getName(), DrawStyle.ELLIPSIS);
            // if due today, bold
            if(tasks[x].overdue())
            {
                task.setFont(Font.getDefault().derive(Font.BOLD | Font.UNDERLINED));
                System.out.println("OVERDUE");
            }
            else if(tasks[x].dueToday())
            {
                task.setFont(Font.getDefault().derive(Font.BOLD));
                System.out.println("TODAY");
            }
            
            row.add(task);
            
            // SET THE LIST NAME
            row.add(new FontColorField(rtm.getListName(tasks[x].getListID()), DrawStyle.ELLIPSIS, 0x00878787));
            
            // SET THE DUE DATE/TIME
            row.add(new FontColorField(tasks[x].getFormattedDue(), DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
            
            rows.addElement(row);
        }
        
        setSize(rows.size());             
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
    public void drawListRow(ListField listField, Graphics g, int index, int y, int width)
    {
        TaskListField list = (TaskListField) listField;
        TableRowManager rowManager = (TableRowManager)list.rows.elementAt(index);
        rowManager.drawRow(g, 0, y, width, list.getRowHeight());
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
    
    protected boolean trackwheelClick(int status, int time)
    {
        
        return true;
    }
    
    private final class CompleteTaskMenuItem extends MenuItem
    {
        public CompleteTaskMenuItem()
        {
            super("Complete", 100000, 1);
        }
        
        public void run()
        {
            int[] items = getSelection();
            final int index = getSelectedIndex();
            Thread t = new Thread() {
                public void run()
                {
                    rtm.completeTask(tasks[index]);
                }
            };
            
            t.start();
            
            while(t.isAlive());
            
            delete(index);
        }
    }
    
    public void delete(int index)
    {
        Arrays.removeAt(tasks, index);
        rows.removeElementAt(index);
        super.delete(index);
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
