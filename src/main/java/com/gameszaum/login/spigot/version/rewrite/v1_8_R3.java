package com.gameszaum.login.spigot.version.rewrite;

import com.gameszaum.login.core.util.Util;
import com.gameszaum.login.spigot.api.reflection.Reflection;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author netindev
 */
public class v1_8_R3 extends LoginListener {

    private static final MinecraftServer SERVER = MinecraftServer.getServer();

    public v1_8_R3(NetworkManager networkManager, String player) {
        super(v1_8_R3.SERVER, networkManager);
        if (this.networkManager == null) {
            Util.info("/ Conexao com o player " + player + " foi cancelada.");
            return;
        }
        Reflection.setField("m", this, this.networkManager, 0);
        Reflection.setField("i",
                new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + player).getBytes(StandardCharsets.UTF_8)),
                        player),
                this, 1);
    }

    @Override
    public void b() {
        this.c();
    }

    @Override
    public void c() {
        GameProfile validProfile = (GameProfile) Reflection.getField("i", this, 1);
        EntityPlayer attemptLogin = v1_8_R3.SERVER.getPlayerList().attemptLogin(this, validProfile, this.hostname);
        if (attemptLogin != null) {
            this.networkManager.handle(new PacketLoginOutSuccess(validProfile));
            v1_8_R3.SERVER.getPlayerList().a(this.networkManager,
                    v1_8_R3.SERVER.getPlayerList().processLogin(validProfile, attemptLogin));
        }
    }
}