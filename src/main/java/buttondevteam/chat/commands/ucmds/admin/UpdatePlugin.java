package buttondevteam.chat.commands.ucmds.admin;

import org.bukkit.command.CommandSender;
import buttondevteam.bucket.core.TBMCCoreAPI;

public class UpdatePlugin extends AdminCommandBase {

	@Override
	public String[] GetHelpText(String alias) {
		return new String[] { "§6---- Update plugin ----",
				"This command downloads the latest version of a TBMC plugin from GitHub",
				"To update a plugin: /" + alias + " <plugin>", "To list the plugin names: /" + alias };
	}

	@Override
	public boolean OnCommand(CommandSender sender, String alias, String[] args) {
		if (args.length == 0) {
			sender.sendMessage("Downloading plugin names...");
			boolean first = true;
			for (String plugin : TBMCCoreAPI.GetPluginNames()) {
				if (first) {
					sender.sendMessage("§6---- Plugin names ----");
					first = false;
				}
				sender.sendMessage("- " + plugin);
			}
			return true;
		} else {
			sender.sendMessage("Updating plugin...");
			String ret = "";
			if ((ret = TBMCCoreAPI.UpdatePlugin(args[0])).length() > 0) {
				sender.sendMessage(ret);
				return true;
			}
			sender.sendMessage("Updating done!");
			return true;
		}
	}

	@Override
	public String GetAdminCommandPath() {
		return "updateplugin";
	}

}
