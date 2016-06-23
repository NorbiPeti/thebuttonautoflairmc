package io.github.norbipeti.thebuttonmcchat.commands.ucmds;

import org.bukkit.command.CommandSender;

public final class UCommand extends UCommandBase {

	@Override
	public String[] GetHelpText(String alias) {
		return new String[] { "§6---- U commands ----",
				"Subcommands: help, accept, ignore, admin" };
	}

	@Override
	public boolean OnUCommand(CommandSender sender, String alias, String[] args) {
		return false; //TODO: Forward call to the correct handler
	}

	@Override
	public String GetUCommandName() {
		return "u"; //TODO: Same as at AdminCommand
	}

	@Override
	public boolean GetPlayerOnly() {
		return false;
	}

}
