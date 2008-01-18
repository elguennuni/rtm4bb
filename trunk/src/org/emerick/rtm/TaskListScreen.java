/*
 * TaskListScreen.java
 *
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.system.*;


/**
 * 
 * @author Jason Emerick
 */
final class TaskListScreen extends MainScreen{
    
    private TaskListField taskListField;
    private Task[] tasks;
    private TaskListScreenListener listener;
    
    
    public TaskListScreen(Task[] tasks) 
    {
        // call the parents constructor
        super(Manager.NO_VERTICAL_SCROLLBAR);
        
        this.tasks = tasks;
        taskListField = new TaskListField(tasks);
        
        listener = new TaskListScreenListener();
        addKeyListener(listener);

        add(new LabelField("A title box for this list", Field.FIELD_HCENTER));
        add(taskListField);
        
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
