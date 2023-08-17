package com.azlagor.diamondworldutils.mixin;


import com.azlagor.diamondworldutils.BossDataWorker;
import com.azlagor.diamondworldutils.DiamondWorldUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(at = @At("RETURN"), method = "render")
    public void render(DrawContext context, float tickDelta, CallbackInfo callbackInfo){
        int width = MinecraftClient.getInstance().getWindow().getScaledWidth() - 100;
        int hiegth = MinecraftClient.getInstance().getWindow().getScaledHeight() - 10;
        context.drawText(MinecraftClient.getInstance().inGameHud.getTextRenderer(),Text.of(DiamondWorldUtils.location),width,hiegth,0xffffff,false);
        //MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(Main.location,width,hiegth,0xffffff,false, new Matrix4f(), new VertexConsumerProvider);
        //MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(new MatrixStack(),Text.of(Main.location),width,hiegth,0xffffff);
        int y = BossDataWorker.y;
        MatrixStack matrix = new MatrixStack();
        matrix.scale(BossDataWorker.scale, BossDataWorker.scale, BossDataWorker.scale);

        Map<String,Long> sortedMap = BossDataWorker.bossTimes.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue,
                LinkedHashMap::new));
        boolean marker = true;
        for(String name : sortedMap.keySet())
        {
            if(BossDataWorker.clanFightMap.containsKey(name)) continue;
            if(marker)
            {
                BossDataWorker.upBoss = BossDataWorker.lvlMap.get(name).toString();
                marker = false;
            }
            long time = BossDataWorker.bossTimes.get(name) - System.currentTimeMillis() / 1000;

            String text = "§7" + name + " §8[§c" + BossDataWorker.lvlMap.get(name) + "§8] §9";
            String timeText = "";
            while (time > 0)
            {
                timeText = time%60 + timeText;
                time /= 60;
                if(time > 0) timeText = ":" + timeText;
            }
            if(timeText == "")
            {
                BossDataWorker.bossTimes.remove(name);
                continue;
            }
            if(timeText.length() > 10)
            {
               timeText = "§4Сломали :`-(";
            }
            text += timeText;
            //MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrix,Text.of(text), BossDataWorker.x, y,0xffffff);
            y += BossDataWorker.step;
        }

        marker = true;
        for(String name : BossDataWorker.unkBoss)
        {
            if(BossDataWorker.clanFightMap.containsKey(name)) continue;
            if(BossDataWorker.bossTimes.containsKey(name)) continue;
            if(marker)
            {
                BossDataWorker.downBoss = BossDataWorker.lvlMap.get(name).toString();
                marker = false;
            }
            String text = "§8" + name + " §8[§c" + BossDataWorker.lvlMap.get(name) + "§8]";
            //MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrix,Text.of(text), BossDataWorker.x, y,0xffffff);
            y += BossDataWorker.step;
        }
        for(String name : BossDataWorker.clanFightMap.keySet())
        {
            String text = "§8" + name + " §8[§c" + BossDataWorker.lvlMap.get(name) + "§8] §4⚔§7" + BossDataWorker.clanFightMap.get(name) + "§4⚔";
            //MinecraftClient.getInstance().inGameHud.getTextRenderer().draw(matrix,Text.of(text), BossDataWorker.x, y,0xffffff);
            y += BossDataWorker.step;
        }
    }

}