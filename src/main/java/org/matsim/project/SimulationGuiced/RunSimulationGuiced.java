package org.matsim.project.SimulationGuiced;

import com.google.inject.*;
import com.google.inject.Module;
import org.matsim.project.SimulationGuiced.alternative.HelperMyImpl;
import org.matsim.project.SimulationGuiced.base.Helper;
import org.matsim.project.SimulationGuiced.base.HelperDefaultImpl;
import org.matsim.project.SimulationGuiced.base.Simulation;
import org.matsim.project.SimulationGuiced.base.SimulationDefaultImpl;

public class RunSimulationGuiced {

    public static void main(String[] args) {

        Module module = new AbstractModule() {
            // google guice abstract class with "@inject"
            // aim: replacing constructors in order to inject dependencies
            @Override
            protected void configure() {
                // put implementations underneath its interface
                // push up to cloud system
                this.bind( Simulation.class ).to( SimulationDefaultImpl.class );
                //this.bind( Helper.class ).to( HelperDefaultImpl.class );
                this.bind( Helper.class ).to( HelperMyImpl.class );
            }
        };

        // bundle stuff together
        Injector injector = Guice.createInjector( module );

        // out of bundled stuff, pull down an instance of "Simulation" down from cloud system
        Simulation simulation = injector.getInstance( Simulation.class );
        simulation.doStep();
    }
}