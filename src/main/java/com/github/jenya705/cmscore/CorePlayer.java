package com.github.jenya705.cmscore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTIntArray;

import java.util.Objects;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class CorePlayer {

    private static Tag<NBT> pos1Tag = Tag.NBT("pos1");
    private static Tag<NBT> pos2Tag = Tag.NBT("pos2");

    @Getter
    private final Player player;

    private final Vec pos1;
    private final Vec pos2;

    public CorePlayer setPos(int posIndex, Vec pos) {
        if (posIndex == 0) {
            player.setTag(pos1Tag, toNbt(pos));
            return new CorePlayer(player, pos, pos2);
        }
        if (posIndex == 1) {
            player.setTag(pos2Tag, toNbt(pos));
            return new CorePlayer(player, pos1, pos);
        }
        return this;
    }

    public boolean isEmptySelection() {
        return pos1 == null && pos2 == null;
    }

    public boolean isSingleSelection() {
        return !isEmptySelection() && !isFullSelection();
    }

    public boolean isFullSelection() {
        return pos1 != null && pos2 != null;
    }

    public Vec getSingleSelection() {
        if (isSingleSelection()) {
            return Objects.requireNonNullElse(pos1, pos2);
        }
        return null;
    }

    public Selection getSelection() {
        if (isSingleSelection()) {
            Vec vec = getSingleSelection();
            return new Selection(vec, vec);
        }
        if (isFullSelection()) {
            return getFullSelection();
        }
        return null;
    }

    public Selection getFullSelection() {
        if (isFullSelection()) {
            return new Selection(pos1, pos2);
        }
        return null;
    }

    public static CorePlayer of(Player player) {
        Vec pos1 = null; Vec pos2 = null;
        if (player.getTag(pos1Tag) instanceof NBTIntArray pos1array) {
            pos1 = toPos(pos1array.getValue().copyArray());
        }
        if (player.getTag(pos2Tag) instanceof NBTIntArray pos2array) {
            pos2 = toPos(pos2array.getValue().copyArray());
        }
        return new CorePlayer(
                player, pos1, pos2
        );
    }

    private static Vec toPos(int[] array) {
        if (array.length != 3) return null;
        return new Vec(
                array[0],
                array[1],
                array[2]
        );
    }

    private static NBTIntArray toNbt(Vec pos) {
        return NBT.IntArray(pos.blockX(), pos.blockY(), pos.blockZ());
    }

}
