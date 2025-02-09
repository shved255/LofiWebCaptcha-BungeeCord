package ru.shved255.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.shved255.Main;

public class Config {
    
    private Main plugin = Main.getInst();
    private Configuration cfg;
    private String success;
    private String proverka;
    private String site;
    private String key;
    private Boolean title;
    private String titleUp;
    private String titleDown;
    private String titleUpNo;
    private String titleDownNo;
    private String serverCheck;
    private String serverConnect;
    private int hours;
	private List<String> commandsPlayer;
	private List<String> commandsServer;
	private String kickPlayer;
    private int time;
    private Boolean serverConnectEnable;
	
    public Config(Main plugin) {
        this.plugin = plugin;
        try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), "config.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        success = cfg.getString("messages.success");
        proverka = cfg.getString("messages.message");
        kickPlayer = cfg.getString("messages.timeKick");
	    title = cfg.getBoolean("titles.titleOn");
	    titleUp = cfg.getString("titles.title");
	    titleDown = cfg.getString("titles.subTittle");
	    titleUpNo = cfg.getString("titles.titleNo");
	    titleDownNo = cfg.getString("titles.subTittleNo");
        site = cfg.getString("site");
        key = cfg.getString("key");
	    commandsPlayer = cfg.getStringList("playerCommands");
	    commandsServer = cfg.getStringList("serverCommands");
	    hours = cfg.getInt("hours");
	    serverCheck = cfg.getString("serverCheck");
	    serverConnect = cfg.getString("serverConnect");
	    time = cfg.getInt("time");
	    serverConnectEnable = cfg.getBoolean("serverConnectEnable");
    }
    
    public Configuration getConfig() {
        return this.cfg;
    }
    
    public String ChatColor(String text) {
        return Hex.toChatColorString(text);
    }

    public String getSuccess() {
        return ChatColor(success);
    }

    public String getProverka() {
        return ChatColor(proverka);
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }

	public List<String> getCommandsPlayer() {
		return commandsPlayer;
	}

	public List<String> getCommandsServer(ProxiedPlayer player) {
	    String nick = player.getName();
	    List<String> result = new ArrayList<>();
	    for(String command : this.commandsServer) {
	        result.add(command.replace("{PLAYER}", nick));
	    }
	    return result;
	}

	public Boolean getTitle() {
		return title;
	}

	public String getTitleUp() {
		return ChatColor(titleUp);
	}

	public String getTitleDown() {
		return ChatColor(titleDown);
	}

	public String getTitleUpNo() {
		return ChatColor(titleUpNo);
	}

	public String getTitleDownNo() {
		return ChatColor(titleDownNo);
	}

	public int getHours() {
		return hours;
	}

	public String getServerCheck() {
		return serverCheck;
	}

	public String getServerConnect() {
		return serverConnect;
	}

	public String getKickPlayer() {
		return ChatColor(kickPlayer);
	}

	public int getTime() {
		return time;
	}

	public Boolean getServerConnectEnable() {
		return serverConnectEnable;
	}
}
