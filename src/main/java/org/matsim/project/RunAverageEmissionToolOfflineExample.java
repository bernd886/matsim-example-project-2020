/*
package org.matsim.project;

*/
/* *********************************************************************** *
 * project: org.matsim.*
 * RunEmissionToolOffline.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** *//*


import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.emissions.EmissionModule;
import org.matsim.contrib.emissions.utils.EmissionsConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Injector;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.EventWriterXML;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.MatsimVehicleWriter;
import org.matsim.vehicles.VehicleUtils;


*/
/**
 *
 * Use the config file as created by the
 * {@link CreateEmissionConfig CreateEmissionConfig} to calculate
 * emissions based on the link leave events of an events file. Resulting emission events are written into an event file.
 *
 * @author benjamin, julia
 *//*

public final class RunAverageEmissionToolOfflineExample{

    private final static String runDirectory = "./output/Emissions/";
    private static final String configFile = "./scenarios/sampleScenario/testv2_Vehv1/config_average.xml";

    private static final String eventsFile =  "./scenarios/sampleScenario/5.events.xml.gz";
    // (remove dependency of one test/execution path from other. kai/ihab, nov'18)

    private static final String emissionEventOutputFileName = "5.emission.events.offline.xml.gz";
    private Config config;

    // =======================================================================================================

    public static void main (String[] args){
        RunAverageEmissionToolOfflineExample emissionToolOfflineExampleV2 = new RunAverageEmissionToolOfflineExample();
        emissionToolOfflineExampleV2.run();
    }

    public Config prepareConfig() {
        config = ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup());
        return config;
    }

    public Config prepareConfig(String configFile) {
        config = ConfigUtils.loadConfig(configFile, new EmissionsConfigGroup());
        return config;
    }

    public void run() {
        if ( config==null ) {
            this.prepareConfig() ;
        }
        Scenario scenario = ScenarioUtils.loadScenario(config);
        EventsManager eventsManager = EventsUtils.createEventsManager();

        AbstractModule module = new AbstractModule(){
            @Override
            public void install(){
                bind( Scenario.class ).toInstance( scenario );
                bind( EventsManager.class ).toInstance( eventsManager );
                bind( EmissionModule.class ) ;
            }
        };;

        com.google.inject.Injector injector = Injector.createInjector(config, module );

        EmissionModule emissionModule = injector.getInstance(EmissionModule.class);

        final String outputDirectory = scenario.getConfig().controler().getOutputDirectory();
        EventWriterXML emissionEventWriter = new EventWriterXML( outputDirectory + emissionEventOutputFileName );
        emissionModule.getEmissionEventsManager().addHandler(emissionEventWriter);

        MatsimEventsReader matsimEventsReader = new MatsimEventsReader(eventsManager);
        matsimEventsReader.readFile(eventsFile);

        emissionEventWriter.closeFile();

        new MatsimVehicleWriter( scenario.getVehicles() ).writeFile( outputDirectory + "vehicles.xml.gz" );

    }
}
*/
