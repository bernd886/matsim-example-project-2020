package org.matsim.project.SimulationGuiced.base;

public class HelperDefaultImpl implements Helper {
    public void help() {
        System.out.println( this.getClass().getSimpleName() + " is helping" );
    }
}
