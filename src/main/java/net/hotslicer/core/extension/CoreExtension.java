package net.hotslicer.core.extension;

import net.hotslicer.core.CoreApplication;
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
