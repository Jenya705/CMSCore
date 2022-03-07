package com.github.jenya705.cmscore.module.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minestom.server.attribute.AttributeOperation;

/**
 * @author Jenya705
 */
@Getter
@RequiredArgsConstructor
public class CustomItemAttribute {

    private final float value;
    private final AttributeOperation operation;

}
