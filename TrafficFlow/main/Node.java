package TrafficFlow.main;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    private T data;
    private List<Node<T>> neighbors;
    private int state;

    public Node(T data) {
        this.data = data;
        this.neighbors = new ArrayList<>();
        this.state = 0;
    }

    public T getData() {
        return data;
    }

    public List<Node<T>> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Node<T> neighbor) {
        neighbors.add(neighbor);
    }

    public List<String> getEdges() {
        List<String> edges = new ArrayList<>();
        for (Node<T> neighbor : neighbors) {
            edges.add(data + "-" + neighbor.getData());
        }
        return edges;
    }

    public int getState(){
        return state;
    }

    public void setState(int state){
        this.state = state;
    }
    public boolean canColor(int color){
    //loops though all neighbors 
    for( Node<T> n : neighbors){
        //if neighbot has the same color, return false
        if(n.state == color){
            return false;
        }
    }
    // no conflicts found assign the color to the node 
    this.state = color;
    return true;
}

    public void clearState() {
        this.state = 0;
    }
   
}

    

