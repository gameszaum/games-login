package com.gameszaum.login.spigot.command;

import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.command.builder.CommandBuilder;
import com.gameszaum.login.spigot.command.builder.base.CommandBase;
import com.gameszaum.login.spigot.command.helper.CommandHelper;
import com.gameszaum.login.spigot.event.BypassLoginEvent;
import com.gameszaum.login.spigot.manager.AccountManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
    @author gameszaum
 */
public class Command {

    private AccountManager accountManager;

    public Command(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    private CommandBuilder createCommand(CommandBase commandBuilder) {
        return commandBuilder;
    }

    public void setupCommands() {
        createCommand(new CommandBase() {
            @Override
            public void handler(CommandSender commandSender, CommandHelper helper, String... args) throws Exception {
                if (args.length != 1) {
                    commandSender.sendMessage("Sintaxe incorreta, use §e/bypass <name>§f.");
                    return;
                }
                String targetName = args[0];

                commandSender.sendMessage("§aVocê forçou o login de §f" + targetName + "§a.");
                accountManager.getAccount(targetName).getAccountDao().bypassLogin();
            }
        }).onlyPermission("login.bypass").setCommand("bypass");

        createCommand(new CommandBase() {
            @Override
            public void handler(CommandSender commandSender, CommandHelper helper, String... args) throws Exception {
                Player player = helper.getPlayer(commandSender);
                Account account = accountManager.getAccount(player.getName());
                String password = args[0];

                if (password.length() < 5) {
                    player.sendMessage("§cA sua senha precisa ter no mínimo 5 caracteres...");
                    return;
                }
                if (account.isRegistered()) {
                    player.sendMessage("§cVocê já está registrado, use §e/login <senha>§c.");
                    return;
                }
                player.sendMessage("§aVocê se registrou.");
                account.setPass(password);

                Bukkit.getPlugin().getServer().getPluginManager().callEvent(new BypassLoginEvent(player));
            }
        }).onlyPlayer().runAsync().setCommand("register", "registrar");

        createCommand(new CommandBase() {
            @Override
            public void handler(CommandSender commandSender, CommandHelper helper, String... args) throws Exception {
                Player player = helper.getPlayer(commandSender);
                Account account = accountManager.getAccount(player.getName());
                String password = args[0];

                if (!account.isRegistered()) {
                    player.sendMessage("§cVocê não está registrado, use §e/register <senha>§c.");
                    return;
                }
                if (account.getPass().equals(password)) {
                    player.sendMessage("§aVocê se logou.");
                    Bukkit.getPlugin().getServer().getPluginManager().callEvent(new BypassLoginEvent(player));
                }
            }
        }).onlyPlayer().runAsync().setCommand("login", "logar");
    }

}
