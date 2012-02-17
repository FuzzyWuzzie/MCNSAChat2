package com.mcnsa.mcnsachat2.commands;

import java.util.Arrays;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsachat2.MCNSAChat2;
import com.mcnsa.mcnsachat2.util.Command;
import com.mcnsa.mcnsachat2.util.CommandInfo;
import com.mcnsa.mcnsachat2.util.ChatManager.Verbosity;

@CommandInfo(alias = "cmute", permission = "mute", usage = "[player]", description = "mutes [player] or lists those you have muted")
public class CommandMute implements Command {
	private static MCNSAChat2 plugin = null;
	public CommandMute(MCNSAChat2 instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		if(sArgs.length() < 1) {
			// get a list of who they have muted
			String[] muted = plugin.chatManager.mutedList(player);
			Arrays.sort(muted);
			String message = "&7Players you have muted: ";
			for(int i = 0; i < muted.length; i++) {
				message += "&f" + muted[i] + "&7, ";
			}
			player.sendMessage(plugin.processColours(message));
			return true;
		}
		
		// get the targeted player
		Player targetPlayer = plugin.getServer().getPlayer(sArgs.trim());
		// make sure they're a valid player
		if(targetPlayer == null) {
			player.sendMessage(plugin.processColours("&cError: I could not find the player '&f" + sArgs.trim() + "&c'!"));
			return true;
		}
		
		// toggle their muted status
		boolean muted = plugin.chatManager.toggleMuted(player, targetPlayer);
		
		// and return a message
		if(muted) {
			player.sendMessage(plugin.processColours(plugin.permissions.getUser(targetPlayer).getPrefix() + targetPlayer.getName() + " &7has been &cmuted&7!"));
			if(plugin.chatManager.getVerbosity(targetPlayer).compareTo(Verbosity.SHOWSOME) >= 0) {
				targetPlayer.sendMessage(plugin.processColours(plugin.permissions.getUser(player).getPrefix() + player.getName() + " &7has &cmuted&7 you!"));
			}
		}
		else {
			player.sendMessage(plugin.processColours(plugin.permissions.getUser(targetPlayer).getPrefix() + targetPlayer.getName() + " &7has been &aunmuted&7!"));
			if(plugin.chatManager.getVerbosity(targetPlayer).compareTo(Verbosity.SHOWSOME) >= 0) {
				targetPlayer.sendMessage(plugin.processColours(plugin.permissions.getUser(player).getPrefix() + player.getName() + " &7has &cunmuted&7 you!"));
			}
		}
		
		// and we handled it!
		return true;
	}
}