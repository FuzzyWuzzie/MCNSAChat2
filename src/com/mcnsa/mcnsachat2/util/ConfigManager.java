package com.mcnsa.mcnsachat2.util;

import com.mcnsa.mcnsachat2.MCNSAChat2;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ConfigManager {
	// store the main plugin for later access
	static MCNSAChat2 plugin = null;
	public ConfigOptions options = new ConfigOptions();
	public ConfigManager(MCNSAChat2 instance) {
		plugin = instance;
	}

	// load the configuration
	public void load(FileConfiguration config) {
		// load the chat radius
		plugin.debug("loading options...");
		
		options.localChatRadius = (float)config.getDouble("local-chat-radius");
		plugin.debug("local chat radius: " + options.localChatRadius.toString());
		
		options.defaultChannel = config.getString("default-channel");
		plugin.debug("default channel: " + options.defaultChannel);
		
		options.defaultColour = ColourHandler.translateName(config.getString("default-colour"));
		plugin.debug("default colour: " + options.defaultColour.toString());
		
		options.announceTimeouts = config.getBoolean("announce-timeouts");
		plugin.debug("announce timeouts: " + options.announceTimeouts.toString());

		options.spamConfig.messageLimit = (float)config.getDouble("spam.limit");
		plugin.debug("spam message limit: " + options.spamConfig.messageLimit);
		
		options.spamConfig.messagePeriod = (float)config.getDouble("spam.period");
		plugin.debug("spam message period: " + options.spamConfig.messagePeriod);
		
		// now get the hard channels!
		ConfigurationSection channelSection = config.getConfigurationSection("hard-channels");
		// and the list of all keys in here
		Set<String> channels = channelSection.getKeys(false);
		// and loop through each channel
		Iterator<String> it = channels.iterator();
		while(it.hasNext()) {
			String channelName = it.next();
			plugin.debug("found hard channel: " + channelName);
			
			// now create the hard config for this channel
			ChannelHardConfig channelConfig = new ChannelHardConfig();
			channelConfig.name = channelName;
			
			channelConfig.colour = ColourHandler.translateName(channelSection.getString(channelName + ".colour", "grey"));
			plugin.debug("\tchannel colour: " + channelConfig.colour);
			
			channelConfig.alias = channelSection.getString(channelName + ".alias", "");
			plugin.debug("\tchannel alias: " + channelConfig.alias);
			
			channelConfig.permission = channelSection.getString(channelName + ".permission", "allchannels");
			plugin.debug("\tchannel permission: " + channelConfig.permission);
			
			channelConfig.local = channelSection.getBoolean(channelName + ".local", false);
			plugin.debug("\tchannel local: " + channelConfig.local.toString());
			
			channelConfig.broadcast = channelSection.getBoolean(channelName + ".broadcast", false);
			plugin.debug("\tchannel broadcast: " + channelConfig.broadcast.toString());
			
			// and add it!
			options.hardChannels.put(channelName, channelConfig);
		}
	}

	// create a "class" in here to store config options!
	private class ConfigOptions {
		public HashMap<String, ChannelHardConfig> hardChannels = new HashMap<String, ChannelHardConfig>();
		public Float localChatRadius = new Float(200);
		public String defaultChannel = new String("");
		public String defaultColour = new String("grey");
		public Boolean announceTimeouts = true;
		public SpamConfig spamConfig = new SpamConfig(5.0f, 3.0f);
	}

	// spam configurations
	private class SpamConfig {
		public Float messageLimit = new Float(5);
		public Float messagePeriod = new Float(3);

		public SpamConfig(Float limit, Float period) {
			messageLimit = limit;
			messagePeriod = period;
		}
	}

	// hard-channel configurations
	private class ChannelHardConfig {
		@SuppressWarnings("unused")
		public String name = new String("");
		public String colour = new String("grey");
		public String permission = new String("channel");
		public Boolean local = false;
		public String alias = new String("");
		public Boolean broadcast = false;
	}
}
