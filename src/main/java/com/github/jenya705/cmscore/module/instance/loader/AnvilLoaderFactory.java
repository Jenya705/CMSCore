package com.github.jenya705.cmscore.module.instance.loader;

import lombok.experimental.UtilityClass;
import com.github.jenya705.cmscore.config.ConfigData;
import net.minestom.server.instance.AnvilLoader;

@UtilityClass
public class AnvilLoaderFactory {

    public AnvilLoader create(ConfigData data) {
        if (!data.has("path")) return null;
        String path = data.get("path", "");
        return new AnvilLoader(path);
    }

}
