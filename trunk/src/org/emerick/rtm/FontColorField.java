/*
 * FontColorRichTextField.java
 *
 * 
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
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
    private int onFocus = 0x00ffffff;
    private int current;
    
    public FontColorField(String text, long style, int color)
    {
        super(text, style);
        this.color = color;
        int height = Font.getDefault().getHeight() - 3;
        setFont(Font.getDefault().derive(Font.PLAIN, height));
    }
    public FontColorField(String text, long style, int color, Font font)
    {
        super(text, style);
        this.color = color;
        setFont(font);
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
        //g.setBackgroundColor(0x00359AFF);
        //g.clear();
        g.setColor(color);
        super.paint(g);
    }
} 
