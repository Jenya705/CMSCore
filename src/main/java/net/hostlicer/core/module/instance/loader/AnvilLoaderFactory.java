package net.hostlicer.core.module.instance.loader;

import lombok.experimental.UtilityClass;
import net.hostlicer.core.config.ConfigData;
import net.minestom.server.instance.AnvilLoader;

@UtilityClass
public class AnvilLoaderFactory {

    public AnvilLoader create(ConfigData data) {
        if (!data.has("path")) return null;
        String path = data.get("path", "");
        return new AnvilLoader(path);
    }

}
