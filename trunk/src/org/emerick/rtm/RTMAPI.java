/*
 * RTMAPI.java
 *
 *
 *
 * This product uses the Remember The Milk API but is not endorsed or certified by Remember The Milk.
 */

package org.emerick.rtm;

import java.io.IOException;
import org.json.me.JSONObject;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import java.io.InputStream;
import java.util.Vector;
import net.rim.device.api.crypto.MD5Digest;
import net.rim.device.api.io.Base64InputStream;
//import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;




/**
 * 
 * @author Jason Emerick
 */
public class RTMAPI 
{
    private String key;
    private String secret;
    private String format;
    private String authToken;
    private String timeline;
    private Transaction lastTransaction;
    private String result;
    
    static final private String authURL = "http://www.rememberthemilk.com/services/auth/?";
    static final private String requestURL = "http://api.rememberthemilk.com/services/rest/?";
     private static final char[] hexChars ={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'}; 
    
    
    public RTMAPI(String key, String secret)
    {
        this.key = key;
        try{
            this.secret = new String(Base64InputStream.decode(secret));
        }
        catch(IOException e)
        {
        }
        format = "json";
        authToken = "";
        timeline = "";
        lastTransaction = null;
    }
    
    private boolean isJSONArray(String str)
    {
        return (str.startsWith("[") && str.endsWith("]"));
    }
    
    private boolean isEmpty(String str)
    {
        return (str.equals("[]"));
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
    
    private String httpRequest(String URL) throws RTMException
    {        
        try 
        {
            String result;
            HttpConnection conn = null;
            //conn = (HttpConnection)Connector.open(URL + ";deviceside=true");
            //conn = (HttpConnection)Connector.open(URL + ";deviceside=false;ConnectionUID=GPMDSNA01");
            conn = (HttpConnection)Connector.open(URL + appendConnectionString());
            
            InputStream input = conn.openInputStream();
                                
            byte[] data = new byte[256];
            int len = 0;
            StringBuffer raw = new StringBuffer();
            while(-1 != (len = input.read(data)))
            {
                raw.append(new String(data,0,len));
            }
            
            result = raw.toString();
            
            input.close();
            conn.close();
            
            return result;
        }
        catch(IOException e)
        {
           throw new RTMException("Error making HTTP API Request");
        }
    }
    
    private String appendConnectionString() 
    {
    	ServiceBook sb = ServiceBook.getSB();
		ServiceRecord[] records = sb.findRecordsByCid("IPPP");
		if(records != null) {
			for(int i = records.length-1; i >= 0; i--) {
				ServiceRecord rec = records[i];
				if(rec.isValid() && !rec.isDisabled()) {
					if(rec.getEncryptionMode() == ServiceRecord.ENCRYPT_RIM) {
						//MDS - disabled for now.
						//return ";deviceside=false";
					} else {
						//BIS
						return ";deviceside=false;ConnectionType=mds-public";
					}
				}
			}
		} 
		return ";deviceside=true"; // No IPPP records, fallback to TCP. User will need to enter the APN settings in the Options app.
    }
    
    public String getFrob() throws RTMException
    {
        try
        {
            URLBuilder url = new URLBuilder(secret,false);
        
            url.append("api_key", key);

            url.append("format", format);
            
            url.append("method", "rtm.auth.getFrob"); 
            
            String result = httpRequest(url.getURL());
        
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                return rsp.getString("frob");
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
           throw new RTMException(e.toString());
        }
        
        
    }
    
    public String getAuthURL(String frob) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,true);
        
        url.append("api_key", key);

        url.append("frob", frob);
        
        url.append("perms", "delete");
        
        return url.getURL();
    }
    
    public String getAuthToken(String frob) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("format", format);
        
        url.append("method", "rtm.auth.getToken");
        
        url.append("frob", frob);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject auth = rsp.getJSONObject("auth");
                
                authToken = auth.getString("token");
                
                return authToken;
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
           throw new RTMException(e.toString());
        }
    }
    
    public void setAuthToken(String authToken)
    {
        this.authToken = authToken;
    }
    
    public boolean isAuthorized() throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.auth.checkToken");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                return true;
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public User getUser() throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.auth.checkToken");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject auth = rsp.getJSONObject("auth");
                JSONObject user = auth.getJSONObject("user");
                
                return new User(user.getString("username"), user.getString("fullname"), user.getString("id"));
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    
    public Settings getSettings() throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.settings.getList");
        
        String result = httpRequest(url.getURL());
 
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            // {"rsp":{"stat":"ok","settings":{"timezone":"America\/New_York","dateformat":"1","timeformat":"0","defaultlist":"","language":""}}}
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject settings = rsp.getJSONObject("settings");
                
                
                return new Settings(settings.getString("timezone"), settings.getString("dateformat"), settings.getString("timeformat"), settings.getString("defaultlist"), settings.getString("language"));
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public String getTimeline() throws RTMException
    {
       URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.timelines.create");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                timeline = rsp.getString("timeline");
                return timeline;
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
        
    
    public Vector getTasks(String list_id, String filter, int offset)
    {
        Vector tasks = new Vector();
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);

        if(filter.length() > 0)
        {
            url.append("filter", filter);
        }
        
        url.append("format", format);
        
        if(list_id.length() > 0)
        {
            url.append("list_id", list_id);
        }
        
        url.append("method", "rtm.tasks.getList");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                if( isEmpty(rsp.getString("tasks")))
                {
                    return new Vector();
                }
                
                JSONObject t = rsp.getJSONObject("tasks");
                String str = t.getString("list");
                int length;
                JSONArray alllists = null;
                if(isJSONArray(str))
                {
                    alllists = t.getJSONArray("list");
                    length = alllists.length();
                }
                else
                {
                    length = 1;
                }
                                    
                for(int x = 0; x < length; x++)
                {
                    JSONObject list;
                    if(length > 1)
                    {
                        list = alllists.getJSONObject(x);
                    }
                    else
                    {
                        list = t.getJSONObject("list");
                    }
                    
                    String listid = list.getString("id");
                    
                    JSONArray taskseries = null;
                    int numOfTasks;
                    if( isJSONArray(list.getString("taskseries")))
                    {
                        taskseries = list.getJSONArray("taskseries");
                        numOfTasks = taskseries.length();
                    }
                    else
                    {
                        numOfTasks = 1;
                    }
                    
                    for(int y = 0; y < numOfTasks; y++)
                    {
                        JSONObject task;
                        if( numOfTasks > 1)
                        {
                            task = taskseries.getJSONObject(y);
                        }
                        else
                        {
                            task = list.getJSONObject("taskseries");
                        }
                        
                        Task theTask = new Task(listid);
                        theTask.setTaskseriesID(task.getString("id"));
                        theTask.setCreated(task.getString("created"));
                        theTask.setModified(task.getString("modified"));
                        theTask.setName(task.getString("name"));
                        theTask.setSource(task.getString("source"));
                        theTask.setURL(task.getString("url"));
                        theTask.setLocationID(task.getString("location_id"));
                        
                        if( ! isEmpty(task.getString("tags")))
                        {
                            JSONObject tags = task.getJSONObject("tags");
                            if(isJSONArray(tags.getString("tag")))
                            {
                                JSONArray multipleTags = tags.getJSONArray("tag");
                                for(int z = 0; z < multipleTags.length(); z++)
                                {
                                    theTask.addTag(multipleTags.getString(z));
                                }
                            }
                            else
                            {
                                theTask.addTag(tags.getString("tag"));
                            }
                        }
                        
                        if( ! isEmpty(task.getString("participants")))
                        {
                            JSONObject parts = task.getJSONObject("participants");
                            if(isJSONArray(parts.getString("contact")))
                            {
                                JSONArray contacts = parts.getJSONArray("contact");
                                for(int z = 0; z < contacts.length(); z++)
                                {
                                    JSONObject contact = contacts.getJSONObject(z);
                                    theTask.addParticipant(new User(contact.getString("username"), contact.getString("fullname"), contact.getString("id")));
                                }
                            }
                            else
                            {
                               JSONObject contact = parts.getJSONObject("contact");
                               theTask.addParticipant(new User(contact.getString("username"), contact.getString("fullname"), contact.getString("id")));
                            }
                        }
                        
                        if( ! isEmpty(task.getString("notes")))
                        {
                            JSONObject allnotes = task.getJSONObject("notes");
                            if(isJSONArray(allnotes.getString("note")))
                            {
                                JSONArray notes = allnotes.getJSONArray("note");
                                for(int z = 0; z < notes.length(); z++)
                                {
                                    JSONObject note = notes.getJSONObject(z);
                                    theTask.addNote(new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t")));
                                }
                            }
                            else
                            {
                               JSONObject note = allnotes.getJSONObject("note");
                               theTask.addNote(new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t")));
                            }
                        }
                        
                        if( ! isJSONArray(task.getString("task")))
                        {
                            JSONObject taskDetails = task.getJSONObject("task");
                            theTask.setTaskID(taskDetails.getString("id"));
                            theTask.setDue(taskDetails.getString("due"), offset);
                            theTask.setHasDueTime(taskDetails.getString("has_due_time"));
                            theTask.setAdded(taskDetails.getString("added"));
                            theTask.setCompleted(taskDetails.getString("completed"));
                            theTask.setDeleted(taskDetails.getString("deleted"));
                            theTask.setPriority(taskDetails.getString("priority"));
                            theTask.setPostponed(taskDetails.getString("postponed"));
                            theTask.setEstimate(taskDetails.getString("estimate"));
                            
                            tasks.addElement(theTask);
                        }
                        else
                        {
                            JSONArray taskArray = task.getJSONArray("task");
                            for(int z = 0; z < taskArray.length(); ++z)
                            {
                                JSONObject multiTask = taskArray.getJSONObject(z);
                                Task taskCopy = new Task(theTask);
                                taskCopy.setTaskID(multiTask.getString("id"));
                                taskCopy.setDue(multiTask.getString("due"), offset);
                                taskCopy.setHasDueTime(multiTask.getString("has_due_time"));
                                taskCopy.setAdded(multiTask.getString("added"));
                                taskCopy.setCompleted(multiTask.getString("completed"));
                                taskCopy.setDeleted(multiTask.getString("deleted"));
                                taskCopy.setPriority(multiTask.getString("priority"));
                                taskCopy.setPostponed(multiTask.getString("postponed"));
                                taskCopy.setEstimate(multiTask.getString("estimate"));
                                
                                tasks.addElement(taskCopy);
                            }
                        }    
                    }
                }
                    
               //Task[] taskArray = new Task[tasks.size()];
               //tasks.copyInto(taskArray); 
               return tasks;
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Vector getLists() throws RTMException
    {
        Vector listsVector = new Vector();
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.getList");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject allLists = rsp.getJSONObject("lists");
                JSONArray lists = allLists.getJSONArray("list");
                for(int x = 0; x < lists.length(); x++)
                {
                    JSONObject list = lists.getJSONObject(x);
                    if( list.getString("smart").equals("1") )
                    {
                        listsVector.addElement(new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                        list.getString("smart"), list.getString("sort_order"), list.getString("filter")));
                    }
                    else
                    {
                        listsVector.addElement(new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                        list.getString("smart"), list.getString("sort_order"), ""));
                    }
                }
                
                //List[] listArray = new List[listsVector.size()];
                //listsVector.copyInto(listArray); 
                return listsVector;
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public List deleteList(String listid) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.delete");
        
        url.append("list_id", listid);
        
        url.append("timeline", timeline);
             
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));

                JSONObject list = rsp.getJSONObject("list");
                if( list.getString("smart").equals("1") )
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), list.getString("filter"));
                }
                else
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), "");
                }
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public List archiveList(String listid) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.archive");
        
        url.append("list_id", listid);
        
        url.append("timeline", timeline);     
       
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));

                JSONObject list = rsp.getJSONObject("list");
                if( list.getString("smart").equals("1") )
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), list.getString("filter"));
                }
                else
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), "");
                }
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
     public boolean undo() throws RTMException
    {
        if(lastTransaction == null || ! lastTransaction.isUndoable())
        {
            return false;
        }
        
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.undo");
        
        url.append("timeline", timeline);

        url.append("transaction_id", lastTransaction.getID());

        String result = httpRequest(url.getURL());
                
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                return true;
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public List unarchiveList(String listid) throws RTMException
    {
       URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.unarchive");
        
        url.append("list_id", listid);
        
        url.append("timeline", timeline);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));

                JSONObject list = rsp.getJSONObject("list");
                if( list.getString("smart").equals("1") )
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), list.getString("filter"));
                }
                else
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), "");
                }
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public List setListName(String listid, String name) throws RTMException
    {
       
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        url.append("format", format);
        
        url.append("method", "rtm.lists.setName");
        
        url.append("list_id", listid);
        
        url.append("name", name);
        
        url.append("timeline", timeline);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));

                JSONObject list = rsp.getJSONObject("list");
                if( list.getString("smart").equals("1") )
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), list.getString("filter"));
                }
                else
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), "");
                }
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public List addList(String name, String filter) throws RTMException
    {
       
        URLBuilder url = new URLBuilder(secret,false);
        
        url.append("api_key", key);
        
        url.append("auth_token", authToken);
        
        if( ! filter.equals("") )
        {
            url.append("filter", filter);
        }
        
        url.append("format", format);

        url.append("method", "rtm.lists.add");

        url.append("name", name);
        
        url.append("timeline", timeline);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));

                JSONObject list = rsp.getJSONObject("list");
                if( list.getString("smart").equals("1") )
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), list.getString("filter"));
                }
                else
                {
                    return new List(list.getString("id"), list.getString("name"), list.getString("deleted"), list.getString("locked"), list.getString("archived"), list.getString("position"), 
                    list.getString("smart"), list.getString("sort_order"), "");
                }
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task addTask(String listid, String name) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.add");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", listid);
        url.append("name", name);
        url.append("parse", "1");
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
        
    }
    
    public Task addTags(String list_id, String taskseries_id, String task_id, String addtags) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.addTags");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("tags", addtags);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
        
    }
    
    public Task removeTags(String list_id, String taskseries_id, String task_id, String removetags) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.removeTags");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("tags", removetags);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
        
    }
    
    public Task completeTask(String list_id, String taskseries_id, String task_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.complete");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task uncompleteTask(String list_id, String taskseries_id, String task_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.uncomplete");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task deleteTask(String list_id, String taskseries_id, String task_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.delete");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task moveTaskPriority(String list_id, String taskseries_id, String task_id, String direction) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.movePriority");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("direction", direction);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task moveTask(String from_list_id, String to_list_id, String taskseries_id, String task_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.moveTo");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("from_list_id", from_list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("to_list_id", to_list_id);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    public Task postponeTask(String list_id, String taskseries_id, String task_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.postpone");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
    }
    
    private Task parseJSONTask(JSONObject list) throws JSONException
    {
        Task newTask = new Task();
        newTask.setListID(list.getString("id"));
        JSONObject taskSeries = list.getJSONObject("taskseries");
        newTask.setTaskseriesID(taskSeries.getString("id"));
        newTask.setCreated(taskSeries.getString("created"));
        newTask.setModified(taskSeries.getString("modified"));
        newTask.setName(taskSeries.getString("name"));
        newTask.setSource(taskSeries.getString("source"));
        newTask.setURL(taskSeries.getString("url"));
        newTask.setLocationID(taskSeries.getString("location_id"));
        if( ! isEmpty(taskSeries.getString("tags")))
        {
            JSONObject tags = taskSeries.getJSONObject("tags");
            if(isJSONArray(tags.getString("tag")))
            {
                JSONArray multipleTags = tags.getJSONArray("tag");
                for(int z = 0; z < multipleTags.length(); z++)
                {
                    newTask.addTag(multipleTags.getString(z));
                }
            }
            else
            {
                newTask.addTag(tags.getString("tag"));
            }
        }
        
        if( ! isEmpty(taskSeries.getString("participants")))
        {
            JSONObject parts = taskSeries.getJSONObject("participants");
            if(isJSONArray(parts.getString("contact")))
            {
                JSONArray contacts = parts.getJSONArray("contact");
                for(int z = 0; z < contacts.length(); z++)
                {
                    JSONObject contact = contacts.getJSONObject(z);
                    newTask.addParticipant(new User(contact.getString("username"), contact.getString("fullname"), contact.getString("id")));
                }
            }
            else
            {
                JSONObject contact = parts.getJSONObject("contact");
                newTask.addParticipant(new User(contact.getString("username"), contact.getString("fullname"), contact.getString("id")));
            }
        }
        
        if( ! isEmpty(taskSeries.getString("notes")))
        {
            JSONObject allnotes = taskSeries.getJSONObject("notes");
            if(isJSONArray(allnotes.getString("note")))
            {
                JSONArray notes = allnotes.getJSONArray("note");
                for(int z = 0; z < notes.length(); z++)
                {
                    JSONObject note = notes.getJSONObject(z);
                    newTask.addNote(new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t")));
                }
            }
            else
            {
                JSONObject note = allnotes.getJSONObject("note");
                newTask.addNote(new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t")));
            }
        }
        
        JSONObject taskDetails = taskSeries.getJSONObject("task");
        newTask.setTaskID(taskDetails.getString("id"));
        newTask.setDue(taskDetails.getString("due"), 5);
        newTask.setHasDueTime(taskDetails.getString("has_due_time"));
        newTask.setAdded(taskDetails.getString("added"));
        newTask.setCompleted(taskDetails.getString("completed"));
        newTask.setDeleted(taskDetails.getString("deleted"));
        newTask.setPriority(taskDetails.getString("priority"));
        newTask.setPostponed(taskDetails.getString("postponed"));
        newTask.setEstimate(taskDetails.getString("estimate"));
        
        
        return newTask;
    }
    
    public Task setTaskDueDate(String list_id, String taskseries_id, String task_id, String due, String has_due_time) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setDueDate");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! due.equals(""))
        {
            url.append("due", due);
            url.append("has_due_time", has_due_time);
            url.append("parse", "1");
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
        
    }
    
    public Task setTaskEstimate(String list_id, String taskseries_id, String task_id, String estimate) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setEstimate");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! estimate.equals(""))
        {
            url.append("estimate", estimate);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }
        
    }
    
    public Task setTaskLocation(String list_id, String taskseries_id, String task_id, String location_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setLocation");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! location_id.equals(""))
        {
            url.append("location_id", location_id);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Task setTaskName(String list_id, String taskseries_id, String task_id, String name) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setName");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("name", name);
        
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Task setTaskPriority(String list_id, String taskseries_id, String task_id, String priority) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setPriority");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! priority.equals(""))
        {
            url.append("priority", priority);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Task setTaskRecurrence(String list_id, String taskseries_id, String task_id, String recurrence) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setRecurrence");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! recurrence.equals(""))
        {
            url.append("repeat", recurrence);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Task setTaskTags(String list_id, String taskseries_id, String task_id, String tags) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setTags");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! tags.equals(""))
        {
            url.append("tags", tags);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Task setTaskURL(String list_id, String taskseries_id, String task_id, String URL) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.setURL");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        if(! URL.equals(""))
        {
            url.append("url", URL);
        }
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject list = rsp.getJSONObject("list");
                return parseJSONTask(list);
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        }   
    }
    
    public Note addNote(String list_id, String taskseries_id, String task_id, String title, String note_text) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.notes.add");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("list_id", list_id);
        url.append("taskseries_id", taskseries_id);
        url.append("task_id", task_id);
        url.append("note_title", title);
        url.append("note_text", note_text);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject note = rsp.getJSONObject("note");
                return new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t"));
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        } 
    }
    
    public boolean deleteNote(String note_id) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.notes.delete");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("note_id", note_id);
        
        String result = httpRequest(url.getURL());
        
        System.out.println(result);
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                return true;
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        } 
    }
    
    public Note editNote(String note_id, String title, String note_text) throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.tasks.notes.edit");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        url.append("timeline", timeline);
        url.append("note_id", note_id);
        url.append("note_title", title);
        url.append("note_text", note_text);
        
        String result = httpRequest(url.getURL());
        
        System.out.println(result);
        
        try 
        {
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                JSONObject trans = rsp.getJSONObject("transaction");
                lastTransaction = new Transaction(trans.getString("id"), trans.getString("undoable"));
                JSONObject note = rsp.getJSONObject("note");
                return new Note(note.getString("id"), note.getString("created"), note.getString("modified"), note.getString("title"), note.getString("$t"));
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        } 
    }
    
    public Vector getLocations() throws RTMException
    {
        URLBuilder url = new URLBuilder(secret, false);
        
        url.append("method", "rtm.locations.getList");
        url.append("api_key", key);
        url.append("auth_token", authToken);
        url.append("format", format);
        
        String result = httpRequest(url.getURL());
        
        try 
        {
            Vector locationVector = new Vector();
            JSONObject jsonobject = new JSONObject(result);
            JSONObject rsp = jsonobject.getJSONObject("rsp");
            
            if(rsp.getString("stat").equalsIgnoreCase("ok"))
            {
                if(! isEmpty(rsp.getString("locations")))
                {
                    JSONObject locations = rsp.getJSONObject("locations");
                    if(isJSONArray(locations.getString("location")))
                    {
                        JSONArray locationArray = locations.getJSONArray("location");
                        for(int x = 0; x < locationArray.length(); x++)
                        {
                            JSONObject location = locationArray.getJSONObject(x);
                            locationVector.addElement( new Location(location.getString("id"), location.getString("name"), location.getString("longitude"), location.getString("latitude"),
                            location.getString("zoom"), location.getString("address"), location.getString("viewable")));
                        }
                            
                    }
                    else
                    {
                        JSONObject location = locations.getJSONObject("location");
                        locationVector.addElement( new Location(location.getString("id"), location.getString("name"), location.getString("longitude"), location.getString("latitude"),
                        location.getString("zoom"), location.getString("address"), location.getString("viewable")));
                    }
                }
                    
                //Location[] array = new Location[locationVector.size()];
                //locationVector.copyInto(array); 
                return locationVector;
                
            }
            else
            {
                JSONObject err = rsp.getJSONObject("err");
                throw new RTMException(err.getString("code") + " - " + err.getString("msg"));
            }
            
            
        }
        catch( JSONException e)
        {
            throw new RTMException(e.toString());
        } 
    }
        
} 
