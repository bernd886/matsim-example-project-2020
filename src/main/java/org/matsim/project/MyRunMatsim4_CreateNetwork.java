package org.matsim.project;


import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkFactory;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.NetworkWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyRunMatsim4_CreateNetwork {

    // CAPACITY at links that all agents use
    private static final long CAP_FIRST_LAST = 3600; // [veh/h]
    // capacity at all other links
    private static final long CAP_MAIN = 1800;

    // link LENGTH for all links
    private static final long LINK_LENGTH = 200; // [m]

    // travel TIME for middle link
    private static final double LINK_TT_MID = 60; // [s]
    // travel time for the middle route links
    private static final double LINK_TT_SMALL = 60;
    // travel time for the two remaining outer route links (choose at least
    // 3*LINK_TT_SMALL!)
    private static final double LINK_TT_BIG = 10 * 60;
    // travel time for links that all agents have to use
    private static final double MINIMAL_LINK_TT = 1;


    private static void setLinkAttributes(Link link, double capacity, double length, double travelTime) {
        link.setCapacity(capacity);
        link.setLength(length);
        // agents have to reach the end of the link before the time step ends to
        // be able to travel forward in the next time step (matsim time step logic)
        link.setFreespeed(link.getLength() / (travelTime - 0.1));
    }


    public static void main(String[] args) throws IOException {

        // create empty network
        Network network = NetworkUtils.createNetwork();
        NetworkFactory networkFactory = network.getFactory();

        // create nodes
        Node n0 = networkFactory.createNode(Id.createNodeId(0), new Coord(-200, 200));
        network.addNode(n0);
        Node n1 = networkFactory.createNode(Id.createNodeId(1), new Coord(0,200));
        network.addNode(n1);
        Node n2 = networkFactory.createNode(Id.createNodeId(2), new Coord(200,200));
        network.addNode(n2);
        Node n3 = networkFactory.createNode(Id.createNodeId(3), new Coord(400, 400));
        network.addNode(n3);
        Node n4 = networkFactory.createNode(Id.createNodeId(4), new Coord(400, 0));
        network.addNode(n4);
        Node n5 = networkFactory.createNode(Id.createNodeId(5), new Coord(600, 200));
        network.addNode(n5);
        Node n6 = networkFactory.createNode(Id.createNodeId(6), new Coord(800, 200));
        network.addNode(n6);

        // create links
        Link linkToAdd = networkFactory.createLink(Id.createLinkId("0_1"), n0, n1);
        setLinkAttributes(linkToAdd, CAP_FIRST_LAST, LINK_LENGTH, MINIMAL_LINK_TT);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("1_2"), n1, n2);
        setLinkAttributes(linkToAdd, CAP_FIRST_LAST, LINK_LENGTH, MINIMAL_LINK_TT);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("2_3"), n2, n3);
        setLinkAttributes(linkToAdd, CAP_MAIN, LINK_LENGTH, LINK_TT_SMALL);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("2_4"), n2, n4);
        setLinkAttributes(linkToAdd, CAP_MAIN, LINK_LENGTH, LINK_TT_BIG);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("3_4"), n3, n4);
        setLinkAttributes(linkToAdd, CAP_MAIN, LINK_LENGTH, LINK_TT_MID);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("3_5"), n3, n5);
        setLinkAttributes(linkToAdd, CAP_MAIN, LINK_LENGTH, LINK_TT_BIG);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("4_5"), n4, n5);
        setLinkAttributes(linkToAdd, CAP_MAIN, LINK_LENGTH, LINK_TT_SMALL);
        network.addLink(linkToAdd);
        linkToAdd = networkFactory.createLink(Id.createLinkId("5_6"), n5, n6);
        setLinkAttributes(linkToAdd, CAP_FIRST_LAST, LINK_LENGTH, MINIMAL_LINK_TT);
        network.addLink(linkToAdd);

        // create outputFolder if necessary
        Path outputFolder = Files.createDirectories(Paths.get("minimalNetworkCreation"));

        // write network
        new NetworkWriter(network).write(outputFolder.resolve("network.xml").toString());

    }
}
