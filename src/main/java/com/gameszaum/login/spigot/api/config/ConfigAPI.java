package com.gameszaum.login.spigot.api.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/*
    @author gameszaum
 */
public class ConfigAPI implements Serializable {

    private static final long serialVersionUID = 1L;
    private File arch;
    private FileConfiguration config;

    public ConfigAPI(String pathname, Plugin plugin) {
        try {
            File pluginDir = new File(plugin.getDataFolder(), pathname + ".yml");
            if (!plugin.getDataFolder().exists()) {
                plugin.getDataFolder().mkdirs();
            }
            if (!pluginDir.exists()) {
                pluginDir.createNewFile();
            }
            arch = pluginDir;
            config = YamlConfiguration.loadConfiguration(arch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean contains(String c) {
        return config.contains(c);
    }

    public void put(String key, Object value) {
        config.set(key, value);
    }

    public Object get(String key) {
        return config.get(key);
    }

    public int getInt(String key) {
        return config.getInt(key);
    }

    public boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public String getString(String key) {
        return config.getString(key);
    }

    public float getFloat(String key) {
        return (float) config.getDouble(key);
    }

    public double getDouble(String key) {
        return config.getDouble(key);
    }

    public FileConfiguration reload() {
        return config = YamlConfiguration.loadConfiguration(arch);
    }

    public ConfigurationSection getConfigurationSection(String key) {
        return config.getConfigurationSection(key);
    }

    public void save() {
        try {
            config.save(arch);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}