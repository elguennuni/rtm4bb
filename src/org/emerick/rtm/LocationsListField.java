/*
 * LocationsListField.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.Bitmap;
import java.util.Vector;

/**
 * @author Jason Emerick
 */
public class LocationsListField extends ListField implements ListFieldCallback
{
    
    private Location[] locations;
    private Vector rows;
    private Font font;
    private RTM rtm;
    
    public LocationsListField(RTM rtm)
    {
        super(0);
        setEmptyString("Hooray, no locations here!", DrawStyle.HCENTER);
        setCallback(this);
        this.rtm = rtm;
        font = Font.getDefault();
        
        this.locations = rtm.getLocations();
        rows = new Vector();
        
        for(int x = 0; x < locations.length; ++x)
        {
            TableRowManager row = new TableRowManager();
            // set the list name
            row.add(new LabelField(locations[x].getName(), DrawStyle.ELLIPSIS));
            
            // set the number of due items in the given list
            row.add(new FontColorField("(" + rtm.countByLocation(locations[x].getID()) + ") ", DrawStyle.ELLIPSIS | Field.USE_ALL_WIDTH | DrawStyle.RIGHT, 0x00878787, font));
            
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
            
            //g.setColor(0x00CACACA);
            //g.drawLine(0, 0, getPreferredWidth(), 0);

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
        LocationsListField list = (LocationsListField) listField;
        TableRowManager rowManager = (TableRowManager)list.rows.elementAt(index);
        rowManager.drawRow(g, 0, y, width, list.getRowHeight());
    }
    
    public Object get(ListField list, int index)
    {
        return locations[index];
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
        UiApplication.getUiApplication().pushScreen(new TaskListScreen(rtm, locations[index].getName(), rtm.getTasksByLocation(locations[index].getID())));
        return true;
    }
    
 
            
        
} 
