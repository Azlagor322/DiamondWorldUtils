package com.azlagor.diamondworldutils;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.FileWriter;
import java.io.IOException;

public class DiamondWorldUtils implements ModInitializer {
    public static KeyBinding upBossBtn = KeyBindingHelper.registerKeyBinding(new KeyBinding("Тп к первому боссу", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_UP, "Poofistiki"));
    public static KeyBinding downBossBtn = KeyBindingHelper.registerKeyBinding(new KeyBinding("Тп к последнему боссу", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_PAGE_DOWN, "Poofistiki"));
    public static String location = "Спавн";
    public static String locationLvl = "0";
    public static void log(String text)
    {
        try(FileWriter writer = new FileWriter("DWutils_log.txt", true))
        {
            writer.write(text + '\n');
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    private void log(String text, boolean append)
    {
        try(FileWriter writer = new FileWriter("DWutils_log.txt", append))
        {
            writer.write(text + '\n');
            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void onInitialize() {
        log("DWutils inits",false);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (upBossBtn.isPressed())
            {
                MinecraftClient.getInstance().getNetworkHandler().sendCommand("boss " + BossDataWorker.upBoss.replace("§a",""));
            }
            if (downBossBtn.isPressed())
            {
                MinecraftClient.getInstance().getNetworkHandler().sendCommand("boss " + BossDataWorker.downBoss.replace("§a",""));
            }
        });

        BossDataWorker.init();
        try {
            DiscordWebSocketClient.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
