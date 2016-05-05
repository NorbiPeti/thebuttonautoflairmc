package io.github.norbipeti.thebuttonmc;

public class Channel {
	public final String DisplayName;
	public final String Color;
	public final String Command;

	public Channel(String displayname, String color, String command) {
		DisplayName = displayname;
		Color = color;
		Command = command;
	}

	public static Channel GlobalChat = new Channel("§fg§f", "white", "g");
	public static Channel TownChat = new Channel("§3TC§f", "dark_aqua", "tc");
	public static Channel NationChat = new Channel("§6NC§f", "gold", "nc");
	public static Channel AdminChat = new Channel("§cADMIN§f", "red", "a");
	public static Channel ModChat = new Channel("§9MOD§f", "blue", "mod");
}
