package com.resimulators.simukraft;

import com.resimulators.simukraft.init.ModItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by fabbe on 06/01/2018 - 5:31 AM.
 */
public class SimUTab {
    public static CreativeTabs SUTab = new CreativeTabs(Reference.MOD_ID) {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(Items.STICK);
        }
    };
}
