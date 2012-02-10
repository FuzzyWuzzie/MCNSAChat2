package com.mcnsa.mcnsachat2.util;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsachat2.MCNSAChat2;

public class ChatManager {
	MCNSAChat2 plugin = null;
	ChannelManager channelManager = null;

	public ChatManager(MCNSAChat2 instance, ChannelManager cm) {
		plugin = instance;
		channelManager = cm;
	}

	public void handleChat(Player player, String message) {
		// figure out which channel the player is in
		String channel = channelManager.getPlayerChannel(player);

		// and get a list of everyone who is listening in
		ArrayList<String> listeners = channelManager.getAllListeners(channel, player);

		// TODO: check for spam

		// now send the message out!
		String outgoing = new String(plugin.config.options.chatFormat);
		outgoing = outgoing.replace("%channel", channelManager.getChannelColour(channel) + channel);
		outgoing = outgoing.replace("%prefix", plugin.permissions.getUser(player).getPrefix());
		outgoing = outgoing.replace("%suffix", plugin.permissions.getUser(player).getSuffix());
		outgoing = outgoing.replace("%player", player.getName());
		outgoing = outgoing.replace("%message", message);
		// now process colours..
		outgoing = plugin.processColours(outgoing);
		// now send it
		for(int i = 0; i < listeners.size(); i++) {
			// get the player associated with this name
			Player recipient = plugin.getServer().getPlayer(listeners.get(i));
			if(recipient != null) {
				recipient.sendMessage(outgoing);
			}
		}

		// and log it
		plugin.log("<" + channel + "> " + player.getName() + ": " + message);
	}
}
