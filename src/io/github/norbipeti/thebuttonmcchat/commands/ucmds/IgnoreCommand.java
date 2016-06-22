package io.github.norbipeti.thebuttonmcchat.commands.ucmds;

import io.github.norbipeti.thebuttonmcchat.FlairStates;
import io.github.norbipeti.thebuttonmcchat.MaybeOfflinePlayer;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class IgnoreCommand extends UCommandBase {

	@Override
	public String[] GetHelpText(String alias) {
		return new String[] {
				"§6---- Ignore flair ----",
				"Stop the \"write your name in the thread\" message from showing up",
				"Use /u ignore <username> if you commented from multiple accounts" };
	}

	@Override
	public boolean OnUCommand(CommandSender sender, String alias, String[] args) {
		final Player player = (Player) sender;
		MaybeOfflinePlayer p = MaybeOfflinePlayer.GetFromPlayer(player);
		if (p.FlairState.equals(FlairStates.Accepted)) {
			player.sendMessage("§cYou can only ignore the \"write yoőu rname in the thread\" message.");
			return true;
		}
		if (p.FlairState.equals(FlairStates.Commented)) {
			player.sendMessage("Sorry, but your flair isn't recorded. Please ask a mod to set it for you.");
			return true;
		}
		if (!p.FlairState.equals(FlairStates.Ignored)) {
			p.FlairState = FlairStates.Ignored;
			p.SetFlair(MaybeOfflinePlayer.FlairTimeNone);
			p.UserName = "";
			player.sendMessage("§bYou have ignored the message. You can still use /u accept to get a flair.§r");
		} else
			player.sendMessage("§cYou already ignored the message.§r");
		return true;
	}

	@Override
	public String GetUCommandName() {
		return "ignore";
	}

}
