package com.azlagor.diamondworldutils.mixin;

import com.azlagor.diamondworldutils.BossDataWorker;
import com.azlagor.diamondworldutils.DiamondWorldUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Timer;
import java.util.TimerTask;

@Mixin(MessageHandler.class)
public class onChatMessage {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    public void onGameMessage(Text message, boolean overlay, CallbackInfo info) {
        if(message.toString().contains("Вы нашли коллекционный предмет!"))
        {
            MinecraftClient.getInstance().player.playSound(SoundEvents.ENTITY_CAT_PURR,3,1);
            MinecraftClient.getInstance().player.sendMessage(Text.of("§aВы нашли коллекционный предмет!"));
            MinecraftClient.getInstance().player.sendMessage(Text.of("§aВы нашли коллекционный предмет!"));
            MinecraftClient.getInstance().player.sendMessage(Text.of("§aВы нашли коллекционный предмет!"));
            MinecraftClient.getInstance().player.sendMessage(Text.of("§aВы нашли коллекционный предмет!"));
            MinecraftClient.getInstance().player.sendMessage(Text.of("§aВы нашли коллекционный предмет!"));
            return;
        }
        if(message.getSiblings().size() < 2) return;
        if(message.getSiblings().get(0).getString().equals("В данный момент эту локацию пытается захватить клан "))
        {
            for(String name : BossDataWorker.lvlMap.keySet())
            {

                String lvl = BossDataWorker.lvlMap.get(name).replace("§a","");
                if(lvl.equals(DiamondWorldUtils.locationLvl))
                {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            System.out.println("Локацию " + name + " захватывают!");
                            System.out.println(message.getSiblings().get(1).getStyle().getColor().getHexCode());
                            BossDataWorker.clanFightMap.put(name,message.getSiblings().get(1).getString());
                        }
                    }, 1000);

                }
            }
        }
        if(message.getSiblings().size() < 3) return;
        if(message.getSiblings().get(0).getString().equals("Босс ") && message.getSiblings().get(2).getString().contains("захвачен кланом"))
        {
            BossDataWorker.clanFightMap.remove(message.getSiblings().get(1));
        }
    }
}



