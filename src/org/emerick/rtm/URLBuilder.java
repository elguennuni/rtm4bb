/*
 * URLBuilder.java
 *
 *  This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import java.io.IOException;
import java.util.Vector;
import java.lang.StringBuffer;

import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.util.Arrays;

/**
 * Construct a REST API HTTP GET URL request to RememberTheMilk
 * 
 * @author Jason Emerick
 * @see #append(String, String)
 * @see #getURL()
 */
public class URLBuilder {
    
    private StringBuffer url;
    private String secret;
    private Vector values;
    private int count;
    private static final String authURL = "http://www.rememberthemilk.com/services/auth/?";
    private static final String requestURL = "http://api.rememberthemilk.com/services/rest/?";
    private static final char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
    
    public URLBuilder(String secret, boolean auth)
    {
        this.secret = secret;
        this.url = new StringBuffer();
        if(auth)
        {
            url.append(authURL);
        }
        else
        {
            url.append(requestURL);
        }
        values = new Vector();
        count = 0;
    }
    
    /**
     * Append a key-value HTTP parameter to this URL request
     * @param name key name
     * @param value key's value
     */
    public void append(String name, String value)
    {
        if(count > 0)
        {
            url.append('&');
        }
        count++;
        
        url.append(name).append('=').append(value);
        
        String signature = name + value;
        
        values.addElement(signature);
    }
        
    public String getURL()
    {
        String[] sigs = new String[values.size()];
        values.copyInto(sigs);
        Arrays.sort(sigs, new StringComparator());
        
        StringBuffer signature = new StringBuffer();
        signature.append(secret);
        int len = sigs.length;
        for(int x = 0; x < len; x++)
        {
            signature.append(sigs[x]);
        }
                
        url.append("&api_sig=").append(md5(signature.toString()));
        
        return URLEncode(url.toString());
    }     
    
    /**
     * URL-Escape a URL
     * @param url input url
     * @return URL-encoded URL
     */
    private static String URLEncode(String url)
    {
    	//TODO consider URLEncodedPostData.class
        StringBuffer rv = new StringBuffer();
        int beginindex = 0;
        int endindex = url.indexOf(' ');
        
        while (endindex != -1)
        {
            rv.append(url.substring(beginindex, endindex));
            rv.append("%20");
            beginindex = endindex + 1;
            endindex = url.indexOf(' ', beginindex);
        }
        
        rv.append(url.substring(beginindex));
        
        return rv.toString();       
    }
    
    private static String hexStringFromBytes(byte[] b)
    {
        StringBuffer hex = new StringBuffer();
        int msb;
        int lsb;
        int i;
        
        // MSB maps to idx 0
        
        for (i = 0; i < b.length; i++)
        {
        	//TODO consider Integer.toHexString(int)
            msb = ((int)b[i] & 0x000000FF) / 16;
            lsb = ((int)b[i] & 0x000000FF) % 16;
            hex.append(hexChars[msb]);
            hex.append(hexChars[lsb]);
        }
        return(hex.toString());
    } 
    
    /**
     * Get the MD5 hash of a given UTF-8 String, and return it as hex-encoded
     * @param str String to hash
     * @return a hex-encoded MD5 hash of the input string
     * @throws RTMException any exception from getting the digest
     */
    private static String md5(String str) throws RTMException
    {
        try
        {
            MD5Digest md5 = new MD5Digest();
            byte[] inputBytes = str.getBytes("UTF-8");
            md5.update(inputBytes, 0, inputBytes.length );
            
            byte[] outputBytes = new byte[md5.getDigestLength()];
                   
            md5.getDigest(outputBytes, 0, true);
            
            String hash = hexStringFromBytes(outputBytes);
            
            return hash;
        }
        catch(IOException e)
        {
            throw new RTMException("Error Signing API Request");
        }
    }
} 
