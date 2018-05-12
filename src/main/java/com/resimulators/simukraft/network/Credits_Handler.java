package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class Credits_Handler implements IMessageHandler<Credits_packets, IMessage> {

    @Override public IMessage onMessage(Credits_packets message, MessageContext ctx){
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                SimEventHandler.setCredits(message.credit);
            }

        });
        //System.out.println("incoming value =:" + message.credit);
        //System.out.println("Credits equal: " + SimToHire.getCredits());
        return null;

    }

}
