import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BellmanFord {

    protected GraphNode source;
    protected GraphNode destination;
    protected int nrNodes;
    protected int[] costCurent;
    protected GraphNode[] from;
    protected List<Edge> edgeCollection;
    protected Map<Integer, GraphNode> nodeCollection;

    BellmanFord(int nrNodes, GraphNode source, GraphNode destination, List<Edge> edgeCollection,
                Map<Integer, GraphNode> nodeCollection) {

        this.nrNodes = nrNodes;
        this.setSource(source);
        this.setDestination(destination);
        this.edgeCollection = edgeCollection;
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

    public void algBellmanFord() {

        costCurent = new int[nrNodes];
        from = new GraphNode[nrNodes];

        for (int index = 0; index < nrNodes; index++) {
            costCurent[index] = Integer.MAX_VALUE;
        }

        costCurent[source.getId()] = 0;
        boolean changed;
        for (int currentNode = 0; currentNode < nrNodes - 1; currentNode++) {
            changed = false;
            for (Edge edge : edgeCollection) {
                GraphNode nodeStart = edge.getStart();
                GraphNode nodeEnd = edge.getEnd();
                Long lungime = edge.getLength();

                if (costCurent[nodeStart.getId()] + lungime < costCurent[nodeEnd.getId()]) {
                    costCurent[nodeEnd.getId()] = (int) (costCurent[nodeStart.getId()] + lungime);
                    from[nodeEnd.getId()] = nodeStart;
                    changed = true;
                }
            }
            if (!changed)
                break;
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
