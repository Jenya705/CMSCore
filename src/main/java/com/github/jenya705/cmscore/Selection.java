package com.github.jenya705.cmscore;

import lombok.Data;
import net.minestom.server.coordinate.Vec;

/**
 * @author Jenya705
 */
@Data
public class Selection {

    private final Vec min;
    private final Vec max;

    public Selection(Vec vec1, Vec vec2) {
        min = vec1.min(vec2);
        max = vec1.max(vec2);
    }

    public void forEach(PosIConsumer function) {
        for (int x = min.blockX(); x <= max.blockX(); ++x) {
            for (int y = min.blockY(); y <= max.blockY(); ++y) {
                for (int z = min.blockZ(); z <= max.blockZ(); ++z) {
                    function.apply(x, y, z);
                }
            }
        }
    }

}
