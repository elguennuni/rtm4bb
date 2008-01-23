/*
 * HomeMenuListField.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.Bitmap;

import net.rim.device.api.util.Arrays;

/**
 * @author Jason Emerick
 */
public class HomeMenuListField extends ListField implements ListFieldCallback
{

    private TableRowManager[] rows;
    
    public HomeMenuListField()
    {
        super(6);
        setEmptyString("* No Menu!!! *", DrawStyle.HCENTER);
        setCallback(this);
        setRowHeight(36);
        rows = new TableRowManager[6];

        // create a table row manager
        rows[0] = new TableRowManager();
        // set the menu item name
        rows[0].add(new LabelField("Today", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[0].add(new FontColorField("(5)", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
        // create a table row manager
        rows[1] = new TableRowManager();
        // set the menu item name
        rows[1].add(new LabelField("Tomorrow", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[1].add(new FontColorField("(4)", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
        // create a table row manager
        rows[2] = new TableRowManager();
        // set the menu item name
        rows[2].add(new LabelField("This Week", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[2].add(new FontColorField("(18)", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
        // create a table row manager
        rows[3] = new TableRowManager();
        // set the menu item name
        rows[3].add(new LabelField("Lists", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[3].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
        // create a table row manager
        rows[4] = new TableRowManager();
        // set the menu item name
        rows[4].add(new LabelField("Tags", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[4].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
        // create a table row manager
        rows[5] = new TableRowManager();
        // set the menu item name
        rows[5].add(new LabelField("Locations", DrawStyle.ELLIPSIS));
        // set the number of list items if there are any
        rows[5].add(new FontColorField("", DrawStyle.ELLIPSIS | LabelField.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787));
        
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
            layoutChild(field, preferredWidth - 75, preferredHeight);
            setPositionChild(field, 0, 0);
            
            field = getField(1);
            layoutChild(field, 75, preferredHeight);
            setPositionChild(field, preferredWidth-75, 0);

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
        HomeMenuListField list = (HomeMenuListField) listField;
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
        
        RTMAPI rtm = new RTMAPI("968d8045c952707d8d13f5187a93cb9f", "ODg5ZDhlMjkxNGJiYzkxZg==");
        rtm.setAuthToken("5a2808b55d06861172aae3f4e4ae2b1802399afd");
        
        // if today is selected
        if( index == 0 )
        {
            Task[] tasks = rtm.getTasks("","due:today AND status:incomplete");
            Arrays.sort(tasks, new TaskComparator());
            UiApplication.getUiApplication().pushScreen(new TaskListScreen(tasks));
        }
        // if tomorrow is selected
        if( index == 1 )
        {
            Task[] tasks = rtm.getTasks("","due:tomorrow AND status:incomplete");
            Arrays.sort(tasks, new TaskComparator());
            UiApplication.getUiApplication().pushScreen(new TaskListScreen(tasks));
        }
            
        
        
        return true;
    }
     
        
} 
