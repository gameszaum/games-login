package com.gameszaum.login.spigot.listener.player;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PassCheckEvent extends PlayerEvent {

    @Getter
    private static HandlerList handlerList = new HandlerList();

    public PassCheckEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
