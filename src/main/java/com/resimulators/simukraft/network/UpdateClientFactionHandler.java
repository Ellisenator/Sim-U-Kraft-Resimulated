package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class UpdateClientFactionHandler implements IMessageHandler<UpdateClientFactionPacket,IMessage> {
    @Override
    public IMessage onMessage(UpdateClientFactionPacket message, MessageContext ctx) {
        IThreadListener mainThread = Minecraft.getMinecraft();


        mainThread.addScheduledTask(()->{
           long id  = Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).getfactionid();
            Minecraft.getMinecraft().player.getCapability(ModCapabilities.PlayerCap,null).getfaction(id).deserializeNBT(message.compound);

        });



        return null;
    }
}
