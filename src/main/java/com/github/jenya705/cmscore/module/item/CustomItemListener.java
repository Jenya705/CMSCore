package com.github.jenya705.cmscore.module.item;

import com.github.jenya705.cmscore.module.item.event.CustomItemEvent;
import net.minestom.server.event.EventNode;

/**
 * @author Jenya705
 */
@FunctionalInterface
public interface CustomItemListener {

    void register(EventNode<CustomItemEvent> itemEventNode);

}
