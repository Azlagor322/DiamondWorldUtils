package com.azlagor.diamondworldutils.mixin;


import com.azlagor.diamondworldutils.BossDataWorker;
import com.azlagor.diamondworldutils.DiscordWebSocketClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;


@Mixin(ClientPlayNetworkHandler.class)
public class SendChatMessage {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String message , CallbackInfo info) throws IOException {
        if(message.startsWith(".")) info.cancel();
        if (message.startsWith(".ts"))
        {
            DiscordWebSocketClient.getMessage();
        }
        if (message.startsWith(".d"))
        {
            String name = message.replace(".d ","");
            BossDataWorker.bossTimes.put(name, 9999999999999l);
            BossDataWorker.clanFightMap.remove(name);
            DiscordWebSocketClient.checkDataClient(name);
        }
        if (message.startsWith(".c"))
        {
            String name = message.replace(".c ","");
            BossDataWorker.bossTimes.remove(name);
            BossDataWorker.clanFightMap.remove(name);
        }
    }
}