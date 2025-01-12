package ru.shved255.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.logging.Level;

import org.yaml.snakeyaml.Yaml;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import ru.shved255.Main;

public class Players {
	
	private Main plugin = Main.getInst();
    private Configuration yml;
    private String fileName = "players.yml";
	
    public Players() {
    	try {
    		yml = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public boolean needVerifed(ProxiedPlayer player) {
    	UUID uuid = player.getUniqueId();
		String id = uuid.toString();
		if (!yml.contains(id)) {
		    return true; 
		}
		int hours = plugin.config().getHours();
		String oldIp = yml.getString(id + ".ip");
		String newIp = player.getAddress().getAddress().getHostAddress();
		if(!oldIp.equals(newIp)) {
		    return true; 
		}
		Instant oldInst = Instant.parse(yml.getString(id + ".date"));
		Instant newInst = Instant.now();
		long timeDiff = ChronoUnit.HOURS.between(oldInst, newInst);
		if(timeDiff >= hours) {
		    return true; 
		}
        return false; 
    }

    public boolean setVerifed(ProxiedPlayer player) {
        UUID uuid = player.getUniqueId();
        String id = uuid.toString();
        yml.set(id + ".ip", player.getAddress().getAddress().getHostAddress());
        yml.set(id + ".name", player.getName());
        yml.set(id + ".date", Instant.now().toString());
        try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(yml, new File(plugin.getDataFolder(), fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return true;
    }
	
}
