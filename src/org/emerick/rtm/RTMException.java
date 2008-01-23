/*
 * RTMException.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;




/**
 * 
 * @author Jason Emerick
 */
public class RTMException extends RuntimeException
 {
    public RTMException()
    {
        super();
    }
    
    public RTMException(String message)
    {
        super(message);
    }
} 
