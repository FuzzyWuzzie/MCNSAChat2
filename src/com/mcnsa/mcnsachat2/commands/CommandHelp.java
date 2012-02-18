package com.mcnsa.mcnsachat2.commands;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.mcnsa.mcnsachat2.MCNSAChat2;
import com.mcnsa.mcnsachat2.util.ColourHandler;
import com.mcnsa.mcnsachat2.util.Command;
import com.mcnsa.mcnsachat2.util.CommandInfo;
import com.mcnsa.mcnsachat2.util.CommandManager.InternalCommand;

@CommandInfo(alias = "chelp", usage = "[page #]", description = "views the chat help menus (option: [page#])")
public class CommandHelp implements Command {
	private static MCNSAChat2 plugin = null;
	public CommandHelp(MCNSAChat2 instance) {
		plugin = instance;
	}

	public Boolean handle(Player player, String sArgs) {
		// see if we have args
		sArgs = sArgs.trim();
		//plugin.debug("sArgs: " + sArgs);
		Integer page = 0;
		try {
			page = Integer.parseInt(sArgs) - 1;
			//plugin.debug("page: " + page);
		}
		catch(Exception e) {
			
		}
		
		//plugin.debug(player.getName() + " called help for page: " + page);
		
		// get a list of all commands the user can use
		//plugin.debug("getting a list of all commands");
		InternalCommand[] commands = plugin.commandManager.listCommands();
		// now get a list of commands that we have permission to view
		//plugin.debug("checking commands");
		ArrayList<InternalCommand> permCommands = new ArrayList<InternalCommand>();
		for(int i = 0; i < commands.length; i++) {
			//plugin.debug("making sure we have permissions");
			if(commands[i].permissions.equals("") || plugin.hasPermission(player, commands[i].permissions)) {
				//plugin.debug("player has permission for " + commands[i].alias + ", adding");
				permCommands.add(commands[i]);
			}
		}
		//plugin.debug("have list..");
		
		// sort out the page numbers
		//plugin.debug("calculating pages");
		Integer totalPages = permCommands.size() / 5;
		if(permCommands.size() % 5 != 0) totalPages++;
		if(page < 0) page = 0;
		if(page >= totalPages) page = totalPages - 1;
		
		// calculate start and end indices
		Integer start = page * 5;
		Integer end = start + 5;
		if(end > permCommands.size()) {
			end = permCommands.size();
		}
		
		// and dispense the help!
		//plugin.debug("dispensing help");
		ColourHandler.sendMessage(player, "&7--- &fChat Help &7- &fPage &e" + (page + 1) + "&7/&e" + totalPages + " &f---");
		for(int i = start; i < end; i++) {
			ColourHandler.sendMessage(player, "&f/" + permCommands.get(i).alias + " &e" + permCommands.get(i).usage);
			ColourHandler.sendMessage(player, "    &7" + permCommands.get(i).description);
		}
		
		// and we handled it!
		return true;
	}
}

