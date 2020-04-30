package com.gameszaum.login.bungee;

import com.gameszaum.login.bungee.listener.LoginListener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/*
    @author gameszaum
 */
public class Bungee extends Plugin {

    private static Bungee instance;
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        generateConfig();
        getProxy().getPluginManager().registerListener(this, new LoginListener());

        System.out.println("[GamesLogin] Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getProxy().getPluginManager().unregisterListeners(this);
    }

    private void generateConfig() {
        if (!getDataFolder().exists())
            getDataFolder().mkdirs();

        File file = new File(getDataFolder(), "config.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bungee getInstance() {
        return instance;
    }

    public Configuration getConfig() {
        return config;
    }
}
