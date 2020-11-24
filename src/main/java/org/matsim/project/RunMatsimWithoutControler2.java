package org.matsim.project;

import com.google.inject.Module;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.*;
import org.matsim.core.controler.corelisteners.ControlerDefaultCoreListenersModule;
import org.matsim.core.router.TripRouter;
import org.matsim.core.scenario.ScenarioByInstanceModule;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.List;

public class RunMatsimWithoutControler2 {

    // This gives you Matsim infra without having to "bring up" everything
    // usefull for:
    // *1: seeing how its parallel to Guice (similar way of plugging it together)
    // *2: tests: using just parts of matsim (without bringing up everything)
    // *3: cases where you just want certain piece of matsim (like tripRouter)

    public static void main(String[] args) {

        // config
        Config config = ConfigUtils.createConfig();
        // possibly modify config here
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles );
        //--

        // scenario
        Scenario scenario = ScenarioUtils.loadScenario( config );
        // get everything in: network, transit schedule, whatever infra
        // --

        // put it all in abstract module
        // !!! Matsim version !!!
        Module module = new AbstractModule() {
            @Override
            public void install() {
                install( new NewControlerModule());
                install( new ControlerDefaultCoreListenersModule());
                // !!! ControlerDefaultsModule controls how stuff is plugged together !!!
                // want to plug stuff together by yourself?
                // want to replace something, go beyond code-examples, see how it's done inside matsim?
                // look it up here
                install( new ControlerDefaultsModule());
                install( new ScenarioByInstanceModule( scenario ));
            }
        };

        // get it back from cloud for use
        // !!! Google injector !!!
        com.google.inject.Injector injector = Injector.createInjector(config, module);

        TripRouter tripRouter = injector.getInstance( TripRouter.class );
//        List<? extends PlanElement> result = tripRouter.calcRoute(mainMode, fromFacility, toFacility, departureTime, person);
    }

}
