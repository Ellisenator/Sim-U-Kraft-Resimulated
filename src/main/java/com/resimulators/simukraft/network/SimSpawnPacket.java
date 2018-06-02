package com.resimulators.simukraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;

public class SimSpawnPacket implements IMessage {


    public SimSpawnPacket(UUID id){
        this.sims = id;

    }
    public SimSpawnPacket(){

    }
    UUID sims;
    @Override
    public void toBytes(ByteBuf bytebuf){
        ByteBufUtils.writeUTF8String(bytebuf, sims.toString());

    }

    @Override
    public void fromBytes(ByteBuf bytebuf){
        this.sims = UUID.fromString(ByteBufUtils.readUTF8String(bytebuf));

    }
}
