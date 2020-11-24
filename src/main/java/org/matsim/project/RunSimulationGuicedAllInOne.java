package org.matsim.project;

import com.google.inject.*;
import com.google.inject.Module;

public class RunSimulationGuicedAllInOne {

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

    // should be in a seperate file as an interface
    interface Helper {
        void help();
    }

    // should be in a seperate file as an interface
    interface Simulation {
        void doStep();
    }

    // should be in a seperate file as an implementation
    static class HelperDefaultImpl implements Helper {
        public void help() {
            System.out.println( this.getClass().getSimpleName() + " is helping" );
        }
    }
    // to switch implementations behind interfaces,
    // create new implementation and connect it to interface (see above: "bind")
    static class HelperMyImpl implements Helper {
        @Override
        public void help() {
            System.out.println( this.getClass().getSimpleName() + " is helping better" );
        }
    }

    // should be in a seperate file as an implementation
    static class SimulationDefaultImpl implements Simulation {
        // same as get instance of simulation (see above)
        // get an instance of Helper down from cloud system
        @Inject private Helper helper;

        public void doStep() {
            System.out.println( "entering " + this.getClass().getSimpleName() );
            helper.help();
            System.out.println( "leaving "+ this.getClass().getSimpleName() );
        }
    }
}