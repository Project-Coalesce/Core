package com.coalesce.nms.reflection;

import com.coalesce.api.NMSCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NMSReflection extends NMSCore {
    private static final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private Class<?> string = String.class; // bloody annotations, man

    @InitClass(className = "IChatBaseComponent") private Class<?> iChatBaseComponentClass;
    @InitClass(className = "IChatBaseComponent$ChatSerializer", fallback = "ChatSerializer") private Class<?> chatSerializer;
    @InitClass(className = "CraftPlayer", type = ClassType.OBC) private Class<?> craftPlayer;
    @InitClass(className = "EntityPlayer") private Class<?> entityPlayer;
    @InitClass(className = "PlayerConnection") private Class<?> playerConnection;
    @InitClass(className = "Packet") private Class<?> packet;
    @InitClass(className = "PacketPlayOutTitle") private Class<?> packetPlayOutTitle;
    @InitClass(className = "PacketPlayOutTitle$EnumTitleAction") private Class<?> packetPlayOutTitleEnum;
    @InitClass(className = "PacketPlayOutPlayerListHeaderFooter") private Class<?> packetHeaderFooter;
    @InitClass(className = "PacketPlayOutChat") private Class<?> packetPlayOutChat;

    @InitField(name = "b", classOwner = "packetHeaderFooter") private Field headerFooterField_b;
    @InitField(name = "playerConnection", classOwner = "entityPlayer") private Field playerConnectionField;
    @InitField(name = "ping", classOwner = "entityPlayer") private Field pingField;

    @InitMethod(name = "a", args = {"string"}, classOwner = "chatSerializer") private Method chatSerializerA;
    @InitMethod(name = "sendPacket", args = {"packet"}, classOwner = "packetConnection") private Method sendPacket;
    @InitMethod(name = "getHandle", args = {}, classOwner = "craftPlayer") private Method getCraftPlayerHandle;

    @InitConstructor(classOwner = "packetPlayOutChat", args = {"iChatBaseComponentClass"}) private Constructor<?> packetPlayOutChatConstructor;
    @InitConstructor(classOwner = "packetHeaderFooter", args = {"iChatBaseComponentClass"}) private Constructor<?> packetHeaderFooterConstructor;
    @InitConstructor(classOwner = "packetHeaderFooter", args = {}) private Constructor<?> packetHeaderFooterConstructorEmpty;
    @InitConstructor(classOwner = "packetPlayOutTitle", args = {"packetPlayOutTitleEnum", "iChatBaseComponentClass"}) private Constructor<?> titleConstructor;

    public NMSReflection() {
        for (Field f : getClass().getDeclaredFields()) {
            InitClass anno = f.getAnnotation(InitClass.class);
            if (anno == null) {
                continue;
            }
            String field;
            if (anno.type() == ClassType.NMS) {
                field = "net.minecraft.server." + version + '.';
            } else if (anno.type() == ClassType.OBC) {
                field = "org.bukkit.craftbukkit." + version + '.';
            } else {
                field = "";
            }
            try {
                f.set(this, Class.forName(field + anno.className()));
            } catch (ClassNotFoundException | IllegalAccessException e) {
                try {
                    f.set(this, Class.forName(field + anno.fallback()));
                } catch (ClassNotFoundException | IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        try {
            for (Field f : getClass().getDeclaredFields()) {
                if (f.isAnnotationPresent(InitField.class)) {
                    InitField anno = f.getAnnotation(InitField.class);
                    String name = anno.name();
                    Field owner = getClass().getDeclaredField(anno.classOwner());
                    if (owner.getType() != Class.class) {
                        continue;
                    }
                    Field toSet = ((Class<?>) owner.get(this)).getDeclaredField(name);
                    toSet.setAccessible(true);
                    f.set(this, toSet);
                } else if (f.isAnnotationPresent(InitMethod.class)) {
                    InitMethod anno = f.getAnnotation(InitMethod.class);
                    String name = anno.name();
                    Field owner = getClass().getDeclaredField(anno.classOwner());
                    if (owner.getType() != Class.class) {
                        continue;
                    }
                    Class<?>[] arguments = new Class<?>[anno.args().length];
                    for (String n : anno.args()) {
                        arguments[arguments.length] = (Class<?>) getClass().getDeclaredField(n).get(this);
                    }
                    Method toSet = ((Class<?>) owner.get(this)).getDeclaredMethod(name, arguments);
                    toSet.setAccessible(true);
                    f.set(this, toSet);
                } else if (f.isAnnotationPresent(InitConstructor.class)) {
                    InitConstructor anno = f.getAnnotation(InitConstructor.class);
                    Field owner = getClass().getDeclaredField(anno.classOwner());
                    if (owner.getType() != Class.class) {
                        continue;
                    }
                    Class<?>[] arguments = new Class<?>[anno.args().length];
                    for (String n : anno.args()) {
                        arguments[arguments.length] = (Class<?>) getClass().getDeclaredField(n).get(this);
                    }
                    Constructor<?> toSet = ((Class<?>) owner.get(this)).getDeclaredConstructor(arguments);
                    toSet.setAccessible(true);
                    f.set(this, toSet);
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override public boolean sendActionBar(Player player, String message) {
        try {
            Object cp = craftPlayer.cast(player);
            Object ep = getCraftPlayerHandle.invoke(cp);
            Object conn = playerConnectionField.get(ep);
            sendPacket.invoke(conn,
                    titleConstructor.newInstance(packetPlayOutTitleEnum.getEnumConstants()[2], chatSerializerA.invoke(null, "{\"text\":\"" + message + "\"}"))
            );
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override public boolean sendTabTitles(Player player, String title, String footer) {
        try {
            Object cp = craftPlayer.cast(player);
            Object ep = getCraftPlayerHandle.invoke(cp);
            Object conn = playerConnectionField.get(ep);
            sendPacket.invoke(conn,
                    packetHeaderFooterConstructor.newInstance(chatSerializerA.invoke(null, "{\"text\":\"" + title + "\"}"))
            );
            Object packet = packetHeaderFooterConstructorEmpty.newInstance();
            headerFooterField_b.set(packet, chatSerializerA.invoke(null, "{\"text\":\"" + footer + "\"}"));
            sendPacket.invoke(conn, packet);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override public boolean sendJson(Player player, String json) {
        try {
            Object cp = craftPlayer.cast(player);
            Object ep = getCraftPlayerHandle.invoke(cp);
            Object conn = playerConnectionField.get(ep);
            sendPacket.invoke(conn, chatSerializerA.invoke(json));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override public double getPing(Player player) {
        try {
            Object cp = craftPlayer.cast(player);
            Object ep = getCraftPlayerHandle.invoke(cp);
            return (double) pingField.get(ep);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private enum ClassType {
        NMS("net.minecraft.server."), OBC("org.bukkit.craftbukkit.");

        private final String packageName;

        ClassType(String packageName) {
            this.packageName = packageName;
        }

        public Class<?> from(String name) {
            try {
                return Class.forName(packageName + version + '.' + name);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface InitClass {
        ClassType type() default ClassType.NMS;

        String className();

        String fallback() default "";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface InitField {
        String name();

        String classOwner(); // Must be declared within the class as a field.
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface InitMethod {
        String name();

        String[] args();

        String classOwner(); // Must be declared within the class as a field.
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface InitConstructor {
        String[] args();

        String classOwner(); // Must be declared within the class as a field.
    }
}
