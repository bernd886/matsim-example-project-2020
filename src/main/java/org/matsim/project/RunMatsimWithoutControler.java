package org.matsim.project;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.geotools.referencing.crs.AbstractCRS;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.*;
import org.matsim.core.controler.corelisteners.ControlerDefaultCoreListenersModule;
import org.matsim.core.router.TripRouter;
import org.matsim.core.scenario.ScenarioByInstanceModule;
import org.matsim.core.scenario.ScenarioUtils;

public class RunMatsimWithoutControler {

    public static void main(String[] args) {
        Config config = ConfigUtils.createConfig();
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);

        Scenario scenario = ScenarioUtils.loadScenario(config);


        Module module = new AbstractModule() {
            @Override
            public void install() {
                install(new NewControlerModule());
                install(new ControlerDefaultsModule());
                install(new ControlerDefaultCoreListenersModule());
                install(new ScenarioByInstanceModule(scenario));
            }
        };
        com.google.inject.Injector injector = org.matsim.core.controler.Injector.createInjector(config, module);
        TripRouter tripRouter = injector.getInstance(TripRouter.class);
    }
}
