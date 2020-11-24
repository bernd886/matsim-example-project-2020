package org.matsim.project.SimulationGuiced.alternative;

import org.matsim.project.SimulationGuiced.base.Helper;

public class HelperMyImpl implements Helper {
    @Override
    public void help() {
        System.out.println( this.getClass().getSimpleName() + " is helping better" );
    }
}