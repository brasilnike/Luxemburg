import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Graph extends JPanel {

    private static final int NODE_DIAM = 2;

    private Map<Integer, GraphNode> nodeCollection = new HashMap<Integer, GraphNode>();
    private List<Edge> edgeCollection = new ArrayList<Edge>();
    LinkedList<Edge>[] adjacencyList;
    Stack<GraphNode> pathNodes = new Stack<GraphNode>();
    private int nrNodes;

    public Long max_lat = Long.MIN_VALUE;
    public Long max_long = Long.MIN_VALUE;
    public Long min_long = Long.MAX_VALUE;
    public Long min_lat = Long.MAX_VALUE;

    public int nrClicks = 0;
    public GraphNode nodeStart;
    public GraphNode nodeEnd;
    private Scanner sc;

    Graph() {
        sc = new Scanner(System.in);
        readFromFile();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e))
                    switch (nrClicks) {
                        case 0:
                            nodeStart = searchNode(e.getPoint());
                            ++nrClicks;
                            System.out.println("Source node= " + nodeStart.getId());
                            repaint();
                            break;
                        case 1:
                            nodeEnd = searchNode(e.getPoint());
                            ++nrClicks;
                            System.out.println("Destination node= " + nodeEnd.getId());
                            repaint();
                            break;
                        case 2:
                            nrClicks = 0;
                            chooseAlgorithm();
                            repaint();
                            break;
                    }

            }
        });

    }

    private void chooseAlgorithm() {
        System.out.println("Alege algoritmul:" + " \n 1 : Dijkstra" + "\n 2 : Bellman Ford ");
        int alg = sc.nextInt();
        switch (alg) {
            case 1:
                Dijkstra algorithm = new Dijkstra(nrNodes, nodeStart, nodeEnd, adjacencyList, nodeCollection);
                algorithm.algDijkstra();
                pathNodes = algorithm.printPath();
                break;
            case 2:
                BellmanFord algorithm1 = new BellmanFord(nrNodes, nodeStart, nodeEnd, edgeCollection, nodeCollection);
                algorithm1.algBellmanFord();
                pathNodes = algorithm1.printPath();
                break;
            default:
                System.out.println("Numar ales gresit!");
                break;
        }
    }

    private GraphNode searchNode(Point nod) {

        GraphNode closest_node = null;
        double nearest_dist = Integer.MAX_VALUE;
        for (GraphNode node : nodeCollection.values()) {
            double distance = distance(nod.getX(), nod.getY(), node.getLongitude(), node.getLatitude());
            if (distance < nearest_dist) {
                nearest_dist = distance;
                closest_node = node;
            }
        }
        return closest_node;

    }

    private double distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return dx * dx + dy * dy;
    }

    public void addEdge(int from, int to, Long length) {
        Edge edge = new Edge(nodeCollection.get(from), nodeCollection.get(to), length);
        adjacencyList[from].addFirst(edge);

        edge = new Edge(nodeCollection.get(to), nodeCollection.get(from), length);
        adjacencyList[to].addFirst(edge); //graf neorientat
    }

    @SuppressWarnings("unchecked")
    public void readFromFile() {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = parserFactory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e1) {
            e1.printStackTrace();
        }

        DefaultHandler parserHandler = new DefaultHandler() {

            public void startElement(String uri, String localName, String name, Attributes attributes) {

                if (name.equals("node")) {
                    // ++nrNodes;

                    int length = attributes.getLength();
                    int id = 0;
                    Long longitude = null, latitude = null;

                    for (int i = 0; i < length; ++i) {

                        String attributeName = attributes.getLocalName(i);

                        if (attributeName.equals("id")) {
                            String idString = attributes.getValue(i);
                            id = Integer.parseInt(idString);

                        }

                        if (attributeName.equals("longitude")) {
                            String longitudeString = attributes.getValue(i);
                            longitude = Long.parseLong(longitudeString);
                            if (longitude > max_long)
                                max_long = longitude;
                            if (longitude < min_long)
                                min_long = longitude;

                        }

                        if (attributeName.equals("latitude")) {
                            String latitudeString = attributes.getValue(i);
                            latitude = Long.parseLong(latitudeString);

                            if (latitude > max_lat)
                                max_lat = latitude;
                            if (latitude < min_lat)
                                min_lat = latitude;

                        }

                    }
                    nodeCollection.put(id, new GraphNode(id, longitude, latitude));
                }
                else {
                    if (adjacencyList == null || adjacencyList.length == 0) {
                        adjacencyList = new LinkedList[nodeCollection.size()];
                        for (int i = 0; i < adjacencyList.length; ++i) {
                            adjacencyList[i] = new LinkedList<Edge>();
                        }
                    }
                }

                if (name.equals("arc")) {

                    int length = attributes.getLength();
                    int from = 0, to = 0;
                    Long arcLength = null;

                    for (int index = 0; index < length; ++index) {

                        String attributeName = attributes.getLocalName(index);

                        if (attributeName.equals("from")) {
                            String fromString = attributes.getValue(index);
                            from = Integer.parseInt(fromString);

                        }

                        if (attributeName.equals("to")) {
                            String toString = attributes.getValue(index);
                            to = Integer.parseInt(toString);

                        }

                        if (attributeName.equals("length")) {
                            String lengthString = attributes.getValue(index);
                            arcLength = Long.parseLong(lengthString);

                        }

                    }
                    edgeCollection.add(new Edge(nodeCollection.get(from), nodeCollection.get(to), arcLength));
                    addEdge(from, to, arcLength);

                }
            }

        };

        try {
            saxParser.parse("src/map2.xml", parserHandler);
            nrNodes = nodeCollection.size();
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        for (GraphNode node : nodeCollection.values()) {
            node.drawNode(g, max_long, max_lat, min_long, min_lat, NODE_DIAM);

        }

        for (Edge edge : edgeCollection) {
            edge.drawEdge(g, NODE_DIAM);
        }

        Graphics2D g2d = (Graphics2D) g;

        if (nodeStart != null) {
            g2d.setColor(Color.RED);
            Shape circle = new Ellipse2D.Double(nodeStart.getLongitude() - 5, nodeStart.getLatitude() - 5, 10, 10);
            g2d.draw(circle);
            g2d.fill(circle);
        }
        if (nodeEnd != null) {
            Shape circle = new Ellipse2D.Double(nodeEnd.getLongitude() - 5, nodeEnd.getLatitude() - 5, 10, 10);
            g2d.draw(circle);
            g2d.fill(circle);
        }

        g2d.setStroke(new java.awt.BasicStroke(3));
        if (!pathNodes.empty()) {
            g2d.setColor(Color.RED);
            while (!pathNodes.empty()) {
                GraphNode start = pathNodes.peek();
                pathNodes.pop();
                if (!pathNodes.empty()) {
                    GraphNode end = pathNodes.peek();
                    Shape line = new Line2D.Double(start.getScreenlongitude() + NODE_DIAM / 2,
                            start.getScreenlatitude() + NODE_DIAM / 2, end.getScreenlongitude() + NODE_DIAM / 2,
                            end.getScreenlatitude() + NODE_DIAM / 2);
                    g2d.draw(line);
                }
            }
        }
    }
}
