package com.resimulators.simukraft.common.entity.entitysim;
import com.resimulators.simukraft.common.entity.player.SaveSimData;
import com.resimulators.simukraft.network.PacketHandler;
import com.resimulators.simukraft.network.Siminfo_packet;
import ibxm.Player;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

import java.util.*;


public class SimEventHandler {
    static SaveSimData data;
    public static float getCredits() {
        //System.out.println(" getting credits that is equal to: " + credits);
        return credits;
    }
    public static float setCredits(float credit){
        credits = credit;
        //System.out.println(" set credits to" + credits);
        return credits;
    }
    private static float credits = 10;


    public static SaveSimData getWorldSimData() {

        return data;
    }

    @SubscribeEvent
    public void Sim_Spawn(LivingSpawnEvent event)
    {
        World world = event.getWorld();
        data = SaveSimData.get(world);

        if (event.getEntity() instanceof EntitySim){

            if (!world.isRemote)
            {

            if (!data.getTotalSims().contains(event.getEntity().getPersistentID()))
            {
                UUID id = event.getEntity().getPersistentID();
                data.spawnedSim(id);

                PacketHandler.INSTANCE.sendToAll(new Siminfo_packet(id));
            }
        }
    }
}

    @SubscribeEvent
    public void Sim_Death(LivingDeathEvent event){


        if (event.getEntity() instanceof EntitySim){
            EntitySim sim = (EntitySim) event.getEntity();
            World world = sim.world;
            if (!world.isRemote)
            {
                UUID id = event.getEntity().getPersistentID();
                data.simDied(id);
            }

        }
    }
}