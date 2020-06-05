package com.gameszaum.login.spigot.listener.server;

import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.manager.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.List;

public class BlockActionsListener implements Listener {

    private AccountManager accountManager;

    public BlockActionsListener() {
        accountManager = Bukkit.getAccountManager();
    }

    @EventHandler
    void blockPlace(BlockPlaceEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged()) {
            event.getPlayer().sendMessage("§cVocê não pode colocar blocos antes de se logar/registrar.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void blockBreak(BlockBreakEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged()) {
            event.getPlayer().sendMessage("§cVocê não pode quebrar blocos antes de se logar/registrar.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void playerMove(PlayerMoveEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged())
            event.setCancelled(true);
    }

    @EventHandler
    void playerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!accountManager.getAccount(player.getName()).isLogged()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void playerProcessCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!accountManager.getAccount(player.getName()).isLogged()) {
            List<String> cmds = Arrays.asList("register", "registrar", "login", "logar");

            if (cmds.stream().noneMatch(s -> event.getMessage().startsWith(s) || event.getMessage().contains(s) || event.getMessage().equalsIgnoreCase(s))) {
                event.setCancelled(true);
                player.sendMessage("§cVocê não pode usar comandos antes de se logar/registrar.");
            }
        }
    }

    @EventHandler
    void foodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (!accountManager.getAccount(player.getName()).isLogged()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void playerChat(AsyncPlayerChatEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged()) {
            event.getPlayer().sendMessage("§cVocê não pode falar no chat antes de se logar/registrar.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void playerInteract(PlayerInteractEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged()) {
            event.getPlayer().sendMessage("§cVocê não pode interagir antes de se logar/registrar.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    void chat(AsyncPlayerChatEvent event) {
        if (!accountManager.getAccount(event.getPlayer().getName()).isLogged()) {
            if (!event.getMessage().startsWith("/")) {
                event.getPlayer().sendMessage("§cVocê não pode falar no chat antes de se logar/registrar.");
                event.setCancelled(true);
            }
        }
    }

}
