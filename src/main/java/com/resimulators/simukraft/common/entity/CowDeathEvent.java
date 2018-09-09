package com.resimulators.simukraft.common.entity;

import com.resimulators.simukraft.common.capabilities.ModCapabilities;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.item.base.ItemBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class CowDeathEvent {

    @SubscribeEvent
    public static void CowDeath(LivingDropsEvent event) {
        if (event.getEntity() instanceof EntityCow) {
            if (event.getSource().getTrueSource() instanceof EntitySim) {
                if (!event.getEntity().world.isRemote) {
                    EntityCow cow = (EntityCow) event.getEntity();
                    if (cow.hasCapability(ModCapabilities.getCAP(), null)) {
                        if (cow.getCapability(ModCapabilities.getCAP(), null).iscontroledspawn()) {
                            event.setCanceled(true);
                            List<EntityItem> items = event.getDrops();
                            for (EntityItem item : items) {
                                EntitySim sim = ((EntitySim) event.getSource().getTrueSource());
                                for (int i = 0; i < sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH).getSlots(); i++) {
                                    if (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).getStackInSlot(i).isEmpty() || (sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).getStackInSlot(i).getItem().equals(item.getItem().getItem())) && !(sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).getStackInSlot(i).getCount() + item.getItem().getCount() > 64)) {
                                        if ((sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).getStackInSlot(i).equals(item.getItem()))) {
                                            ItemStack stack = sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).getStackInSlot(i);
                                            ItemStack mergestack = new ItemStack(stack.getItem(), stack.getCount() + item.getItem().getCount());
                                            sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).insertItem(i, mergestack, true);
                                        }
                                        sim.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,EnumFacing.NORTH).insertItem(i, item.getItem(), false);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}