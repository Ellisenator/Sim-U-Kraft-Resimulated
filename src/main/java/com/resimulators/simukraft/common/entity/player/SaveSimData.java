package com.resimulators.simukraft.common.entity.player;

import com.resimulators.simukraft.Reference;
import com.resimulators.simukraft.common.interfaces.ISimJob;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class SaveSimData extends WorldSavedData {
    private static final String DATA_NAME = Reference.MOD_ID + "_SimData";
    private Set<UUID> Total_sims = new HashSet<>();
    private Set<UUID> Unemployed_sims = new HashSet<>();
    private Set<TileEntity> job_tiles = new HashSet<>();
    private Map<UUID,Integer> mode = new HashMap<>();
    private Map<UUID,Boolean> enables = new HashMap<>();
    public SaveSimData() {
        super(DATA_NAME);
    }

    public SaveSimData(String s) {
        super(s);
    }

    public Set<UUID> getTotalSims() {
        return Total_sims;
    }

    public void setUnemployed_sims(UUID id) {
        Unemployed_sims.add(id);
        markDirty();
    }

    public Set<UUID> getUnemployed_sims() {
        return Unemployed_sims;
    }

    public void addSim(UUID id) {
        Total_sims.add(id);
        markDirty();
    }

    public void spawnedSim(UUID id) {
        Total_sims.add(id);
        Unemployed_sims.add(id);
        markDirty();
    }

    public void simDied(UUID id) {
        Total_sims.remove(id);
        Unemployed_sims.remove(id);
        markDirty();
    }

    public void hiredsim(UUID id) {
        Unemployed_sims.remove(id);
        markDirty();
    }

    public void firedSim(UUID id) {
        Unemployed_sims.add(id);
        markDirty();
    }

    public void addTile(TileEntity tile) {
        job_tiles.add(tile);
        markDirty();
    }

    public boolean isEnabled(UUID id) {
        return enables.getOrDefault(id, false);
    }

    public void setEnabled(UUID id,boolean enable) {
        enables.put(id,enable);
        markDirty();
    }


    public Set<TileEntity> getJob_tiles() {
        return job_tiles;
    }

    public void removeTile(TileEntity tile) {
        if (job_tiles.contains(tile)) {
            job_tiles.remove(tile);
            markDirty();
        }
    }


    public Integer isMode(UUID id)
    {
        return mode.get(id);
    }

    public void setMode(UUID id,int mode)
    {
        this.mode.put(id,mode);
        markDirty();
    }
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList Ttaglist = nbt.getTagList("TSims", Constants.NBT.TAG_COMPOUND);
        NBTTagList Utaglist = nbt.getTagList("USims", Constants.NBT.TAG_COMPOUND);
        NBTTagList JobTiles = nbt.getTagList("JobTiles", Constants.NBT.TAG_COMPOUND);
        NBTTagList EnabledList = nbt.getTagList("enables",Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < Ttaglist.tagCount(); i++) {
            NBTTagCompound tag = Ttaglist.getCompoundTagAt(i);
            UUID id = tag.getUniqueId("TSim");
            addSim(id);
        }
        for (int i = 0; i < Utaglist.tagCount(); i++) {
            NBTTagCompound tag = Utaglist.getCompoundTagAt(i);
            UUID id = tag.getUniqueId("USim");
            setUnemployed_sims(id);
        }

        for (int i = 0; i < JobTiles.tagCount(); i++) {
            NBTTagCompound tag = JobTiles.getCompoundTagAt(i);
            int x = tag.getInteger("Tile x");
            int y = tag.getInteger("Tile y");
            int z = tag.getInteger("Tile Z");
            BlockPos pos = new BlockPos(x, y, z);
            TileEntity entity = Minecraft.getMinecraft().world.getTileEntity(pos);
            if (entity instanceof ISimJob) {
                addTile(entity);
            }
        }
        for (int i = 0;i < EnabledList.tagCount();i++){
            NBTTagCompound enabled = EnabledList.getCompoundTagAt(i);
            setEnabled(UUID.fromString(enabled.getString("uuid")),enabled.getBoolean("enable"));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList Ttaglist = new NBTTagList();
        NBTTagList Utaglist = new NBTTagList();
        NBTTagList JobTiles = new NBTTagList();
        NBTTagList modeList = new NBTTagList();
        NBTTagList enabledList = new NBTTagList();
        for (UUID id: Total_sims)
        {
            NBTTagCompound sims = new NBTTagCompound();
            sims.setUniqueId("TSim", id);
            Ttaglist.appendTag(sims);
        }
        nbt.setTag("TSims", Ttaglist);

        for (UUID id : Unemployed_sims) {
            NBTTagCompound sims = new NBTTagCompound();
            sims.setUniqueId("USim", id);
            Utaglist.appendTag(sims);
        }
        nbt.setTag("USims", Utaglist);

        for (TileEntity tiles : job_tiles) {
            NBTTagCompound tile = new NBTTagCompound();
            tile.setInteger("Tile x", tiles.getPos().getX());
            tile.setInteger("Tile y", tiles.getPos().getY());
            tile.setInteger("Tile Z", tiles.getPos().getZ());
            JobTiles.appendTag(tile);


        }
        nbt.setTag("JobTiles",JobTiles);
        for (UUID uuid: mode.keySet())
        {
            NBTTagCompound modes = new NBTTagCompound();
            modes.setInteger(uuid.toString(),mode.get(uuid));
            modeList.appendTag(modes);
        }
        nbt.setTag("Modes",modeList);
        for (UUID uuid: enables.keySet()) {
            NBTTagCompound enabled = new NBTTagCompound();
            enabled.setBoolean("enable",enables.get(uuid));
            enabled.setString("uuid",uuid.toString());
            enabledList.appendTag(enabled);

        }
        nbt.setTag("enables",enabledList);
        return nbt;
    }

    public static SaveSimData get(World world) {
        MapStorage storage = world.getMapStorage();
        if (storage == null) return null;
        SaveSimData instance = (SaveSimData) storage.getOrLoadData(SaveSimData.class, DATA_NAME);
        if (instance == null) {
            instance = new SaveSimData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;

    }
}