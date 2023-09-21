package com.gameszaum.login.spigot.listener.player;

import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.exception.InvalidCheckException;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.account.dao.AccountDao;
import com.gameszaum.login.spigot.manager.AccountManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CheckListener implements Listener {

    private final AccountManager accountManager;

    public CheckListener() {
        accountManager = com.gameszaum.login.spigot.Bukkit.getAccountManager();
    }

    @EventHandler
    void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (accountManager.getAccount(event.getName()) != null) {
            accountManager.removeAccount(event.getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player.getName());

        if (account.isPremium()) {
            Bukkit.getScheduler().scheduleAsyncDelayedTask(com.gameszaum.login.spigot.Bukkit.getPlugin(), () -> account.getAccountDao().bypassLogin(), 15L);
        } else {
            player.sendMessage("§cAutenticado como jogador pirata.");

            Bukkit.getScheduler().scheduleAsyncDelayedTask(com.gameszaum.login.spigot.Bukkit.getPlugin(), () -> account.getAccountDao().requestLogin(), 15L);
        }
        Bukkit.getOnlinePlayers().forEach(o -> {
            o.hidePlayer(player);
            player.hidePlayer(o);
        });
    }

    @EventHandler
    void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        accountManager.getAccount(event.getPlayer().getName()).getAccountDao().update();
    }

}
