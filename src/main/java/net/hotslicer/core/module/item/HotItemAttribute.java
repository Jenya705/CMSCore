package net.hotslicer.core.module.item;

import lombok.RequiredArgsConstructor;

/**
 * @author Jenya705
 */
@RequiredArgsConstructor
public class HotItemAttribute {

    private final float value;
    private final boolean coefficient;

    /**
     *
     * Applies attribute value to given
     *
     * @param currentValue Given value
     * @return Modified value
     */
    public float apply(float currentValue) {
        if (coefficient) {
            return currentValue * (1 + value);
        }
        return currentValue + value;
    }

    /**
     *
     * Returns attribute value from applied
     *
     * @param appliedValue Applied value
     * @return Attribute value
     */
    public float ret(float appliedValue) {
        if (coefficient) {
            return appliedValue / (1 + value);
        }
        return appliedValue - value;
    }

}
