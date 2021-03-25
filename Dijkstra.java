import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import javafx.util.Pair;

public class Dijkstra {

    protected GraphNode source;
    protected GraphNode destination;
    protected int nrNodes;
    protected int[] costCurent;
    protected GraphNode[] from;
    protected LinkedList<Edge>[] adjacencylist;
    protected Map<Integer, GraphNode> nodeCollection;

    Dijkstra(int nrNodes, GraphNode source, GraphNode destination,
             LinkedList<Edge>[] adjacencylist,
             Map<Integer, GraphNode> nodeCollection) {
        this.nrNodes = nrNodes;
        this.setSource(source);
        this.setDestination(destination);
        this.adjacencylist = adjacencylist;
        this.nodeCollection = nodeCollection;
    }

    public GraphNode getSource() {
        return source;
    }

    public void setSource(GraphNode source) {
        this.source = source;
    }

    public GraphNode getDestination() {
        return destination;
    }

    public void setDestination(GraphNode destination) {
        this.destination = destination;
    }

    public void algDijkstra() {
        costCurent = new int[nrNodes];
        from = new GraphNode[nrNodes];
        for (int index = 0; index < nrNodes; index++) {
            costCurent[index] = Integer.MAX_VALUE;
        }
       PriorityQueue<Pair<Long, Integer>> pq = new PriorityQueue<>(nrNodes, new Comparator<Pair<Long, Integer>>() {
            @Override
            public int compare(Pair<Long, Integer> p1, Pair<Long, Integer> p2) {
                long key1 = p1.getKey();
                long key2 = p2.getKey();
                return (int) (key1 - key2);
            }
        });
        Pair<Long, Integer> p0 = new Pair<Long, Integer>((long) 0, source.getId());
        costCurent[source.getId()] = 0;
        pq.add(p0);

        while (!pq.isEmpty()) {
            Pair<Long, Integer> extractedPair = pq.poll();
            int currentNode = extractedPair.getValue();
            if (currentNode == destination.getId())
                break;
            LinkedList<Edge> list = adjacencylist[currentNode];
            for (int index = 0; index < list.size(); index++) {
                Edge edge = list.get(index);
                long new_cost = costCurent[currentNode] + edge.getLength();
                if (new_cost < costCurent[edge.getEnd().getId()]) {
                    costCurent[edge.getEnd().getId()] = (int) new_cost;
                    from[edge.getEnd().getId()] = nodeCollection.get(currentNode);
                    Pair<Long, Integer> p = new Pair<>(new_cost, edge.getEnd().getId());
                    pq.add(p);
                }
            }
        }
    }

    public Stack<GraphNode> printPath() {
        Stack<GraphNode> stack = new Stack<GraphNode>();
        stack.push(destination);
        GraphNode currentNode = destination;
        while (source.getId() != currentNode.getId()) {
            currentNode = from[currentNode.getId()];
            stack.push(currentNode);
        }
        System.out.println("Cost = " + costCurent[destination.getId()]);
        return stack;
    }
}
