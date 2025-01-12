package ru.shved255;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import ru.shved255.Listeners.Listeners;
import ru.shved255.util.Config;

public class Main extends Plugin {

	private static Main inst;
	private ProxyServer server = ProxyServer.getInstance();
	private Config config;
	
	@Override
	public void onEnable() {
		inst = this;
        if(!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), "config.yml");
        if(!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file1 = new File(getDataFolder(), "players.yml");
        if(!file1.exists()) {
            try (InputStream in1 = getResourceAsStream("players.yml")) {
                Files.copy(in1, file1.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    	config = new Config(this);
    	getServer().getPluginManager().registerListener(this, new Listeners());
	}
	
	@Override
	public void onDisable() {
		
	}

	public static Main getInst() {
		return inst;
	}

	public Config config() {
		return config;
	}

	public ProxyServer getServer() {
		return server;
	}
	
}
