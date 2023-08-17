package com.azlagor.diamondworldutils.mixin;

import com.azlagor.diamondworldutils.DiamondWorldUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudTitleMixin {
    @Shadow @Final protected Text subtitle;
    @Shadow @Final protected Text title;
    @Inject(at = @At("RETURN"), method = "render")
    public void render(DrawContext context, float tickDelta, CallbackInfo callbackInfo){
        if(title == null || subtitle == null || title.getString() == null || subtitle.getString() == null) return;
        if(title.getString().toLowerCase().contains("спавн")) DiamondWorldUtils.location = "§6Спавн";
        if(subtitle.getString().toLowerCase().contains("уровень"))
        {
            String start = "";
            String[] data = subtitle.getString().split(" ") ;
            if(title.getString().toLowerCase().contains("шахту")) start += "§8Шахта: §a" + data[1];
            if(title.getString().toLowerCase().contains("боссу")) start += "§4Босс: §a" + data[1];
            DiamondWorldUtils.location = start;
            DiamondWorldUtils.locationLvl = data[1];
        }
    }
}
