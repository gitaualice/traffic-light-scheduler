package TrafficFlow.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Graph<T> {
    private Map<T, Node<T>> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public void addNode(T data) {
        // Node<T> node = new Node<>(data);
        // nodes.put(data, node);
        nodes.put(data, new Node<>(data));
    }

    public void addEdge(T node1, T node2) {
        Node<T> n1 = nodes.get(node1);
        Node<T> n2 = nodes.get(node2);
        if (n1 == null || n2 == null) {
            throw new IllegalArgumentException("Nodes must be added to the graph before adding edges.");
        }
        n1.addNeighbor(n2);
        n2.addNeighbor(n1);
    }

    public Set<T> getNodes() {
        return nodes.keySet();
    }

    public int greedyColoring() {
        clearStates();

        Queue<Node<T>> unlabeled = new LinkedList<>();
        for (Node<T> n : nodes.values()) {
            unlabeled.add(n);
        }

        int color = 1;
        while (!unlabeled.isEmpty()) {
            greedy(color, unlabeled);
            color++;
        }
        return color - 1;
    }

    public void greedy(int color, Queue<Node<T>> unlabeled) {
        for (int i = unlabeled.size(); i > 0; i--) {
            Node<T> n = unlabeled.remove();
            if (!n.canColor(color)) {
                unlabeled.add(n);
            }
        }
    }

    public List<T> getColor(int color) {
        List<T> coloredNodes = new ArrayList<>();
        for (Node<T> node : nodes.values()) {
            if (node.getState() == color) {
                coloredNodes.add(node.getData());
            }
        }
        return coloredNodes;
    }

    private void clearStates() {
        for (Node<T> node : nodes.values()) {
            node.clearState();
        }
    }
}
