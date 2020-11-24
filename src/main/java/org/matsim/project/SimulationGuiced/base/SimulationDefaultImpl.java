package org.matsim.project.SimulationGuiced.base;

import com.google.inject.Inject;

public class SimulationDefaultImpl implements Simulation {
    // same as get instance of simulation (see above)
    // get an instance of Helper down from cloud system
    @Inject private Helper helper;

    public void doStep() {
        System.out.println( "entering " + this.getClass().getSimpleName() );
        helper.help();
        System.out.println( "leaving "+ this.getClass().getSimpleName() );
    }
}
