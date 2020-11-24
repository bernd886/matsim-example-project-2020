package org.matsim.project;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.net.URL;

public class MyRunMatsim_TimeDependentNetwork {

    public static void main(String[] args) {

        Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml") ;
        // configure the time variant network here:
        config.network().setTimeVariantNetwork( true );
        config.controler().setLastIteration( 30 );
        config.controler().setOverwriteFileSetting( OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists );
        // create/load the scenario here.  The time variant network does already have to be set at this point
        // in the config, otherwise it will not work.
        Scenario scenario = ScenarioUtils.loadScenario( config );

        for ( Link link : scenario.getNetwork().getLinks().values() ) {
            double speed = link.getFreespeed();
            final double threshold = 5. / 3.6;
            if ( speed > threshold ) {
                {
                    NetworkChangeEvent event = new NetworkChangeEvent(7. * 3600.);
                    event.setFreespeedChange(new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE_IN_SI_UNITS, threshold / 10));
                    event.addLink(link);
                    NetworkUtils.addNetworkChangeEvent(scenario.getNetwork(), event);
                }
                {
                    NetworkChangeEvent event = new NetworkChangeEvent(11.5 * 3600.);
                    event.setFreespeedChange(new NetworkChangeEvent.ChangeValue(NetworkChangeEvent.ChangeType.ABSOLUTE_IN_SI_UNITS, speed));
                    event.addLink(link);
                    NetworkUtils.addNetworkChangeEvent(scenario.getNetwork(), event);
                }
            }
        }

        Controler controler = new Controler( scenario );
        controler.run();
    }
}
