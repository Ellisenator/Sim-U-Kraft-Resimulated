package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class HiringHandler implements IMessageHandler<HiringPacket, IMessage> {
    IThreadListener mainThread;
    boolean reply;
    @Override public IMessage onMessage(HiringPacket message, MessageContext ctx){

        if (FMLCommonHandler.instance().getSide() == Side.SERVER){
        mainThread = ctx.getServerHandler().player.getServerWorld();
        reply = true;

        }
        else
            {
                mainThread = Minecraft.getMinecraft();
                reply = false;
                System.out.println("receiving on client side");
            }
        mainThread.addScheduledTask(() -> {
            EntitySim sim;
            if (reply)
            {
                sim = (EntitySim) ctx.getServerHandler().player.world.getEntityByID(message.sims);
            } else
                {
                    sim = (EntitySim) Minecraft.getMinecraft().world.getEntityByID(message.sims);
                }
                UUID id = sim.getUniqueID();
                System.out.println("Hiring sim");
                SimEventHandler.getWorldSimData().hiredsim(id);
                sim.setProfession(message.job);
                if (reply)
                {
                    System.out.println("returning packet to update unemployed sims");
                    PacketHandler.INSTANCE.sendToAll(new HiringPacket(message.sims,message.job));
                }



        });return null;
    }
}

