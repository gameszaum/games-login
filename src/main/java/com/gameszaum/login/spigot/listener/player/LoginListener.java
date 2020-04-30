package com.gameszaum.login.spigot.listener.player;

import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.api.item.ItemBuilder;
import com.gameszaum.login.spigot.api.title.ActionBarAPI;
import com.gameszaum.login.spigot.event.AskCaptchaEvent;
import com.gameszaum.login.spigot.event.AskLoginEvent;
import com.gameszaum.login.spigot.event.BypassLoginEvent;
import com.gameszaum.login.spigot.manager.AccountManager;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class LoginListener implements Listener {

    private AccountManager accountManager;
    private String defaultServer;

    public LoginListener() {
        accountManager = Bukkit.getAccountManager();
        defaultServer = Bukkit.getPlugin().getConfig().getString("default-server");
    }

    @EventHandler
    void playerBypass(BypassLoginEvent event) {
        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player.getName());

        account.setCaptcha(false);
        account.setLogged(true);

        if (defaultServer != null && !defaultServer.isEmpty()) {
            player.sendMessage("§aConectando...");
            sendToLobby(player);
        }
    }

    @EventHandler
    void playerAskLogin(AskLoginEvent event) {
        Player player = event.getPlayer();
        Account account = accountManager.getAccount(player.getName());

        Bukkit.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(Bukkit.getPlugin(), () -> {
            if (!account.isLogged()) {
                ActionBarAPI.send(player, (account.isRegistered() ? "Logue-se usando §e/login <senha>§f." : "Registre-se usando §e/register <senha>§f."));
            }
        }, 0, 20L);
    }

    @EventHandler
    void playerCaptcha(AskCaptchaEvent event) {
        buildCaptcha(event.getPlayer());
    }

    @EventHandler
    void inventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();

        if (inventory.getTitle().equals("Validação de captcha")) {
            if (event.getWhoClicked() instanceof Player) {
                if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || event.getCurrentItem().getTypeId() == 0)
                    return;

                Player player = (Player) event.getWhoClicked();
                Account account = accountManager.getAccount(player.getName());

                for (ItemStack content : inventory.getContents()) {
                    if (content != null && content.getType() == event.getCurrentItem().getType()) {
                        account.setCaptcha(false);
                        player.sendMessage("§aO captcha foi validado com êxito.");
                        player.closeInventory();

                        Bukkit.getPlugin().getServer().getPluginManager().callEvent(new AskLoginEvent(player));
                    }
                }
            }
        }
    }

    @EventHandler
    void inventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() instanceof Player) {
            HumanEntity player = event.getPlayer();

            if (accountManager.getAccount(player.getName()).isCaptcha())
                buildCaptcha((Player) player);
        }
    }

    private void buildCaptcha(Player player) {
        List<Material> materials = Arrays.asList(Material.DIAMOND, Material.IRON_INGOT, Material.EMERALD, Material.GOLD_INGOT);
        Random random = new Random();
        Inventory inventory = Bukkit.getPlugin().getServer().createInventory(player, 27, "Validação de captcha");

        inventory.setItem(random.nextInt(inventory.getSize()), new ItemBuilder().create(materials.get(random.nextInt(materials.size()))).display("§aClique aqui").build());
        player.openInventory(inventory);
    }

    private void sendToLobby(Player player) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);

        try {
            out.writeUTF("Connect");
            out.writeUTF(defaultServer);

            player.sendPluginMessage(Bukkit.getPlugin(), "BungeeCord", b.toByteArray());
        } catch (IOException e) {
            player.kickPlayer("§cImpossível validar sessão, tente novamente...");
        }
    }

}
