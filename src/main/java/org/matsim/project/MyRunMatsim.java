package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.ControlerUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

public class MyRunMatsim {

    public static void main(String[] args) {

        // config
        // Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml") ;
        Config config = ConfigUtils.createConfig() ;
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists ) ;
        config.controler().setLastIteration( 0 ) ;
        // possibly modify config here
        //--

        // scenario
        Scenario scenario = ScenarioUtils.loadScenario( config ) ;
        // --

        // controler
        Controler controler = new Controler( scenario ) ;
        controler.run() ;
        // --

        // analysis
        // --
    }
}
