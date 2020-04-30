package com.gameszaum.login.bungee.listener;

import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.exception.InvalidCheckException;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class LoginListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPreLogin(PreLoginEvent event) {
        try {
            event.getConnection().setOnlineMode(Check.fastCheck(event.getConnection().getName()));
        } catch (InvalidCheckException e) {
            event.getConnection().disconnect(TextComponent.fromLegacyText("§cErro ao verificar sua sessão, entre novamente."));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();

        if (player.getPendingConnection().isOnlineMode()) {
            player.sendMessage(TextComponent.fromLegacyText("§aAutenticado como jogador original."));
            return;
        }
    }

}
