package com.gameszaum.login.spigot.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class BypassLoginEvent extends PlayerEvent {

    private static HandlerList handlerList = new HandlerList();

    public BypassLoginEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
