package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Random;

public class AISimGotoToWork extends EntityAIBase {
    private  EntitySim sim;
    public AISimGotoToWork(EntitySim sim){
        this.sim = sim;
    }

    @Override
    public void startExecuting(){
        sim.attemptTeleport(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ());
        //sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
    }


    @Override
    public boolean shouldExecute() {
        if (!sim.getLabeledProfession().equals("nitwit")) {
            System.out.println("Sim " + sim.getName() + " everything " + (!sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.getJobBlockPos() != null && sim.getDistanceSq(sim.getJobBlockPos()) > 2));
            System.out.println("Sim " + sim.getName() + " proffesion " + !sim.getLabeledProfession().equals("nitwit"));
            System.out.println("Sim " + sim.getName() + " food " + sim.getFoodLevel());
        }
        return !sim.getLabeledProfession().equals("nitwit") && sim.getFoodLevel() > 10 && FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().isDaytime() && sim.getJobBlockPos() != null && sim.getDistanceSq(sim.getJobBlockPos()) > 1;
    }


    public boolean shouldContinueExecuting(){
        return sim.getDistanceSq(sim.getJobBlockPos()) > 2;
    }

    public void updateTask(){
        if(sim.getDistanceSq(sim.getJobBlockPos()) > 2){
            sim.getNavigator().tryMoveToXYZ(sim.getJobBlockPos().getX(),sim.getJobBlockPos().getY()+0.5d,sim.getJobBlockPos().getZ(),0.7d);
        }

    }

    }

