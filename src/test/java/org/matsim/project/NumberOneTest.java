package org.matsim.project;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.utils.eventsfilecomparison.EventsFileComparator;

import java.net.URL;

public class NumberOneTest {

    @Rule
    public MatsimTestUtils utils = new MatsimTestUtils();

    @Test
    public void aMethod(){
        Assert.assertTrue(true);

        URL baseUrl = ExamplesUtils.getTestScenarioURL( "equil-extended" );
        URL finalUrl = IOUtils.extendUrl( baseUrl, "config-with-network-change-events.xml" );
        Config config = ConfigUtils.loadConfig( finalUrl );

        config.controler().setOutputDirectory( utils.getOutputDirectory() );
        config.controler().setLastIteration( 2 );

        Scenario scenario = ScenarioUtils.loadScenario( config );

        Controler controler = new Controler( scenario );

        controler.run();

        {
            Population expected = PopulationUtils.createPopulation( ConfigUtils.createConfig() ) ;
            PopulationUtils.readPopulation( expected, utils.getInputDirectory() + "/output_plans.xml.gz" );

            Population actual = PopulationUtils.createPopulation( ConfigUtils.createConfig() ) ;
            PopulationUtils.readPopulation( actual, utils.getOutputDirectory() + "/output_plans.xml.gz" );

            boolean result = PopulationUtils.comparePopulations( expected, actual );
            Assert.assertTrue( result );
        }
        {
            String expected = utils.getInputDirectory() + "/output_events.xml.gz" ;
            String actual = utils.getOutputDirectory() + "/output_events.xml.gz" ;
            EventsFileComparator.Result result = EventsUtils.compareEventsFiles( expected, actual );
            Assert.assertEquals( EventsFileComparator.Result.FILES_ARE_EQUAL, result );
        }
    }
}
