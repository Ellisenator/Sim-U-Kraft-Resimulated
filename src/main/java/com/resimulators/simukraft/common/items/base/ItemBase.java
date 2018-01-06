package com.resimulators.simukraft.common.items.base;

import com.resimulators.simukraft.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

/**
 * Created by fabbe on 06/01/2018 - 4:51 AM.
 */
public class ItemBase extends Item {
    public ItemBase(String name, CreativeTabs tab) {
        setName(name);
        setCreativeTab(tab);
    }

    private void setName(String name) {
        this.setRegistryName(Reference.MOD_ID, name);
        this.setUnlocalizedName(this.getRegistryName().toString());
    }
}
