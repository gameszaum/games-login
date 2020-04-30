package com.gameszaum.login.bungee.listener;

import com.gameszaum.login.bungee.Bungee;
import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.exception.InvalidCheckException;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
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

    @EventHandler
    public void onLogin(LoginEvent event) {
        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(event.getConnection().getUniqueId());
        Configuration config = Bungee.getInstance().getConfig();

        if (event.getConnection().isOnlineMode()) {
            if (config.getString("default-server") != null && !config.getString("default-server").isEmpty()) {
                ServerInfo info = BungeeCord.getInstance().getServerInfo(config.getString("default-server").replaceAll(" ", ""));

                if (info != null && info.getMotd() != null) {
                    player.connect(info);
                    //return;
                }
            }
        }
        /*if (config.getString("login-server") != null && !config.getString("login-server").isEmpty()) {
            ServerInfo info = BungeeCord.getInstance().getServerInfo(config.getString("login-server").replaceAll(" ", ""));

            if (info != null && info.getMotd() != null)
                player.connect(info);
        }*/
    }

}
