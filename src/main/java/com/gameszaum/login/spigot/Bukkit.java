package com.gameszaum.login.spigot;

import com.gameszaum.login.core.database.DatabaseCredentials;
import com.gameszaum.login.core.database.mysql.MySQLBase;
import com.gameszaum.login.core.database.mysql.builder.MySQLBuilder;
import com.gameszaum.login.core.util.Util;
import com.gameszaum.login.spigot.api.config.ConfigAPI;
import com.gameszaum.login.spigot.command.Command;
import com.gameszaum.login.spigot.listener.player.CheckListener;
import com.gameszaum.login.spigot.listener.player.LoginListener;
import com.gameszaum.login.spigot.listener.server.BlockActionsListener;
import com.gameszaum.login.spigot.manager.AccountManager;
import com.gameszaum.login.spigot.protocol.LoginReceiver;
import com.gameszaum.login.spigot.version.Version;
import org.bukkit.plugin.java.JavaPlugin;

/*
    @author gameszaum
 */
public class Bukkit extends JavaPlugin {

    private static Bukkit plugin;
    private static AccountManager accountManager;
    private static MySQLBase mySQL;
    private static ConfigAPI accounts;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        generateFiles();

        if (!this.getServer().getOnlineMode()) {
            Util.setOnlineMode(true);
        }
        if (Version.getPackageVersion() == null) {
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        new LoginReceiver();

        accountManager = new AccountManager();

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        getServer().getPluginManager().registerEvents(new CheckListener(), this);
        getServer().getPluginManager().registerEvents(new LoginListener(), this);
        getServer().getPluginManager().registerEvents(new BlockActionsListener(), this);

        new Command(accountManager).setupCommands();

        System.out.println(" ");
        System.out.println("[GamesLogin] Plugin enabled.");
        System.out.println(" ");
    }

    @Override
    public void onDisable() {
        if (mySQL != null)
            mySQL.closeConnection();
    }

    private void generateFiles() {
        if (getConfig().getBoolean("use-mysql")) {
            mySQL = new MySQLBuilder().createConnection(new DatabaseCredentials(getConfig().getString("mysql.host"),
                    getConfig().getString("mysql.db"), getConfig().getString("mysql.user"), getConfig().getString("mysql.pass"),
                    getConfig().getInt("mysql.port")));
            mySQL.executeQuery("CREATE TABLE IF NOT EXISTS `logins` (`name` VARCHAR(16), `pass` VARCHAR(100));");
        } else {
            accounts = new ConfigAPI("accounts", this);
        }
    }

    public static Bukkit getPlugin() {
        return plugin;
    }

    public static AccountManager getAccountManager() {
        return accountManager;
    }

    public static MySQLBase getMySQL() {
        return mySQL;
    }

    public static ConfigAPI getAccountsFile() {
        return accounts;
    }
}
