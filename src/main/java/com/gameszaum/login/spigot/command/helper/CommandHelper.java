package com.gameszaum.login.spigot.command.helper;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface CommandHelper {

    boolean isInteger(String s);

    boolean isPlayer(CommandSender sender);

    Player getPlayer(CommandSender sender);

    String getMsg(String msg);

}