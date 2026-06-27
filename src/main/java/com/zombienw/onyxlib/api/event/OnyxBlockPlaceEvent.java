package com.zombienw.onyxlib.api.event;

import com.zombienw.onyxlib.api.block.OnyxBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.jspecify.annotations.NonNull;

/**
 * Fired immediately after an OnyxBlock is placed and its display entity is spawned.
 */
public class OnyxBlockPlaceEvent extends BlockEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final OnyxBlock onyxBlock;

    public OnyxBlockPlaceEvent(
            @NonNull Block theBlock,
            @NonNull Player player,
            @NonNull OnyxBlock onyxBlock
    ) {
        super(theBlock);
        this.player = player;
        this.onyxBlock = onyxBlock;
    }

    @NonNull public Player getPlayer() { return this.player; }
    @NonNull public OnyxBlock getOnyxBlock() { return this.onyxBlock; }

    @NonNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    @NonNull
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}