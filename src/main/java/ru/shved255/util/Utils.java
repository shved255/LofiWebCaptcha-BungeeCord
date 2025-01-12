package ru.shved255.util;

import java.util.Base64;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import ru.shved255.Main;

public class Utils {

	private Main plugin = Main.getInst();
	
    public String code(String input) {
        if(input == null || input.isEmpty()) {
            return "";
        }
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(input.getBytes());
    }
    
	public void sendMessage(ProxiedPlayer player, String messageString) {
		BaseComponent message = new TextComponent(messageString);
		player.sendMessage(message);
	}
	
	public void connectPlayer(ProxiedPlayer player, String server) {
		ServerInfo target = ProxyServer.getInstance().getServerInfo(server);
		player.connect(target);
	}
	
	public void sendTitle(ProxiedPlayer player, String subtitleString, String titleString) {
		BaseComponent subTitle = new TextComponent(subtitleString);
		BaseComponent title = new TextComponent(titleString);
		plugin.getServer().createTitle().title(title).subTitle(subTitle).send(player);
	}
	
}
