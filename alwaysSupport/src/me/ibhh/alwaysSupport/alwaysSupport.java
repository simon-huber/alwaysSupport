package me.ibhh.alwaysSupport;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class alwaysSupport extends JavaPlugin {
	
	//private alwaysSupportPlayerListener playerListener = null;
	private final alwaysSupportPlayerListener playerListener = new alwaysSupportPlayerListener (this);
	
	Connection cn = null;
	Statement stmt = null;
	
	@Override
	public void onDisable() {
		try {
			cn.close();
		} catch (SQLException e) {
			System.out.println("[alwaysSupport] Failed to close connection to DB!");
			e.printStackTrace();
			return;
		}
		
		System.out.println("[alwaysSupport] disabled!");
		
	}

	@Override
	public void onEnable() {
		
		try {
			File pluginConfig = new File(this.getDataFolder(), "config.yml");
			if(!(pluginConfig.exists()))
			{
				System.out.println("[alwaysSupport] Config file not found!");
				createConfig();
				System.out.println("[alwaysSupport] could not be enabled: Config file was missing!");
				return;
			}
		}
		catch (Exception e)
		{
			System.out.println("[alwaysSupport] could not be enabled: Exception occured while seaching config file.");
			e.printStackTrace();
			return;
		}
		
		String dbPath = this.getConfig().getString("dbPath", "unknown");
		String dbUser = this.getConfig().getString("dbUser", "unknown");
		String dbPassword = this.getConfig().getString("dbPassword", "unknown");
		
		if(dbPath == "unknown" || dbUser == "unknown" || dbPassword == "unknown")
		{
			System.out.println("[alwaysSupport] could not be enabled: Failed to connect to DB: Check config settings dbPath, dbUser and dbPassword");
			return;
			
		} else {
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				cn = DriverManager.getConnection("jdbc:mysql://" + dbPath, dbUser, dbPassword);
			}
			catch (Exception e) {
				System.out.println("[alwaysSupport] could not be enabled: Exception occured while trying to connect to DB");
				e.printStackTrace();
				if(cn != null)
				{
					System.out.println("[alwaysSupport] Old Connection still activated");
					
					try {
						cn.close();
						System.out.println("[alwaysSupport] Old connection that was still activated has been successfully closed");
					} catch (SQLException e2) {
						System.out.println("[alwaysSupport] Failed to close old connection that was still activated");
						e2.printStackTrace();
					}
				
				}
				return;
			}
			
			try {
				stmt = cn.createStatement();
				
				stmt.executeQuery("SELECT * FROM alwaysSupport");
			} catch (SQLException e) {
				System.out.println("[alwaysSupport] SQL table doesn't exist; trying to create new table.");
				
				try {
					stmt.executeUpdate("CREATE TABLE alwaysSupport (ID INT, sender CHAR(20), message CHAR(50), supportX DOUBLE, supportY DOUBLE, supportZ DOUBLE, supporter CHAR(20), answer CHAR(50), PRIMARY KEY (ID))");
				} catch (SQLException e2) {
					System.out.println("[alwaysSupport] could not be enabled: Failed to create SQL table");
					e2.printStackTrace();
					return;
				}
			}
		}
		
		registerHooks();

		System.out.println("[alwaysSupport] successfully enabled!");
		
	}

	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		boolean succeed = false;
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			if(Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")){
			    PermissionManager permissions = PermissionsEx.getPermissionManager();

			    // Permission check
			    if(permissions.has(player, "alwaysSupport.getsupport")){
			    	
			    	if (cmd.getName().equalsIgnoreCase("getsupport")) {
			    		if (args.length <= 30) {		//alles OK
			    			//in SQL speichern
			    			
			    		}
				
			    		else {
			    			player.sendMessage(ChatColor.GRAY + "[alwaysSupport]" + ChatColor.RED + "Dieser Befehl darf nur 30 Wörter enthalten!");
			    			succeed = false;
			    		}
			    	}
			    } else {
			    	player.sendMessage(ChatColor.GRAY + "[alwaysSupport]" + ChatColor.RED + "Houston, Wir haben ein Problem :) Diesen Befehl darfst Du nicht verwenden!");
			    	succeed = false;
			    	return false;

			    }
			} else {		//kein PermissionsEx
			   Logger.getLogger("Minecraft").warning("PermissionsEx plugin nicht gefunden!");
			}
			
			
			
		}
		
		succeed = true;
		return succeed;
		
	}
	
    public void registerHooks()
    {              
            PluginManager pm = this.getServer().getPluginManager();
           
            pm.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Priority.Highest, this);
            pm.registerEvent(Event.Type.PLAYER_CHAT, playerListener, Priority.Highest, this);
            
    }
	
    void createConfig()
    {
    	this.getConfig().set("dbPath", "unknown");
		this.getConfig().set("dbUser", "unknown");
		this.getConfig().set("dbPassword", "unknown");
		this.saveConfig();
		System.out.println("[alwaysSupport] Config file created!");
    }
    }
