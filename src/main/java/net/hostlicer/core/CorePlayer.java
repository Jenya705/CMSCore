package net.hostlicer.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.tag.Tag;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTIntArray;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class CorePlayer {

    private static Tag<NBT> pos1Tag = Tag.NBT("pos1");
    private static Tag<NBT> pos2Tag = Tag.NBT("pos2");

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

    public Vec getMinPos() {
        if (pos1 == null || pos2 == null) return null;
        return pos1.min(pos2);
    }

    public Vec getMaxPos() {
        if (pos1 == null || pos2 == null) return null;
        return pos2.max(pos1);
    }

    public void forEachSelection(PosIConsumer consumer) {
        Vec min = getMinPos();
        Vec max = getMaxPos();
        for (int x = min.blockX(); x <= max.blockX(); ++x) {
            for (int y = min.blockY(); y <= max.blockY(); ++y) {
                for (int z = min.blockZ(); z <= max.blockZ(); ++z) {
                    consumer.apply(x, y, z);
                }
            }
        }
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
