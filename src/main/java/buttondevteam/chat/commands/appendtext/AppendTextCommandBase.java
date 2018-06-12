package buttondevteam.chat.commands.appendtext;

import buttondevteam.chat.ChatPlayer;
import buttondevteam.chat.listener.PlayerListener;
import buttondevteam.lib.chat.Channel;
import buttondevteam.lib.chat.CommandClass;
import buttondevteam.lib.chat.TBMCChatAPI;
import buttondevteam.lib.chat.TBMCCommandBase;
import buttondevteam.lib.player.TBMCPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandClass(modOnly = false, excludeFromPath = true)
public abstract class AppendTextCommandBase extends TBMCCommandBase {

	public abstract String[] GetHelpText(String alias);

	public abstract String GetAppendedText();

	@Override
	public boolean OnCommand(CommandSender sender, String alias, String[] args) {
		String msg = GetAppendedText();
		for (int i = args.length - 1; i >= 0; i--)
			msg = args[i] + " " + msg;
		if (sender instanceof Player)
			TBMCChatAPI.SendChatMessage(
					TBMCPlayer.getPlayer(((Player) sender).getUniqueId(), ChatPlayer.class).CurrentChannel, sender,
					msg, true);
		else if (sender.isOp())
			TBMCChatAPI.SendChatMessage(PlayerListener.ConsoleChannel, sender, msg);
		else
			TBMCChatAPI.SendChatMessage(Channel.GlobalChat, sender, msg);
		return true;
	}
}
