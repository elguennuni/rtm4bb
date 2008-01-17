/*
 * FontColorRichTextField.java
 *
 * © <your company here>, 2003-2007
 * Confidential and proprietary.
 */

package org.emerick.rtm;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;


/**
 * 
 */
public class FontColorField extends LabelField
{
    
    private int color = 0x00000000;
    
    public FontColorField(String text, long style, int color)
    {
        super(text, style);
        this.color = color;
        int height = Font.getDefault().getHeight() - 3;
        setFont(Font.getDefault().derive(Font.PLAIN, height));
    }
    
    public void setColor(int color)
    {
        this.color = color;
    }
    
    public int getColor()
    {
        return color;
    }
    
    public void paint(Graphics g)
    {
        g.setColor(color);
        super.paint(g);
    }
} 
