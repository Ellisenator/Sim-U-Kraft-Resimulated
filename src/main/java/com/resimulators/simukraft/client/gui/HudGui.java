package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.common.enums.EnumDay;
import com.resimulators.simukraft.common.interfaces.PlayerCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HudGui extends Gui {
    private List<UUID> sim = new ArrayList<>();
    private int population = 1;
    private static float credits;

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void Renderstats(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT)
            return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.gameSettings.showDebugInfo)
            return;
        SaveSimData simData = SaveSimData.get(mc.world);
        if (sim == null)
            return;
        if (simData == null)
            return;
        PlayerCapability playerCap = mc.player.getCapability(ModCapabilities.PlayerCap, null);
        if (playerCap == null)
            return;
        if (!playerCap.getenabled())
            return;
        int mode = playerCap.getmode();
        if (mode == -2)
            return;
        long factionid = playerCap.getfactionid();
        if (simData.getFaction(factionid) == null)
            return;
        List<UUID> sims = simData.getFaction(factionid).getTotalSims();
        int population = sims == null ? 0 : sims.size();
        sims = simData.getFaction(factionid).getUnemployedSims();
        int unemployedsize = sim == null ? 0 : sims.size();
        drawString(mc.fontRenderer, "Population " + population + ", Unemployed sims: " + unemployedsize, 1, 1, Color.WHITE.getRGB());
        drawString(mc.fontRenderer,"Day " + EnumDay.DayStorage.getTotaldays()+ " - " + EnumDay.getDay(EnumDay.DayStorage.getDayInt()).toString(),1,11,Color.WHITE.getRGB());
        if (mode == 0)
            drawString(mc.fontRenderer, "Credits: " + SimEventHandler.getCredits(), 1, 21, Color.WHITE.getRGB());
    }
    }

