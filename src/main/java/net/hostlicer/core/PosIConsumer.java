package net.hostlicer.core;

/**
 * @author Jenya705
 */
@FunctionalInterface
public interface PosIConsumer {

    void apply(int x, int y, int z);

}