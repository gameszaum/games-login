package com.gameszaum.login.spigot.protocol;

import com.comphenix.tinyprotocol.NMSReflection;
import com.comphenix.tinyprotocol.v1_8.TinyProtocol;
import com.gameszaum.login.core.check.Check;
import com.gameszaum.login.core.exception.InvalidCheckException;
import com.gameszaum.login.spigot.Bukkit;
import com.gameszaum.login.spigot.account.Account;
import com.gameszaum.login.spigot.account.dao.AccountDao;
import com.gameszaum.login.spigot.api.reflection.Reflection;
import com.gameszaum.login.spigot.version.Version;
import com.mojang.authlib.GameProfile;
import io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.net.SocketAddress;

public class LoginReceiver extends TinyProtocol {

    private final Version version = Version.getPackageVersion();

    private final NMSReflection.FieldAccessor<GameProfile> profileField = NMSReflection.getField("{nms}.PacketLoginInStart",
            GameProfile.class, 0);

    @Override
    public Object onPacketInAsync(Player receiver, Channel channel, Object packet) {
        if (this.profileField.hasField(packet)) {
            if (this.receiveLogin(packet, channel)) {
                return null;
            }
        }
        return super.onPacketInAsync(receiver, channel, packet);
    }

    private boolean receiveLogin(Object packet, Channel channel) {
        boolean check;
        String name = this.profileField.get(packet).getName();

        try {
            check = Check.fastCheck(name);
        } catch (InvalidCheckException e) {
            e.printStackTrace();
            return false;
        }
        Account account = Bukkit.getAccountManager().getAccount(name);

        if (account != null) {
            account.setPremium(check);
        } else {
            try {
                new AccountDao(name).create(Check.fastCheck(name));
            } catch (InvalidCheckException e) {
                e.printStackTrace();
            }
        }
        if (check) {
            return false;
        }
        try {
            Class<?> loginClass = Class.forName("com.gameszaum.login.spigot.version.rewrite" + this.version.toString());
            loginClass.getConstructors()[0].newInstance(networkList(channel.remoteAddress(),
                            this.version.getServerConnection(), this.version.getNetworkManager()),
                    name);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        return true;
    }

    private Object networkList(SocketAddress socketAddress, String connectionMethod, String networkManager) {
        try {
            Class<?> minecraftServer = Class
                    .forName("net.minecraft.server.v1_8_R3.MinecraftServer");
            Object serverInstance = minecraftServer.getMethod("getServer", (Class<?>[]) null).invoke(minecraftServer,
                    (Object[]) null);
            Method serverConnection = serverInstance.getClass().getMethod(connectionMethod, (Class<?>[]) null);
            Object invokedConnection = serverConnection.invoke(serverInstance, (Object[]) null);
            Iterable<Object> networkList = (Iterable<Object>) Reflection.getField(networkManager, invokedConnection, 0);
            for (final Object nextNetwork : networkList) {
                Object address = nextNetwork.getClass().getMethod("getSocketAddress", (Class<?>[]) null)
                        .invoke(nextNetwork, (Object[]) null);
                if (address.equals(socketAddress)) {
                    return nextNetwork;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}