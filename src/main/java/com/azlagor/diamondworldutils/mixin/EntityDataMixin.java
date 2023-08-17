package com.azlagor.diamondworldutils.mixin;


import com.azlagor.diamondworldutils.BossDataWorker;
import com.azlagor.diamondworldutils.DiamondWorldUtils;
import com.azlagor.diamondworldutils.DiscordWebSocketClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(ClientPlayNetworkHandler.class)
public class EntityDataMixin {

    private static String lastBoss = "";

    @Inject(method = "onEntityTrackerUpdate", at = @At("RETURN"))
    private void onEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo info) throws IOException {
        Entity entity = MinecraftClient.getInstance().world.getEntityById(packet.id());
        if(entity == null) return;

        DiamondWorldUtils.log("Entity det");
        if(entity.hasCustomName())
        {
            Text customName = entity.getCustomName();
            if(customName.getSiblings().size() < 1) return;
            if(customName.getSiblings().get(0).getString().equals("Босс "))
            {
                BossDataWorker.nowBossTarget = customName.getSiblings().get(1).getString();
                System.out.println("Таргет на босса: " + customName.getSiblings().get(1).getString());
                return;
            }
            if(customName.getSiblings().size() < 2) return;
            String isTimer = customName.getSiblings().get(1).getString();
            String nowBoss = BossDataWorker.nowBossTarget;
            if(lastBoss.equals(nowBoss)) return;
            if(isTimer.equals(" ч. ") || isTimer.equals(" мин. ") || isTimer.equals(" сек. "))
            {
                if(nowBoss.equals("")) return;
                Integer sec = 0;
                long ut = System.currentTimeMillis();
                ut/=1000;
                for (Text t : customName.getSiblings())
                {
                    try
                    {
                        sec = sec * 60 + Integer.valueOf(t.getString());
                    }
                    catch (NumberFormatException ex){}
                }

                Long endTime = ut + (long)sec;

                if(BossDataWorker.bossTimes.containsKey(nowBoss))
                {
                    BossDataWorker.clanFightMap.remove(nowBoss);
                    System.out.println("getBossTimer: " + nowBoss);
                    Long time = BossDataWorker.bossTimes.get(nowBoss);
                    System.out.println(time);
                    System.out.println(endTime);
                    if(time - endTime > 5 || time - endTime < -5)
                    {
                        BossDataWorker.bossTimes.put(nowBoss, endTime);
                        DiscordWebSocketClient.checkDataClient(nowBoss);
                        lastBoss = nowBoss;
                    }

                }
                else
                {
                    lastBoss = nowBoss;
                    System.out.println("setBossTimer: " + nowBoss);
                    BossDataWorker.bossTimes.put(nowBoss, endTime);
                    BossDataWorker.clanFightMap.remove(nowBoss);
                    DiscordWebSocketClient.checkDataClient(nowBoss);
                    MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_CAT_PURREOW,10,1);
                    MinecraftClient.getInstance().player.sendMessage(Text.of("§a" + BossDataWorker.nowBossTarget + " замечен."),false);

                }

            }

        }

    }
}


