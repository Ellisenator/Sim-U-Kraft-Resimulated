package com.resimulators.simukraft.client.gui;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimToHire;
import com.resimulators.simukraft.network.Credits_packets;
import com.resimulators.simukraft.network.Hiring_packet;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import com.resimulators.simukraft.network.PacketHandler;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class GuiMiner extends GuiScreen {
   boolean hiredSim = false;
    float credit;
    private int buttonWidth = 200;
    private int xOffset = 105;
    private int yOffset = 10;
    private int x = 0;
    private String status = "";
    private GuiButton button1;
    private GuiButton button2;
    private List<EntitySim> sims;
    private int mouseX;
    private int mouseY;
    private int buttoni;
    @Override
    public void drawScreen(int mouseX, int mouseY,float partialTicks) {
        drawDefaultBackground();
        if (status != "hiring") {
            drawString(mc.fontRenderer, "Miner", (width / 2) - (buttonWidth / 3), height / 4 - 10, Color.WHITE.getRGB());
        }
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        x = 0;
        sims = SimToHire.unemployedsims;
        buttonList.clear();

        buttonList.add(button2 = new GuiButton(-2, width / 2 - buttonWidth / 2, height - 50, "Cancel"));
        if (!status.equals("hiring")){
        buttonList.add(button1 = new GuiButton(-1, width / 2 - buttonWidth / 2, height - 80, "Hire " + hiredSim));
        if (hiredSim == false){button1.enabled = true;}}
        if (status.equals("hiring")){



            System.out.println(sims.size());
        for (int i = 1; i < sims.size(); i++){
            x++;
            if (((x * xOffset) + 30) > width){
                yOffset += 25;
                x = 1;
            }
            String name = sims.get(i).getName();
            buttonList.add(new GuiButton(i, x * xOffset-75, yOffset + 5,100,20, name + " last name"));
            int bottom_of_screen = height - 70;

            if (yOffset + 10 >= bottom_of_screen || yOffset + 5 <= 0){
                buttoni = i;
                buttonList.get(buttoni).enabled = false;
                buttonList.get(buttoni).visible = false;
                System.out.println("disabling button " + buttoni);
            } else {
                buttonList.get(buttoni).enabled = true;
                buttonList.get(buttoni).visible = true;
            }
            }

            }


        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            //negative numbers here to make it able to check if a sims button has been pressed
            case -1:
                status = "hiring";
                System.out.println("status updated");
                updateButtons(-1);
                break;
            case -2:
                this.mc.displayGuiScreen(null);
                break;
            default:
                updateButtons(button.id);
                disableButton(button.id);



        }
        super.actionPerformed(button);
    }

    private void updateButtons(int id){
        if (status.equals("hiring")){
            button1.visible = false;
            button1.enabled = false;
            System.out.println("hired sim is " + hiredSim);
            initGui();
        if (id > 0){
            EntitySim hired_sim = sims.get(id);
            hired_sim.setProfession(1);
            hiredSim = true;
            System.out.println("sim is hired " + hiredSim);
            this.mc.displayGuiScreen(null);
        }



        }
    }

    private boolean disableButton(int id) {
        buttonList.get(id).enabled = false;
        buttonList.get(id).visible = false;
        return buttonList.get(id).enabled && buttonList.get(id).visible;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public class ButtonList extends GuiScrollingList {
        public ButtonList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
            super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        }

        @Override
        protected int getSize() {
            return 20;
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {

        }

        @Override
        protected boolean isSelected(int index) {
            return false;
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

        }
    }
}





