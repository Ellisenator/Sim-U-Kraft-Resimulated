package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.ModeChangePacket;
import com.resimulators.simukraft.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.awt.*;
import java.io.IOException;

public class GuiStart extends GuiScreen {
    private GuiButton button1;
    private GuiButton button2;
    private GuiButton button3;
    private GuiButton button4;
    private int Gamemode = -2;
    private boolean isDedicated;
    private int serverMode = -1;
    private int button_width = 100;
    private int button_height = 20;


    public void SetModeFactors(boolean isdedicated, int mode) {
        this.isDedicated = isdedicated;
        serverMode = mode;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawString(fontRenderer, "Welcome to Sim-u-Kraft", width / 2 - button_width / 2, height / 3 - 50, Color.WHITE.getRGB());
        if (serverMode == -1) {
            drawCenteredString(fontRenderer, "Please choose a mode to play in:", width / 2, height / 3 - 30, Color.WHITE.getRGB());
        } else if (isDedicated) {
            drawCenteredString(fontRenderer, "Press button to activate mod, you are playing on a dedicated", width / 2, height / 3 - 20, Color.WHITE.getRGB());
            drawCenteredString(fontRenderer, "server because of this the server chooses the mode you play in ", width / 2, height / 3, Color.WHITE.getRGB());
        } else if (mc.getIntegratedServer() == null && serverMode != -1){
            drawCenteredString(fontRenderer, "the mode has already been chosen for the server", width / 2, height / 3 - 30, Color.WHITE.getRGB());
        }


        if (buttonList.contains(button1)) {
            int buttonx = button1.x;
            int buttony = button1.y;
            String desc;
            desc = "Have to supply most resources. " + "Money needed to build and hire sims etc.";
            if (buttonx <= mouseX && mouseX <= (buttonx + button_width)) {
                if (buttony <= mouseY && mouseY <= (buttony + button_height)) {
                    drawHoveringText(desc, buttonx - fontRenderer.getStringWidth(desc) / 3, buttony - 10);
                }
            }
            buttonx = button3.x;
            buttony = button3.y;
            desc = "Have to supply no resources. " + "Everything is Free. Let your imagination run wild";
            if (buttonx <= mouseX && mouseX <= (buttonx + button_width)) {
                if (buttony <= mouseY && mouseY <= (buttony + button_height)) {
                    drawHoveringText(desc, buttonx - fontRenderer.getStringWidth(desc) / 4, buttony - 10);
                }

            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        if (mc.getIntegratedServer() != null || serverMode == -1) {

            buttonList.add(button1 = new GuiButton(1, width / 2 - button_width / 2, height / 3 - 10, button_width, button_height, "Survival"));
            buttonList.add(button3 = new GuiButton(3, width / 2 - button_width / 2, height / 3 + 40, button_width, button_height, "Creative"));

        }else buttonList.add(button4 = new GuiButton(4, width / 2 - button_width / 2, height / 2 - button_height / 2, button_width, button_height, "Activate"));

        super.initGui();
    }



    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                Gamemode = 0;

            case 3:

                    if (Gamemode == -2)
                    Gamemode = 1;
            case 4: {
                if (serverMode != -1 && isDedicated&& Gamemode == -2) {
                    if (Gamemode == -1) Gamemode = serverMode;
                }
            }
            Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).setmode(Gamemode);
            Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).setenabled(true);
        PacketHandler.INSTANCE.sendToServer(new ModeChangePacket(Minecraft.getMinecraft().player.getUniqueID(),Gamemode));
        Minecraft.getMinecraft().displayGuiScreen(null);
        super.actionPerformed(button);
    }}

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }


}

