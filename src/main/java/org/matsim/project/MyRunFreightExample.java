package org.matsim.project;

/* *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
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
 * *********************************************************************** */

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.ControlerUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;

import static org.matsim.core.config.groups.ControlerConfigGroup.*;


/**
 * @see org.matsim.contrib.freight
 */
public class MyRunFreightExample {

    private static final URL scenarioUrl ;
    static{
        scenarioUrl = ExamplesUtils.getTestScenarioURL( "freight-chessboard-9x9" ) ;
    }

    public static void main(String[] args) {

        // ### config stuff: ###

        Config config = ConfigUtils.loadConfig( IOUtils.extendUrl(scenarioUrl, "config.xml" ) );

        config.plans().setInputFile( null ); // remove passenger input

        //more general settings
        config.controler().setOutputDirectory("./output/freight" );

        config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );
        new OutputDirectoryHierarchy( config.controler().getOutputDirectory(), config.controler().getRunId(), config.controler().getOverwriteFileSetting(), true, CompressionType.gzip ) ;
//		new OutputDirectoryHierarchy( config ); # future
        config.controler().setOverwriteFileSetting( OverwriteFileSetting.overwriteExistingFiles );
        // (the directory structure is needed for jsprit output, which is before the controler starts.  Maybe there is a better alternative ...)

        config.global().setRandomSeed(4177 );

        config.controler().setLastIteration(0 );
        // yyyyyy iterations currently do not work; needs to be fixed.

        //freight settings
        FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule( config, FreightConfigGroup.class ) ;
        freightConfigGroup.setCarriersFile( "singleCarrierFiveActivitiesWithoutRoutes.xml");
        freightConfigGroup.setCarriersVehicleTypesFile( "vehicleTypes.xml");

        ControlerUtils.checkConfigConsistencyAndWriteToLog( config, "dump" );

        // ### scenario stuff: ###
        Scenario scenario = ScenarioUtils.loadScenario( config );

//		Building the Carriers, running jsprit for solving the VRP:

        //load carriers according to freight config
        FreightUtils.loadCarriersAccordingToFreightConfig( scenario );

//		FreightUtils.getCarrierVehicleTypes( scenario ).getVehicleTypes().get("light" ).getCapacity().setOther( 1 );

        //### Output before jsprit run (not necessary)
        new CarrierPlanXmlWriterV2(FreightUtils.getCarriers( scenario )).write( scenario.getConfig().controler().getOutputDirectory() + "/jsprit_unplannedCarriers.xml" ) ;

        //Solving the VRP (generate carrier's tour plans)
        try {
            FreightUtils.runJsprit( scenario, ConfigUtils.addOrGetModule( scenario.getConfig(), FreightConfigGroup.class ) );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //### Output after jsprit run (not necessary)
        new CarrierPlanXmlWriterV2(FreightUtils.getCarriers( scenario )).write( scenario.getConfig().controler().getOutputDirectory() + "/jsprit_plannedCarriers.xml" ) ;


        //MATSim configuration:
        final Controler controler = new Controler( scenario ) ;
        Freight.configure( controler );

        OTFVisConfigGroup otfVisConfigGroup = ConfigUtils.addOrGetModule( config, OTFVisConfigGroup.class );
        otfVisConfigGroup.setLinkWidth( 10 );
        otfVisConfigGroup.setDrawNonMovingItems( false );
        config.qsim().setTrafficDynamics( QSimConfigGroup.TrafficDynamics.kinematicWaves );
        config.qsim().setSnapshotStyle( QSimConfigGroup.SnapshotStyle.kinematicWaves );
        controler.addOverridingModule( new OTFVisLiveModule() );

//		start of the MATSim-Run:
        controler.run();
    }


}
