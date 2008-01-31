/*
 * ThisWeekListField.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.Bitmap;



/**
 * @author Jason Emerick
 */
public class ThisWeekListField extends ListField implements ListFieldCallback
{

    private TableRowManager[] rows;
    private Font font;
    private RTM rtm;
    
    public ThisWeekListField(RTM rtm)
    {
        super(2);
        this.rtm = rtm;
        setEmptyString("* No Menu!!! *", DrawStyle.HCENTER);
        setCallback(this);
        setRowHeight(36);
        rows = new TableRowManager[6];
        
        font = Font.getDefault();

        // create a table row manager
        rows[0] = new TableRowManager();
        // set the menu item name
        rows[0].add(new RichTextField("Today", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[0].add(new FontColorField("(" + rtm.countDueToday() + ")", DrawStyle.ELLIPSIS | Field.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
        // create a table row manager
        rows[1] = new TableRowManager();
        // set the menu item name
        rows[1].add(new RichTextField("Tomorrow", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[1].add(new FontColorField("(" + rtm.countDueTomorrow() + ")", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
        // create a table row manager
        rows[2] = new TableRowManager();
        // set the menu item name
        rows[2].add(new RichTextField("This Week", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[2].add(new FontColorField("(18)", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
        // create a table row manager
        rows[3] = new TableRowManager();
        // set the menu item name
        rows[3].add(new RichTextField("Lists", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[3].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
        // create a table row manager
        rows[4] = new TableRowManager();
        // set the menu item name
        rows[4].add(new RichTextField("Tags", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[4].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
        // create a table row manager
        rows[5] = new TableRowManager();
        // set the menu item name
        rows[5].add(new RichTextField("Locations", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[5].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
        
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

            // Restore the graphics context.
            g.popContext();
        }
        
        // Arrages this manager's controlled fields from left to right within
        // the enclosing table's columns.
        protected void sublayout(int width, int height)
        {
            
            int preferredWidth = getPreferredWidth();
            int preferredHeight = getPreferredHeight();
            
            Field field = getField(0);
            layoutChild(field, preferredWidth - 110, preferredHeight);
            setPositionChild(field, 35, 12);
            
            field = getField(1);
            layoutChild(field, 75, preferredHeight);
            setPositionChild(field, preferredWidth-75, 11);

            setExtent(preferredWidth, preferredHeight);
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
        ThisWeekListField list = (ThisWeekListField) listField;
        TableRowManager rowManager = list.rows[index];
        rowManager.drawRow(g, 0, y, width, list.getRowHeight());
    }
    
    public Object get(ListField list, int index)
    {
        return null;
    }
    
    public int indexOfList(ListField list, String p, int s)
    {
        return -1;
    }
    
    public int getPreferredWidth(ListField list)
    {
        return Graphics.getScreenWidth();
    }
    
   
    protected boolean trackwheelClick(int status, int time)
    {
        int index = getSelectedIndex();
        
        // if today is selected
        if( index == 0 )
        {
            
            UiApplication.getUiApplication().pushScreen(new TaskListScreen(rtm, "Today", rtm.dueToday()));
        }
        // if tomorrow is selected
        else if( index == 1 )
        {
            UiApplication.getUiApplication().pushScreen(new TaskListScreen(rtm, "Tomorrow", rtm.dueTomorrow()));
        }
        // if this week is selected
        else if( index == 2 )
        {
            
        }
        // if lists are selected
        else if( index == 3 )
        {
            
        }        
        // if tags are selected
        else if( index == 4 )
        {
            
        }
        // if locations are selected
        else if( index == 5 )
        {
            
        }
        else if( index == 6 )
        {
            
        }
        
        
        return true;
    }
     
        
} 
