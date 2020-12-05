// 
// Decompiled by Procyon v0.5.36
// 

package com.veteran.hack.event.events;

import net.minecraft.network.play.server.SPacketChunkData;
import net.minecraft.world.chunk.Chunk;
import com.veteran.hack.event.MinecraftEvent;

public class ChunkEvent extends MinecraftEvent
{
    private Chunk chunk;
    private SPacketChunkData packet;
    
    public ChunkEvent(final Chunk chunk, final SPacketChunkData packet) {
        this.chunk = chunk;
        this.packet = packet;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
    
    public SPacketChunkData getPacket() {
        return this.packet;
    }
}