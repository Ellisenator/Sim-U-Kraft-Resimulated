package com.resimulators.simukraft.common.entity.ai;

import com.resimulators.simukraft.SimUKraft;
import com.resimulators.simukraft.common.entity.entitysim.EntitySim;
import com.resimulators.simukraft.common.tileentity.structure.Structure;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.*;

/**
 * Created by fabbe on 20/01/2018 - 11:04 AM.
 * !!! PLEASE NOTE !!!
 * THIS IS STILL IN EARLY DEVELOPMENT AND THAT THE CODE ISN'T IN IT'S FINAL STATE.
 * PLEASE DO:
 * * SUGGEST CHANGES ON HOW THIS CAN BE DONE BETTER.
 * * COMMIT ADJUSTMENTS THAT COULD IMPROVE THIS.
 * PLEASE DON'T:
 * * SAY "THIS IS CRAP, KYS".
 * * COMPLAIN ABOUT BUGS, WITHOUT GIVING AN ACTUAL REPORT ABOUT WHAT'S NOT WORKING.
 */
public class AISimBuild extends EntityAIBase {
    private EntitySim entitySim;
    private Structure structure;
    private int progress;
    private BlockPos startPos;
    private BlockPos currentPos;
    private int cooldown = 0;
    private int tries = 0;
    private Random rand = new Random();

    //Special types
    private int specialIndex;
    private List<IBlockState> specialStates = new ArrayList<>();
    private List<BlockPos> specialPositions = new ArrayList<>();

    public AISimBuild(EntitySim entitySim) {
        this.entitySim = entitySim;
    }

    @Override
    public boolean shouldExecute() {
        if (this.entitySim != null) {
            if (this.entitySim.getProfession() == 1) {
                if (this.entitySim.getStructure() != null) {
                    return this.entitySim.isAllowedToBuild();
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute();
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        this.structure = this.entitySim.getStructure();
        this.startPos = this.entitySim.getStartPos();
        this.currentPos = this.entitySim.getStartPos();
        this.specialPositions.clear();
        this.specialStates.clear();
        this.specialIndex = 0;
        SimUKraft.getLogger().info("Executing AISimBuild");
    }

    @Override
    public void resetTask() {

    }

    @Override
    public void updateTask() {
        //Code by Astavie, modified by fabbe50
        if (structure == null) {
            progress = 0;
        } else {
            int x = (progress % structure.getWidth());
            int z = (progress / structure.getWidth()) % structure.getDepth();
            int y = (progress / structure.getWidth()) / structure.getDepth();
            if (!this.specialPositions.isEmpty() && !this.specialStates.isEmpty() && y >= structure.getHeight() && this.specialIndex < this.specialPositions.size()) {
                if (this.specialPositions.get(specialIndex).getDistance((int) this.entitySim.posX, (int) this.entitySim.posY, (int) this.entitySim.posZ) < 4) {
                    if (cooldown == 0) {
                        this.entityPlaceBlock(this.specialStates.get(specialIndex), this.specialPositions.get(specialIndex));
                        this.specialIndex++;
                        cooldown = rand.nextInt(10) + 5;
                    } else {
                        cooldown--;
                    }
                } else {
                    this.moveEntityCloser(this.specialPositions.get(specialIndex));
                }
            } else if (y >= structure.getHeight()) {
                progress = 0;
                this.entitySim.attemptTeleport(entitySim.getStartPos().getX(), entitySim.getStartPos().getY(), entitySim.getStartPos().getZ());
                this.entitySim.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                this.entitySim.setStructure(null);
                this.entitySim.setAllowedToBuild(false);
                this.entitySim.setStartPos(null);
                this.specialPositions.clear();
                this.specialStates.clear();
            } else {
                // TODO: make it require items from inventory
                Block block = structure.getBlock(x, y, z).getBlock();
                if (block == this.entitySim.world.getBlockState(currentPos.add(x + 1, y, z)).getBlock()) {
                    currentPos = startPos.add(x + 1, y, z);
                    progress++;
                } else if (block instanceof BlockLadder || block instanceof BlockTorch || block instanceof BlockButton || block instanceof BlockLever || block instanceof BlockWallSign) {
                    currentPos = startPos.add(x + 1, y, z);
                    this.specialStates.add(structure.getBlock(x, y, z));
                    this.specialPositions.add(currentPos);
                    progress++;
                } else if (currentPos.getDistance((int) this.entitySim.posX, (int) this.entitySim.posY, (int) this.entitySim.posZ) < 4) {
                    if (cooldown == 0) {
                        currentPos = startPos.add(x + 1, y, z);
                        if (block instanceof BlockDoor && structure.getBlock(x, y, z) == structure.getBlock(x, y, z).withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER)) {
                            this.entityPlaceBlock(this.structure.getBlock(x, y, z), currentPos);
                            this.entitySim.world.setBlockState(currentPos.offset(EnumFacing.UP), structure.getBlock(x, y + 1, z));
                        } else
                            this.entityPlaceBlock(this.structure.getBlock(x, y, z), currentPos);
                        progress++;
                        cooldown = rand.nextInt(10) + 5;
                    } else {
                        cooldown--;
                    }
                } else {
                    this.moveEntityCloser(this.currentPos);
                }
            }
        }
    }

    private void moveEntityCloser(BlockPos thePos) {
        if (this.entitySim.getDistanceSq(thePos) > 256.0D) {
            Vec3d vec3d = RandomPositionGenerator.findRandomTargetBlockTowards(this.entitySim, 14, 7, new Vec3d((double) thePos.getX() + 0.5D, (double) thePos.getY(), (double) thePos.getZ() + 0.5D));
            if (vec3d != null) {
                boolean success = this.entitySim.getCustomNavigator().tryMoveToXYZ(vec3d.x, vec3d.y, vec3d.z, 0.7D);
                if (!success) {
                    if (tries == 0) {
                        this.entitySim.attemptTeleport(vec3d.x + (rand.nextInt(20) - 10) + 0.5, vec3d.y + (rand.nextInt(20) - 10), vec3d.z + (rand.nextInt(20) - 10) + 0.5);
                        tries = 5;
                    } else {
                        tries--;
                    }
                }
            }
        } else {
            boolean success = this.entitySim.getCustomNavigator().tryMoveToXYZ(thePos.getX(), thePos.getY(), thePos.getZ(), 0.7D);
            if (!success) {
                if (tries == 0) {
                    this.entitySim.attemptTeleport(thePos.getX() + (rand.nextInt(6) - 3) + 0.5, thePos.getY() + (rand.nextInt(6) - 3), thePos.getZ() + (rand.nextInt(6) - 3) + 0.5);
                    tries = 20;
                } else {
                    tries--;
                }
            }
        }
    }

    private void entityPlaceBlock(IBlockState blockState, BlockPos pos) {
        this.entitySim.getLookHelper().setLookPosition(currentPos.getX(), currentPos.getY(), currentPos.getZ(), 360, 360);
        this.entitySim.setHeldItem(EnumHand.MAIN_HAND, blockState.getBlock().getItem(null, null, blockState));
        this.entitySim.world.setBlockState(pos, blockState);
        this.entitySim.world.playSound(null, pos, blockState.getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 1.0f, (rand.nextFloat() - 0.5f) / 5);
        this.entitySim.swingArm(EnumHand.MAIN_HAND);
    }
}