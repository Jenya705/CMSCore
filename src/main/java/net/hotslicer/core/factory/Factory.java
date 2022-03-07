package net.hotslicer.core.factory;

import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class Factory<T, E> {

    private final Map<String, Function<T, E>> factories = new ConcurrentHashMap<>();

    public void addFactory(String name, Function<T, E> factory) {
        factories.put(name.toLowerCase(Locale.ROOT), factory);
    }

    public void addFactory(String name, Supplier<E> factory) {
        addFactory(name, it -> factory.get());
    }

    public E create(String name, T arg) {
        var factory = factories.get(name.toLowerCase(Locale.ROOT));
        if (factory == null) return null;
        return factory.apply(arg);
    }

    public E create(String name) {
        return create(name, null);
    }

}
