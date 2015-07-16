package tk.sznp.thebuttonautoflair;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    // This method is called, when somebody uses our command
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if (sender instanceof Player) {
	        Player player = (Player) sender;
	        if(args.length<1)
	        	return false;
	        if(!PluginMain.PlayerFlairs.containsKey(player.getName()))
	        {
	        	player.sendMessage("Error: You need to write your username to the reddit thread at /r/TheButtonMinecraft");
	        	return true;
	        }
	        switch(args[0])
	        {
	        case "accept":
	        {
	        	if(PluginMain.IgnoredPlayers.contains(player.getName()))
	        		PluginMain.IgnoredPlayers.remove(player.getName());
	        	if(!PluginMain.AcceptedPlayers.contains(player.getName()))
	        	{
		        	String flair=PluginMain.PlayerFlairs.get(player.getName());
		    		player.setDisplayName(player.getDisplayName()+flair);
	        		PluginMain.AcceptedPlayers.add(player.getName());
	        		player.sendMessage("�6Your flair has been set:�r "+flair);
	        	}
	        	else
	        		player.sendMessage("�cYou already have this user's flair.�r");
	        	break;
	        }
	        case "ignore":
	        {
	        	if(PluginMain.AcceptedPlayers.contains(player.getName()))
	        		PluginMain.AcceptedPlayers.remove(player.getName());
        		if(!PluginMain.IgnoredPlayers.contains(player.getName()))
        		{
    	    		PluginMain.IgnoredPlayers.add(player.getName());
	        		String dname=player.getDisplayName();
		        	String flair=PluginMain.PlayerFlairs.get(player.getName());
		    		player.setDisplayName(dname.substring(0, dname.indexOf(flair)));
		    		player.sendMessage("�6You have ignored this request. You can still use /u accept though.�r");
        		}
        		else
        			player.sendMessage("�cYou already ignored this request.�r");
	        	break;
	        }
        	default:
        		return false;
	        }
	        return true;
		}
		
		// If the player (or console) uses our command correct, we can return true
		    return false;
	}
}
