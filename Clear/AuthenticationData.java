/*
 * AuthenticationData.java
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.clear;

import net.rim.device.api.util.Persistable;


/**
 * 
 */
public class AuthenticationData implements Persistable {
    
    public String frob;
    public String authToken;
    
} 
