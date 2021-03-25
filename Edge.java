import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;

public class Edge {
    private GraphNode start;
    private GraphNode end;
    private Long length;

    public Edge(GraphNode start, GraphNode end, Long length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    public GraphNode getStart() {
        return start;
    }

    public void setStart(GraphNode start) {
        this.start = start;
    }

    public GraphNode getEnd() {
        return end;
    }

    public void setEnd(GraphNode end) {
        this.end = end;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public void drawEdge(Graphics g, int node_diam) {

        Graphics2D g2d = (Graphics2D) g;
        Shape line = new Line2D.Double(start.getScreenlongitude() + (node_diam / 2),
                start.getScreenlatitude() + (node_diam / 2), end.getScreenlongitude() + (node_diam / 2),
                end.getScreenlatitude() + (node_diam / 2));

        g2d.draw(line);

    }

}
