package tk.sznp.thebuttonautoflair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONArray;
import org.json.JSONObject;

public class PluginMain extends JavaPlugin
{ //Translated to Java: 2015.07.15.
	//A user, which flair isn't obtainable:
	//https://www.reddit.com/r/thebutton/comments/31c32v/i_pressed_the_button_without_really_thinking/
    // Fired when plugin is first enabled
    @Override
    public void onEnable()
    {
		System.out.println("The Button Auto-flair Plugin by NorbiPeti (:P)");
		//System.out.println("Original C# version: http://pastebin.com/tX8xCPbp");
		//System.out.println("The Java version is... Also made by the same person.");
		//System.out.println("With the help of StackOverflow and similar.");
		/*catch(MalformedURLException e)
		{
		}
		catch(IOException e)
		{
		}*/
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		//System.out.println("Registering commands...");
		this.getCommand("u").setExecutor(new Commands());
		this.getCommand("u").setUsage(this.getCommand("u").getUsage().replace('&', '�'));
		try {
    		File file=new File("flairsaccepted.txt");
    		if(file.exists())
    		{
				BufferedReader br=new BufferedReader(new FileReader("flairsaccepted.txt"));
				String line;
				while ((line = br.readLine()) != null)
				{
					AcceptedPlayers.add(line.replace("\n", ""));
				}
				br.close();
			}
    		file=new File("flairsignored.txt");
    		if(file.exists())
    		{
				BufferedReader br=new BufferedReader(new FileReader("flairsignored.txt"));
				String line;
				while ((line = br.readLine()) != null)
				{
					IgnoredPlayers.add(line.replace("\n", ""));
				}
				br.close();
    		}
		} catch (IOException e) {
			System.out.println("Error!\n"+e);
		}
		//System.out.println("Registering done.");
		Runnable r=new Runnable(){public void run(){ThreadMethod();}};
		Thread t=new Thread(r);
		t.start();
    }
    Boolean stop=false;
    // Fired when plugin is disabled
    @Override
    public void onDisable()
    {
    	try
    	{
			FileWriter fw;
			fw = new FileWriter("flairsaccepted.txt");
			fw.close();
			fw = new FileWriter("flairsignored.txt");
			fw.close();
    	}
    	catch(Exception e)
    	{
			System.out.println("Error!\n"+e);
    	}
    	for(String player : AcceptedPlayers)
    	{
    		File file=new File("flairsaccepted.txt");
			try {
				BufferedWriter bw=new BufferedWriter(new FileWriter(file, true));
				bw.write(player+"\n");
				bw.close();
			} catch (IOException e) {
				System.out.println("Error!\n"+e);
			}
    	}
    	for(String player : IgnoredPlayers)
    	{
    		File file=new File("flairsignored.txt");
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(player+"\n");
				bw.close();
			} catch (IOException e) {
				System.out.println("Error!\n"+e);
			}
    	}
		stop=true;
    }
    
    public void ThreadMethod() //<-- 2015.07.16.
    {
    	while(!stop)
    	{
			try
			{
				String body=DownloadString("https://www.reddit.com/r/TheButtonMinecraft/comments/3d25do/autoflair_system_comment_your_minecraft_name_and/.json?limit=1000");
				JSONArray json=new JSONArray(body).getJSONObject(1).getJSONObject("data").getJSONArray("children");
				for(Object obj : json)
				{
					JSONObject item = (JSONObject)obj;
					String author=item.getJSONObject("data").getString("author");
					String ign=item.getJSONObject("data").getString("body");
	                int start = ign.indexOf("IGN:") + "IGN:".length();
	                int end = ign.indexOf(' ', start);
	                if (end == -1 || end == start)
	                	end=ign.indexOf('\n', start); //2015.07.15.
	                if (end == -1 || end == start)
	                    ign = ign.substring(start);
	                else
	                    ign = ign.substring(start, end);
	                ign = ign.trim();
	                if(HasIGFlair(ign))
	                	continue;
					//System.out.println("Author: "+author);
					try {
					    Thread.sleep(10);
					} catch(InterruptedException ex) {
					    Thread.currentThread().interrupt();
					}
	                String[] flairdata = DownloadString("http://karmadecay.com/thebutton-data.php?users=" + author).replace("\"", "").split(":");
	                String flair;
	                if(flairdata.length > 1) //2015.07.15.
	                	flair = flairdata[1];
	                else
	                	flair="";
	                if (flair != "-1")
	                    flair = flair + "s";
	                else
	                    flair = "non-presser";
					String flairclass;
					if(flairdata.length>1)
						flairclass = flairdata[2];
					else
						flairclass="unknown";
	                SetFlair(ign, flair, flairclass, author);
				}
				Thread.sleep(10000);
			}
			catch(Exception e)
			{
				System.out.println("Error!\n"+e);
			}
    	}
    }
    
    public String DownloadString(String urlstr) throws MalformedURLException, IOException
    {
		URL url = new URL(urlstr);
		URLConnection con = url.openConnection();
		con.setRequestProperty("User-Agent", "TheButtonAutoFlair");
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		in.close();
		return body;
    }

    //It has to store offline player flairs too, therefore it can't use Player object
    public static Map<String, String> PlayerFlairs=new HashMap<String, String>();
    public static Map<String, String> PlayerUserNames=new HashMap<String, String>();
    //public Map<Player, String> PlayerFlairs=new HashMap<Player, String>();
    public static ArrayList<Player> Players=new ArrayList<Player>();
    public static ArrayList<String> AcceptedPlayers=new ArrayList<String>(); //2015.07.16.
    public static ArrayList<String> IgnoredPlayers=new ArrayList<String>(); //2015.07.16.
    public Boolean HasIGFlair(String playername)
    {
    	/*Player player=null;
    	for(Player p : Players)
    	{
    		if(p.getName()==playername)
    		{
				player=p;
				break;
    		}
    	}
    	if(player==null)
    		return false;*/
    	return PlayerFlairs.containsKey(playername);
    }
    
    public void SetFlair(String playername, String text, String flairclass, String username)
    {
    	String finalflair;
    	switch(flairclass)
    	{
    	case "press-1":
    		finalflair="�c("+text+")�r";
    		break;
    	case "press-2":
    		finalflair="�6("+text+")�r";
    		break;
    	case "press-3":
    		finalflair="�e("+text+")�r";
    		break;
    	case "press-4":
    		finalflair="�a("+text+")�r";
    		break;
    	case "press-5":
    		finalflair="�9("+text+")�r";
    		break;
    	case "press-6":
    		finalflair="�5("+text+")�r";
    		break;
    	case "no-press":
    		finalflair="�7(non-pr.)�r";
    		break;
    	case "cheater":
    		finalflair="�5("+text+")�r";
    		break;
		default:
			finalflair="";
			break;
    	}
    	PlayerFlairs.put(playername, finalflair);
    	PlayerUserNames.put(playername, username);
    	/*for(Player player : Players)
    	{
    		if(player.getName()==playername)
    		{
    			PlayerFlairs.put(player, finalflair);
    			break;
    		}
    	}*/
    	//System.out.println("SetFlair - playername: "+playername+" text: "+text+" flairclass: "+flairclass);
    	System.out.println("Added new flair to "+playername+": "+finalflair);
    	for(Player player : Players)
    	{
    		//System.out.println("Online player: "+player.getName());
    		//System.out.println("player.getName ("+player.getName()+") == playername ("+playername+"): "+(player.getName()==playername));
    		if(player.getName().equals(playername))
    		{
        		//System.out.println("DisplayName: "+player.getDisplayName());
    			//player.setDisplayName(player.getDisplayName()+finalflair);
    			AppendPlayerDisplayFlair(player, username, finalflair);
        		//System.out.println("DisplayName: "+player.getDisplayName());
    			break;
    		}
    	}
    }
    
    public static String GetFlair(Player player)
    { //2015.07.16.
    	String flair=PlayerFlairs.get(player.getName());
    	return flair==null ? "" : flair;
    }
    
    public static void AppendPlayerDisplayFlair(Player player, String username, String flair)
    {
    	if(IgnoredPlayers.contains(player.getName()))
    		return;
    	if(AcceptedPlayers.contains(player.getName()))
    		player.setDisplayName(player.getDisplayName()+flair);
    	else
    		player.sendMessage("�9Are you Reddit user "+username+"?�r �6Type /u accept or /u ignore�r");
    	
    }
}
