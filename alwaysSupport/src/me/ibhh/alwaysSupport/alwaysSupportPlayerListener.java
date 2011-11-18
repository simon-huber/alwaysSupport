package me.ibhh.alwaysSupport;



import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
 
public class alwaysSupportPlayerListener extends PlayerListener{
       
	
	
        @SuppressWarnings("unused")
        private alwaysSupport plugin = null;
       
        public alwaysSupportPlayerListener( alwaysSupport plugin )
        {
                this.plugin = plugin;
        }
       
        public void onPlayerJoin( PlayerJoinEvent e )
        {
                Player p = e.getPlayer();
               
                
                if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
        		    PermissionManager permissions = PermissionsEx.getPermissionManager();

        			// Permission check
        		    if(permissions.has(p, "alwaysSupport.giveSupport")){
        		    	
        		    	
        		    e.getPlayer().sendMessage(ChatColor.GRAY + "[alwaysSupport]" + ChatColor.RED + "Ein Spieler hat einen Supportauftrag hinterlassen!");
        		    
        		    } else {
        		    // houston, we have a problem :)
        		    }
        		} else {
        		   Logger.getLogger("Minecraft").warning("PermissionsEx plugin are not found.");
        		} //Written by: t3hk0d3
        }
       
        public void onPlayerChat( PlayerChatEvent e )
        {
                Player p = e.getPlayer();
               
                p.sendMessage(ChatColor.GREEN + "Nachricht gesendet!");
        }
}
