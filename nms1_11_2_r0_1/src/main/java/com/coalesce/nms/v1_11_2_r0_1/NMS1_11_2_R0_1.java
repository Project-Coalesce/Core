package com.coalesce.nms.v1_11_2_r0_1;

import com.coalesce.api.NMSCore;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class NMS1_11_2_R0_1 extends NMSCore {
    private Field footerHeaderField_b;

    public NMS1_11_2_R0_1() {
        try {
            footerHeaderField_b = PacketPlayOutPlayerListHeaderFooter.class.getDeclaredField("b");
            footerHeaderField_b.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            footerHeaderField_b = null;
        }
    }

    @Override public boolean sendActionBar(Player player, String message) {
        CraftPlayer cp = (CraftPlayer) player;
        cp.getHandle().playerConnection.sendPacket(
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"))
        );
        return true;
    }

    @Override public boolean sendTabTitles(Player player, String title, String footer) {
        CraftPlayer cp = (CraftPlayer) player;
        PlayerConnection conn = cp.getHandle().playerConnection;
        conn.sendPacket(
                new PacketPlayOutPlayerListHeaderFooter(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + title + "\"}"))
        );
        try {
            PacketPlayOutPlayerListHeaderFooter footerPacket = new PacketPlayOutPlayerListHeaderFooter();
            footerHeaderField_b.set(footerPacket, IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}"));
            conn.sendPacket(footerPacket);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public boolean sendJson(Player player, String json) {
        CraftPlayer cp = (CraftPlayer) player;
        cp.getHandle().playerConnection.sendPacket(
                new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(json))
        );
        return true;
    }

    @Override public double getPing(Player player) {
        CraftPlayer cp = (CraftPlayer) player;
        return cp.getHandle().ping;
    }
}
