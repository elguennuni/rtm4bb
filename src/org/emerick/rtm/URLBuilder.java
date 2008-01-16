/*
 * URLBuilder.java
 *
 */

package org.emerick.rtm;

import net.rim.device.api.crypto.MD5Digest;
import java.util.Vector;
import java.io.IOException;
import net.rim.device.api.util.Arrays;

/**
 * 
 * @author Jason Emerick
 */
public class URLBuilder {
    
    private String url;
    private String secret;
    private Vector values;
    private int count;
    static final private String authURL = "http://www.rememberthemilk.com/services/auth/?";
    static final private String requestURL = "http://api.rememberthemilk.com/services/rest/?";
    private static final char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
    
    public URLBuilder(String secret, boolean auth)
    {
        this.secret = secret;
        if(auth)
        {
            url = authURL;
        }
        else
        {
            url = requestURL;
        }
        values = new Vector();
        count = 0;
    }
    
    public void append(String name, String value)
    {
        if(count > 0)
        {
            url += "&";
        }
        count++;
        
        url += name;
        url += "=";
        url += value;
        
        String signature = name + value;
        
        values.addElement(signature);
    }
        
    public String getURL()
    {
        String[] sigs = new String[values.size()];
        values.copyInto(sigs);
        Arrays.sort(sigs, new StringComparator());
        
        String signature = secret;
        for(int x = 0; x < sigs.length; x++)
        {
            signature += sigs[x];
        }
                
        signature = md5(signature);
        url += ("&api_sig=" + signature);
        
        url = URLEncode(url);
        
        return url;
    }     
    
    private String URLEncode(String url)
    {
        String rv = "";
        int beginindex = 0;
        int endindex = url.indexOf(' ');
        
        while (endindex != -1)
        {
            rv += url.substring(beginindex, endindex);
            rv += "%20";
            beginindex = endindex + 1;
            endindex = url.indexOf(' ', beginindex);
        }
        
        rv += url.substring(beginindex);
        
        return rv;       
    }
    
    private String hexStringFromBytes(byte[] b)
    {
    
        String hex = "";
        
        int msb;
        
        int lsb = 0;
        int i;
        
        // MSB maps to idx 0
        
        for (i = 0; i < b.length; i++)
        {
            msb = ((int)b[i] & 0x000000FF) / 16;
        
            lsb = ((int)b[i] & 0x000000FF) % 16;
            hex = hex + hexChars[msb] + hexChars[lsb];
        }
        return(hex);
    } 
    
    private String md5(String str) throws RTMException
    {
        try
        {
            MD5Digest md5 = new MD5Digest();
        
            md5.update(str.getBytes("UTF-8"), 0,str.getBytes("UTF-8").length );
            
            byte[] buffer = new byte[md5.getDigestLength()];
                   
            md5.getDigest(buffer, 0, true);
            
            String hash = hexStringFromBytes(buffer);
            
            return hash;
        }
        catch(IOException e)
        {
            throw new RTMException("Error Signing API Request");
        }
    }
} 
