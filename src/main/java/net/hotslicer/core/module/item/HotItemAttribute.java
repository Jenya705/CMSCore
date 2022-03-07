package net.hotslicer.core.module.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.attribute.AttributeOperation;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class HotItemAttribute {

    private final float value;
    private final AttributeOperation operation;

}
