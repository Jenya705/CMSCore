package com.github.jenya705.cmscore.extension;

import com.github.jenya705.cmscore.CoreApplication;
import net.minestom.server.extensions.Extension;

public final class CoreExtension extends Extension {

    @Override
    public void initialize() {
        CoreApplication application = new CoreApplication(getDataDirectory());
        application.start();
    }

    @Override
    public void terminate() {
        CoreApplication.getInstance().stop();
    }
}
