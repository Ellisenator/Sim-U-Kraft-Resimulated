package com.resimulators.simukraft.network;

import com.resimulators.simukraft.GuiHandler;
import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.client.gui.GuiHire;
import com.resimulators.simukraft.common.interfaces.iSimJob;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReturnSimIdHandler implements IMessageHandler<ReturnSimIdPacket,IMessage> {
    @SideOnly(Side.CLIENT)
    @Override
    public IMessage onMessage(ReturnSimIdPacket message, MessageContext ctx) {
        Minecraft mc = Minecraft.getMinecraft();
        TileEntity entity = mc.world.getTileEntity(new BlockPos(message.x,message.y,message.z));
        System.out.println(mc.currentScreen);
        for(int i = 0;i<((iSimJob)entity).getnames().size();i++)
        {
            ((iSimJob)entity).getnames().remove(i);
        }
        for(int sim: message.sim_ids)
        {
                ((iSimJob)entity).addSim(sim);
        }
        for (String name: message.sim_names)
        {
                ((iSimJob)entity).addSimName(name);
        }
        mc.player.openGui(SimUKraft.instance, GuiHandler.GUI_HIRED, mc.world, message.x, message.y, message.z);
        return null;
    }
}
