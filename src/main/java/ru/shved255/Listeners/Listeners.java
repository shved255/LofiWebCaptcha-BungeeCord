package ru.shved255.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;
import ru.shved255.Main;
import ru.shved255.GetVerify.SiteGet;
import ru.shved255.util.Players;
import ru.shved255.util.Utils;

public class Listeners implements Listener {

    private List<String> on = new ArrayList<>();
    private List<String> no = new ArrayList<>();
    private final SiteGet l = new SiteGet();
    private final Main plugin = Main.getInst();
    private final Utils u = new Utils();
    private final Players p = new Players();
    private final Map<ProxiedPlayer, ScheduledTask> tasks = new ConcurrentHashMap<>();
    private final Map<ProxiedPlayer, ScheduledTask> tasksStart = new ConcurrentHashMap<>();

    @EventHandler
    public void onJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if(p.needVerifed(player)) {
        	ScheduledTask task = plugin.getServer().getScheduler().schedule(plugin, () -> {
        		if(player.getServer() != null) {
        			if(player.getServer().getInfo() != null) {
        				if(player.getServer().getInfo().getName() != null) {
        					if(player.getServer().getInfo().getName().equals(plugin.config().getServerCheck())) {
        						start(player);
        						ScheduledTask taskGet = tasks.get(player);
        					taskGet.cancel();
        							tasks.remove(player);
        					}
        				}
        			}
        		}
        	}, 1, 1, TimeUnit.SECONDS);
        	if(!tasks.containsKey(player)) {
        		tasks.put(player, task);
        	}
        }
    }
    
    public void start(ProxiedPlayer player) {
        if(player.getServer().getInfo().getName().equals(plugin.config().getServerCheck())) {
            String nick = player.getName();
            String ip = player.getAddress().getAddress().getHostAddress();
            String base64 = u.code(nick);
            if(p.needVerifed(player)) {
                if(!on.contains(nick)) {
                    on.add(nick);
                }
                if(on.contains(nick)) {
                    if(plugin.config().getTitle()) {
                        u.sendTitle(player, plugin.config().getTitleUp(), plugin.config().getTitleDown());
                    }
                    String key = plugin.config().getKey();
                    final Boolean[] get = new Boolean[1];
					try {
						get[0] = l.Get(plugin.config().getSite() + "/save-ip.php?ip=" + nick + "/" + ip + "&secret=" + key);
					} catch (Exception e) {
						e.printStackTrace();
					}
                    String site = plugin.config().getSite() + "?username=" + base64;
                    ScheduledTask task = plugin.getServer().getScheduler().schedule(plugin, () -> {
                        if(player.isConnected()) {
                            player.sendMessage(plugin.config().getProverka().replace("{url}", site));
                            Boolean isDataFound = null;
							try {
								isDataFound = l.isDataPresent(nick + "/" + ip, plugin.config().getSite() + "/data.php?secret=" + key);
							} catch (Exception e) {
								e.printStackTrace();
							}
                            if(isDataFound != null && get[0] != null && get[0] && isDataFound) {
                                Boolean b = null;
								try {
									b = l.Get(plugin.config().getSite() + "/remove.php?id=" + nick + "/" + ip + "&file=verified_ips.txt&secret=" + key);
								} catch (Exception e) {
									e.printStackTrace();
								}
                                if(b) {
                                    on.remove(nick);
                                    no.add(nick);
                                }
                            }
                            if(no.contains(nick)) {
                                no.remove(nick);
                                player.sendMessage(plugin.config().getSuccess());
                                ScheduledTask taskM = tasksStart.get(player);
                                tasksStart.remove(player);
                                p.setVerifed(player);
                                if(plugin.config().getServerConnect() != null) {
                                	if(!(player.getServer().getInfo().getName() == plugin.config().getServerConnect())) {
                                		u.connectPlayer(player, plugin.config().getServerConnect());
                                	}
                                }
                                plugin.getServer().getScheduler().schedule(plugin, () -> {
                                    List<String> commandsPlayer = plugin.config().getCommandsPlayer();
                                    List<String> commandsServer = plugin.config().getCommandsServer(player);
                                    for(String command : commandsPlayer) {
                                        plugin.getServer().getPluginManager().dispatchCommand((CommandSender) player, command);
                                    }
                                    for(String command : commandsServer) {
                                        plugin.getServer().getPluginManager().dispatchCommand((CommandSender) plugin.getServer().getConsole(), command);
                                    }
                                }, 1, TimeUnit.SECONDS);
                                if(plugin.config().getTitle()) {
                                    u.sendTitle(player, plugin.config().getTitleUpNo(), plugin.config().getTitleDownNo());
                                }
                                taskM.cancel();
                            }
                        }
                    }, 1, 1, TimeUnit.SECONDS);
                    tasksStart.put(player, task);
                }
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        String nick = player.getName();
        on.remove(nick);
        no.remove(nick);
    }
    
    public void onChat(ChatEvent event) {   
        if(event.getSender() instanceof ProxiedPlayer) {
        	ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        	if(on.contains(player.getName())) {
        		event.setCancelled(true);
        	}
        }
    }

}
