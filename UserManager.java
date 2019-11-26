import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;


//TODO: clean up Try/Catch for write and read methods
//TODO: Add specific criterion search methods (name etc.)
//TODO: add comments
//TODO: write test program and test this code
public class UserManager 
{
	private HashMap<Long, UserClass> userMap = new HashMap<Long, UserClass>();
	private boolean usersFull;
	private long lastUserID;
	
	public UserManager()
	{
		//userMap=null;
		usersFull=false;
		lastUserID=0;
		readUsers();
	}
	
	public UserClass searchUserID(long id)
	{
		return userMap.get(id);
	}
	
	public void addUser(UserClass aUser)
	{
		userMap.put(aUser.getUserID(),aUser);
	}
	
	public UserClass generateUser(String userName, String password, HashSet<Long> listedItemIDs)
	{
		lastUserID++;
		UserClass current = new UserClass(lastUserID, userName, password, listedItemIDs);
		userMap.put(lastUserID, current);
		
		System.out.println(userMap.toString());
		for (Map.Entry <Long, UserClass> user: userMap.entrySet()) {
			long res = user.getKey();
			System.out.println(searchUserID(res).getUserName());

		}
		return current;
		
	}
	
	public HashMap <Long, UserClass> getUserMap(){
		return userMap;
	}
	
	public UserClass searchCredentials(String name, String pass)
	{
		Iterator<Map.Entry<Long, UserClass>> itr = userMap.entrySet().iterator();
		while(itr.hasNext())
    	{
			UserClass tempUser=itr.next().getValue();
			String checkedName=tempUser.getUserName();
			String checkedPassword=tempUser.getPassword();
			if(name.equals(checkedName) && pass.equals(checkedPassword))
			{
				return tempUser;
			}
    	}
		return null;
		
	}
	
	public void readUsers()
	{
		try
		{
			File file = new File("userList.txt");
	        FileReader fr = new FileReader(file);
	        BufferedReader br = new BufferedReader(fr);
	        String line="";
	        boolean done=false;
	        boolean first=true;
	        while(!done)
	        {
	        	line=br.readLine();
	        	if(line==null)
	        	{
	        		done=true;
	        	}
	        	else if(first)
	        	{
	        		lastUserID=Long.parseLong(line);
	        		first=false;
	        	}
	        	else
	        	{
	        		String[] temp=new String[4];
	        		temp=line.split(" ");
	        		String[] tempListedItems=temp[3].split(".");
	        		HashSet<Long> listedItems=new HashSet<Long>();
	        		for(String tempListedItem: tempListedItems)
	        		{
	        			listedItems.add(Long.parseLong(tempListedItem));
	        		}
	        		
	        		UserClass tempUser=new UserClass(Long.parseLong(temp[0]), temp[1], temp[2], listedItems);
	            	userMap.put(Long.parseLong(temp[0]),tempUser);
	        	}
	        }
		    br.close();
	        fr.close();
	        usersFull=true;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("User file not found!");
		}
		catch(IOException e)
		{
			System.out.println("Users read error!");
		}

    
        
	}
	
	public void writeUsers()
	{
		try
		{
			File file = new File("userList.txt");
	        FileWriter fw = new FileWriter(file);
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(""+lastUserID);
	        bw.newLine();
	        while(!userMap.isEmpty())
	        {
	        	Iterator<Map.Entry<Long, UserClass>> itr = userMap.entrySet().iterator();
	        	while(itr.hasNext())
	        	{
	        		UserClass tempUser=itr.next().getValue();
	        		itr.remove();
	        		String tempStringID=""+tempUser.getUserID();
	        		String tempUserName=tempUser.getUserName();
	        		String tempPassword=tempUser.getPassword();
	            	String tempStringListedItems="";
	            	for(Long l: tempUser.getListedItemIDs())
	            	{
	            		tempStringListedItems+=(l+".");
	            		
	            	}
	            	if(tempStringListedItems.length()>0)
	            	{
	            		tempStringListedItems=tempStringListedItems.substring(0,tempStringListedItems.length()-1);
	            	}
	                String line=tempStringID + " " + tempUserName + " " + tempPassword + " " + tempStringListedItems;
	                bw.write(line);
	                bw.newLine();
	        	}
	        }
	        bw.close();
	        fw.close();
	        usersFull=false;
		}
		catch(FileNotFoundException e)
		{
			System.out.println("User file not found!");
		}
		catch(IOException e)
		{
			System.out.println("Users write error!");
		}
		
		
	}
}
