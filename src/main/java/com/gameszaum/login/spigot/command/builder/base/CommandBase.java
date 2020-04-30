package com.gameszaum.login.spigot.command.builder.base;

import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.command.builder.CommandBuilder;
import com.gameszaum.login.spigot.command.helper.impl.CommandHelperImpl;
import com.gameszaum.login.spigot.command.register.CommandRegister;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class CommandBase implements CommandExecutor, CommandBuilder {

    private boolean async, onlyPlayer;
    private String perm;
    private String[] alias;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (onlyPlayer && !(commandSender instanceof Player)) {
            commandSender.sendMessage("§cOnly for players.");
            return false;
        }
        if (perm != null && !(commandSender.hasPermission(perm))) {
            commandSender.sendMessage("§cYou do not have permission to execute this command.");
            return false;
        }

        if (async) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        handler(commandSender, new CommandHelperImpl(), args);
                    } catch (Exception e) {
                        commandSender.sendMessage("§cAn error has occurred to execute this command §e/" + alias[0] + "§c.");
                    }
                }
            }.runTaskAsynchronously(Bukkit.getPlugin());
        } else {
            try {
                handler(commandSender, new CommandHelperImpl(), args);
            } catch (Exception e) {
                commandSender.sendMessage("§cAn error has occurred to execute this command §e/" + alias[0] + "§c.");
            }
        }
        return false;
    }

    @Override
    public void setCommand(String... alias) {
        this.alias = alias;
        CommandRegister.registerCommand(Bukkit.getPlugin(), this, alias);
    }

    @Override
    public CommandBuilder runAsync() {
        this.async = true;
        return this;
    }

    @Override
    public CommandBuilder onlyPermission(String perm) {
        this.perm = perm;
        return this;
    }

    @Override
    public CommandBuilder onlyPlayer() {
        this.onlyPlayer = true;
        return this;
    }
}