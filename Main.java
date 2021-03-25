import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    static int x;
    static int y;
    static JFrame f;

    public static void Resize(int x, int y) {
        f.setSize(x, y);
        x = f.getWidth();
        y = f.getHeight();
    }

    private static void createAndShowGui() {

        System.out.println("Timp inceput: "+ java.time.LocalTime.now());
        Graph graph = new Graph();
        System.out.println("Timp final: "+ java.time.LocalTime.now());
        f = new JFrame("Tema 5");
        f.setSize(1000, 1000);
        x = f.getWidth();
        y = f.getHeight();
        f.add(graph);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGui();
        });
    }
}