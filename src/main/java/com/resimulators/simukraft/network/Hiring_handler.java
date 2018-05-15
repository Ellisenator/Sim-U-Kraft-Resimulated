package com.resimulators.simukraft.network;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.entity.entitysim.SimEventHandler;
import net.minecraft.util.IThreadListener;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class Hiring_handler  implements IMessageHandler<Hiring_packet, IMessage> {

    @Override public IMessage onMessage(Hiring_packet message, MessageContext ctx){
        IThreadListener mainThread = ctx.getServerHandler().player.getServerWorld();
        mainThread.addScheduledTask(() -> {
            UUID Id = message.sims;
            System.out.println(" removing sim " + Id);
            EntitySim e = (EntitySim) ctx.getServerHandler().player.getServerWorld().getEntityFromUuid(Id);
            SimEventHandler.hireSims(Id);
            e.setProfession(message.job);


            });return null;
    }
}

