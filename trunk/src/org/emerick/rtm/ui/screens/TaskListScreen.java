/*
 * TaskListScreen.java
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm.ui.screens;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.MainScreen;

import org.emerick.rtm.RTM;
import org.emerick.rtm.beans.Task;
import org.emerick.rtm.ui.lists.TaskListField;


/**
 * 
 * @author Jason Emerick
 */
public class TaskListScreen extends MainScreen{
    
    private TaskListField taskListField;
    private Task[] tasks;
    private TaskListScreenListener listener;
    private RTM rtm;
    
    
    public TaskListScreen(RTM rtm, String title, Task[] tasks) 
    {
        // call the parents constructor
        super();
        this.rtm = rtm;
        this.tasks = tasks;
        taskListField = new TaskListField(rtm, tasks);
        
        listener = new TaskListScreenListener();
        addKeyListener(listener);

        setTitle(new LabelField(title, LabelField.USE_ALL_WIDTH | DrawStyle.HCENTER));
        //add(new LabelField(title, Field.FIELD_HCENTER));
        add(taskListField);
        add(new SeparatorField());
        
        try
        {
            FontFamily fontFamily = FontFamily.forName("BBClarity");
            Font font = fontFamily.getFont(Font.PLAIN, 18);
            Font.setDefaultFont(font);
        }
        catch(ClassNotFoundException e)
        {
            System.out.println(e.toString());
        }
        
        removeFocus();
        
    }
   
    
    
    
    public static class TaskListScreenListener implements KeyListener
    {
        public boolean keyChar(char key, int status, int time)
        {
            boolean rv = false;
            switch(key)
            {
                case Characters.ENTER:
                    Dialog.alert("enter key pressed");
                    rv = true;
                    break;
            }
            
            return rv;
        }
        public boolean keyDown(int keycode, int time) 
        {
            return false;
        }
        public boolean keyRepeat(int keycode, int time) 
        {
            return false;
        }
        public boolean keyStatus(int keycode, int time) 
        {
            return false;
        }
        public boolean keyUp(int keycode, int time) 
        {
            return false;
        }
    }
                
                
    
    
        
    
    
   
    
    
            
} 
