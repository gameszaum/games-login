package com.gameszaum.login.spigot.account.dao;

import com.gameszaum.login.core.database.mysql.MySQLBase;
import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.api.config.ConfigAPI;
import com.gameszaum.login.spigot.event.AskCaptchaEvent;
import com.gameszaum.login.spigot.event.BypassLoginEvent;
import com.gameszaum.login.spigot.manager.AccountManager;
import org.bukkit.entity.Player;

public class AccountDao {

    private Account account;
    private Player player;
    private ConfigAPI file;
    private MySQLBase mySQL;
    private AccountManager accountManager;

    public AccountDao(Player player) {
        this.player = player;
        this.account = new Account();
        this.file = Bukkit.getAccountsFile();
        this.mySQL = Bukkit.getMySQL();
        this.accountManager = Bukkit.getAccountManager();

        account.setAccountDao(this);
        account.setName(player.getName());
        account.setCaptcha(true);
    }

    public Account create(boolean premium) {
        account.setPremium(premium);
        accountManager.addAccount(account);

        if (!premium) {
            if (mySQL != null) {
                if (!mySQL.contains("logins", "name", player.getName())) {
                    mySQL.executeQuery("INSERT INTO `logins` (`name`, `pass`) VALUES ('" + player.getName() + "', 'null');");
                }
            } else {
                file.put("accounts." + player.getName() + ".pass", null);
                file.save();
            }
        }
        return load(premium);
    }

    private Account load(boolean premium) {
        if (!premium) {
            if (mySQL != null) {
                if (mySQL.contains("logins", "name", player.getName())) {
                    account.setPass(mySQL.getString("logins", "name", player.getName(), "pass"));
                }
            } else {
                file.getConfigurationSection("accounts").getKeys(false).forEach(s -> {
                    if (s.equalsIgnoreCase(player.getName())) {
                        account.setPass(file.getString("accounts." + s + ".pass"));
                    }
                });
            }
            if (account.getPass() != null && !account.getPass().equals("null")) {
                account.setRegistered(true);
            } else {
                account.setRegistered(false);
            }
        }
        return account;
    }

    public void update() {
        if (!account.isPremium()) {
            if (mySQL != null) {
                mySQL.update("logins", "name", player.getName(), "pass", account.getPass());
            } else {
                file.put("accounts." + player.getName() + ".pass", account.getPass());
                file.save();
            }
        }
    }

    public void bypassLogin() {
        Bukkit.getPlugin().getServer().getPluginManager().callEvent(new BypassLoginEvent(player));
    }

    public void requestLogin() {
        Bukkit.getPlugin().getServer().getPluginManager().callEvent(new AskCaptchaEvent(player));
    }

}
