package com.gameszaum.login.spigot.account.dao;

import com.gameszaum.login.core.database.mysql.MySQLBase;
import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.api.config.ConfigAPI;
import com.gameszaum.login.spigot.event.AskCaptchaEvent;
import com.gameszaum.login.spigot.event.BypassLoginEvent;
import com.gameszaum.login.spigot.manager.AccountManager;

public class AccountDao {

    private final Account account;
    private final ConfigAPI file;
    private final MySQLBase mySQL;
    private final AccountManager accountManager;

    public AccountDao(String name) {
        this.account = new Account();
        this.file = Bukkit.getAccountsFile();
        this.mySQL = Bukkit.getMySQL();
        this.accountManager = Bukkit.getAccountManager();

        account.setAccountDao(this);
        account.setName(name);
        account.setCaptcha(true);
    }

    public Account create(boolean premium) {
        account.setPremium(premium);
        accountManager.addAccount(account);

        if (!premium) {
            if (mySQL != null) {
                if (!mySQL.contains("logins", "name", account.getName())) {
                    mySQL.executeQuery("INSERT INTO `logins` (`name`, `pass`) VALUES ('" + account.getName() + "', 'null');");
                }
            } else {
                file.put("accounts." + account.getName() + ".pass", null);
                file.save();
            }
        }
        return load(premium);
    }

    private Account load(boolean premium) {
        if (!premium) {
            if (mySQL != null) {
                if (mySQL.contains("logins", "name", account.getName())) {
                    account.setPass(mySQL.getString("logins", "name", account.getName(), "pass"));
                }
            } else {
                file.getConfigurationSection("accounts").getKeys(false).forEach(s -> {
                    if (s.equalsIgnoreCase(account.getName())) {
                        account.setPass(file.getString("accounts." + s + ".pass"));
                    }
                });
            }
            account.setRegistered(account.getPass() != null && !account.getPass().equals("null"));
        }
        return account;
    }

    public void update() {
        if (!account.isPremium()) {
            if (mySQL != null) {
                mySQL.update("logins", "name", account.getName(), "pass", account.getPass());
            } else {
                file.put("accounts." + account.getName() + ".pass", account.getPass());
                file.save();
            }
        }
    }

    public void bypassLogin() {
        Bukkit.getPlugin().getServer().getPluginManager().callEvent(new BypassLoginEvent(org.bukkit.Bukkit.getPlayer(account.getName())));
    }

    public void requestLogin() {
        Bukkit.getPlugin().getServer().getPluginManager().callEvent(new AskCaptchaEvent(org.bukkit.Bukkit.getPlayer(account.getName())));
    }

}
