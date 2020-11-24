package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

public class MyRunMatsim3_BasicLoad {

    public static void main(String[] args) {

        // config
        Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
        config.controler().setLastIteration( 20 );
        config.network().setInputFile("network.xml");
        config.plans().setInputFile("plans100.xml");
        //--

        // scenario
        Scenario scenario = ScenarioUtils.loadScenario(config);
        // --

        // controler
        Controler controler = new Controler( scenario );
        controler.run();
        // --

        // analysis
        // --
    }
}
