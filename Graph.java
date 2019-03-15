import java.io.FileReader;
import java.io.PrintStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

class Edge {
    public Vertex from, to;
    public double weight;
    public String status = "";                     //For storing the status an edge or a vertex

    public Edge(Vertex from, Vertex to, double weight) {
        super();
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

class Vertex {
    public String name;
    public LinkedList<Edge> adjacentEdges;         // For Storing Adjacent vertices
    public Vertex prev;                            // To save Previous vertex on the shortest path
    public double dist;                            // Weight of the path
    public String status = "";
    public int heapIndex;                          // to maintain the current position in the Binary Heap
    public String color;

    public Vertex(String nm) {
        name = nm;
        adjacentEdges = new LinkedList<>();
        reset();
    }

    public void reset() {
        dist = wGraph.INFINITY;
        prev = null;
        color = "White";
    }
}

class minHeap {
    private ArrayList<Vertex> sQueue;

    public minHeap(ArrayList<Vertex> ar) {
        this.sQueue = ar;
        build(this.sQueue, sQueue.size());
    }

    private void build(ArrayList<Vertex> unsQueue, int n) {                 //For Building the heap

        for (int i = (n / 2) - 1; i >= 0; i--) {
            minHeapify(unsQueue, i, n);
        }
    }

    private void minHeapify(ArrayList<Vertex> unsQueue, int i, int n) {     //Min Heapify
        int l, r;
        l = left(i);
        r = right(i);
        int smallest;
        if (l <= n - 1) {
            if (getDist(unsQueue.get(l)) < getDist(unsQueue.get(i))) {
                smallest = l;
                unsQueue.get(l).heapIndex = i;
                unsQueue.get(i).heapIndex = l;
            } else {
                unsQueue.get(l).heapIndex = l;
                unsQueue.get(i).heapIndex = i;
                smallest = i;
            }
        } else {
            unsQueue.get(i).heapIndex = i;
            smallest = i;
        }
        if (r <= n - 1) {
            if (getDist(unsQueue.get(r)) < getDist(unsQueue.get(smallest))) {
                unsQueue.get(r).heapIndex = i;
                unsQueue.get(i).heapIndex = r;
                smallest = r;
            } else {
                unsQueue.get(r).heapIndex = r;
                unsQueue.get(i).heapIndex = i;
            }
        }
        if (smallest != i) {
            Vertex temp = unsQueue.get(i);
            unsQueue.set(i, unsQueue.get(smallest));
            unsQueue.set(smallest, temp);
            minHeapify(unsQueue, smallest, n);
        }
    }

    public Vertex extractMin(ArrayList<Vertex> sQueue, int n) {             // ExtractMin Operation
        if (n < 1) {
            System.out.println("Heap underflow...");
            return null;
        }
        Vertex min = sQueue.get(0);
        sQueue.set(0, sQueue.get(n - 1));
        sQueue.remove(n - 1);
        n = n - 1;
        if (n > 0) {
            minHeapify(sQueue, 0, n);
        }
        return min;
    }

    public void decreaseKey(int i, double key) {       // To Decrease the Priority
        if (sQueue.get(i).dist < key) {
            return;
        }
        sQueue.get(i).dist = key;
        while (i > 0 && sQueue.get(parent(i)).dist > sQueue.get(i).dist) {
            sQueue.get(parent(i)).heapIndex = i;       // Changing heap index
            sQueue.get(i).heapIndex = parent(i);

            Vertex temp = sQueue.get(i);
            sQueue.set(i, sQueue.get(parent(i)));
            sQueue.set(parent(i), temp);
            i = parent(i);
        }
    }

    private int parent(int i) {                         // Get the Parent of a node
        return (i - 1) / 2;
    }

    private int left(int i) {                           // Get the left child of the node
        return 2 * i + 1;
    }

    private int right(int i) {                          // Get the right child of the node
        return 2 * i + 2;
    }

    public boolean isEmpty() {                          // Check whether the heap is empty
        return sQueue.isEmpty();
    }

    public Vertex extractMin() {
        return extractMin(sQueue, sQueue.size());
    }

    private double getDist(Vertex v) {
        return v.dist;
    }
}

@SuppressWarnings("unchecked")
class wGraph {
    public static final double INFINITY = Double.MAX_VALUE;
    protected Map<String, Vertex> vertexMap = new HashMap<>();

    // To build the wGraph (adds two directed edges i.e. from source to destination and vise versa
    public void addEdges(String sourceName, String destName, double weight) {
        Vertex u = getVertex(sourceName);
        Vertex v = getVertex(destName);
        Edge uv = new Edge(u, v, weight);
        Edge vu = new Edge(v, u, weight);
        u.adjacentEdges.add(uv);
        v.adjacentEdges.add(vu);
    }

    //To add adge between two vertices
    public void addEdge(String source, String dest, double weight) {
        Boolean updated = false;
        if (vertexMap.get(source) != null && vertexMap.get(dest) != null) {
            Vertex u = getVertex(source);
            Vertex v = getVertex(dest);
            for (Edge edge : u.adjacentEdges) {
                if (edge.to.name.equals(dest)) {  // to update weight if the edge already exists
                    edge.weight = weight;
                    updated = true;
                }
            }
            if (!updated) {
                Edge uv = new Edge(u, v, weight);
                u.adjacentEdges.add(uv);
            }
        } else {
            Vertex u = getVertex(source);
            Vertex v = getVertex(dest);
            Edge uv = new Edge(u, v, weight);
            u.adjacentEdges.add(uv);
        }
    }

    // To delete an edge between two vertices
    public void deleteEdge(String sourceVertex, String destVertex) {
        if (vertexMap.get(sourceVertex) != null
                && vertexMap.get(destVertex) != null) {
            Vertex u = getVertex(sourceVertex);
            for (Edge edge : u.adjacentEdges) {
                if (edge.to.name.equals(destVertex)) {
                    u.adjacentEdges.remove(edge);
                    break;
                }
            }
        }
    }

    //To temporarily disable an edge
    public void edgeDown(String sourceVertex, String destVertex) {
        if (vertexMap.get(sourceVertex) != null
                && vertexMap.get(destVertex) != null) {
            Vertex u = getVertex(sourceVertex);
            for (Edge edge : u.adjacentEdges) {
                if (edge.to.name.equals(destVertex)) {
                    edge.status = "DOWN";
                }
            }
        }
    }

    //To enable a temporarily disabled edge
    public void edgeUp(String sourceVertex, String destVertex) {
        if (vertexMap.get(sourceVertex) != null
                && vertexMap.get(destVertex) != null) {
            Vertex u = getVertex(sourceVertex);
            for (Edge edge : u.adjacentEdges) {
                if (edge.to.name.equals(destVertex)) {
                    edge.status = "";
                }
            }
        }
    }

    //To temporarily disable a vertex
    public void vertexDown(String vertex) {
        if (vertexMap.get(vertex) != null) {
            Vertex v = getVertex(vertex);
            v.status = "DOWN";
        }
    }

    // To enable a disabled vertex
    public void vertexUp(String vertex) {
        if (vertexMap.get(vertex) != null) {
            Vertex v = getVertex(vertex);
            v.status = "";
        }
    }

    private Vertex getVertex(String vertexName) {
        Vertex w = vertexMap.get(vertexName);
        if (w == null) {
            w = new Vertex(vertexName);
            vertexMap.put(vertexName, w);
        }
        return w;
    }

    //To show the wGraph with all the edges and vertices
    public void printwGraph() {
        ArrayList<String> sortedVertex = new ArrayList<>(vertexMap.keySet());
        Collections.sort(sortedVertex);				//To sort vertices in alphabetical order for displaying

        for (String key : sortedVertex) {
            Vertex w = vertexMap.get(key);
            System.out.println(w.name + " " + w.status);
            ArrayList<String> adjList = new ArrayList<>();
            for (Edge edge : w.adjacentEdges) {
                adjList.add(edge.to.name + " " + edge.weight + " " + edge.status);
            }
            Collections.sort(adjList);                      
            for (String neighbor : adjList) {
                System.out.println("  " + neighbor);
            }
        }
    }


    public void dijkstra(String startName) {                	//dijkstra algorithm implementation

        for (Vertex v : vertexMap.values()) {               	//Initialization
            v.dist = INFINITY;
            v.prev = null;
        }
        Vertex start = vertexMap.get(startName);
        if (start == null) {
            System.out.println("Start vertex not found!");
            return;
        }
        if (start.status.equals("DOWN")) {
            System.out.println("Start vertex is DOWN!");
            return;
        }

        start.dist = 0.0;
        minHeap pq = new minHeap(new ArrayList(vertexMap.values()));

        while (!pq.isEmpty()) {
            Vertex u = pq.extractMin();

            for (Edge edge : u.adjacentEdges) {
                if (!edge.to.status.equals("DOWN") && !edge.status.equals("DOWN") && u.dist != INFINITY) {
                    double temp = u.dist + edge.weight;
                    temp = (double) Math.round(temp * 100) / 100;
                    if (temp < edge.to.dist) {
                        edge.to.dist = temp;
                        edge.to.prev = u;
                        pq.decreaseKey(edge.to.heapIndex, temp);              //To Decrease the Priority
                    }
                }
            }
        }
    }

    //Checks if Vertex is available & reachable and Calls printShortestPath
    public void printPath(String destName) {
        Vertex w = vertexMap.get(destName);
        if (w == null)
            System.out.println("Destination vertex not found");
        else if (w.dist == INFINITY)
            System.out.println(destName + " is unReachable");
        else {
            printShortestPath(w);
            System.out.print(" " + w.dist);
            System.out.println();
        }
    }

    //To print shortest path between two vertices
    private void printShortestPath(Vertex end) {
        if (end.prev != null) {
            printShortestPath(end.prev);
            System.out.print(" ");
        }
        System.out.print(end.name);
    }
}

class Reachable {                                       //For Finding Reachable vertices through BFS (Breadth First Search)
    public static void reachableVertices(wGraph g) {

        ArrayList<String> vertexList = new ArrayList<>(g.vertexMap.keySet());
        Collections.sort(vertexList);

        for (String vName : vertexList) {               // Initialization - O(V) times
            resetVertices(g);
            Vertex s = g.vertexMap.get(vName);
            if (s.status.equals("DOWN")) {
                continue;
            }
            s.color = "Grey";
            s.dist = 0.0;
            s.prev = null;

            Queue<Vertex> q = new LinkedList<>();
            q.add(s);

            while (!q.isEmpty()) {
                Vertex v = q.remove();

                for (Edge e : v.adjacentEdges) {
                    if (!e.to.status.equals("DOWN") && !e.status.equals("DOWN")
                            && e.to.dist == wGraph.INFINITY
                            && e.to.color.equals("White")) {

                        e.to.color = "Grey";
                        e.to.dist = v.dist + 1.0;
                        e.to.prev = v;
                        q.add(e.to);
                    }
                }
                v.color = "Black";
            }
            // Running time of running BFS is O(V+E).
            // Since we have V vertex here, it takes O(V(V+E)) to perform Reachable operation
            System.out.println(vName);
            displayReachable(vertexList, g, vName);
        }
    }

    private static void displayReachable(ArrayList<String> vertexList, wGraph g, String source) {
        for (String vName : vertexList) {
            if (g.vertexMap.get(vName).color.equals("Black") && !vName.equals(source)) {
                System.out.println("  " + vName);
            }
        }
    }

    private static void resetVertices(wGraph g) {            //Running time of O(V)
        for (Vertex v : g.vertexMap.values()) {
            v.color = "White";
            v.dist = wGraph.INFINITY;
            v.prev = null;
        }
    }
}

class Graph {
    public static void main(String[] args) {
        wGraph g = new wGraph();
        try {
            FileReader f = new FileReader(args[0]);
            FileReader q = new FileReader(args[1]);
            PrintStream writeOutput = new PrintStream(args[2]);
            System.setOut(writeOutput);                             //To write output to a file
            Scanner wGraphFile = new Scanner(f);
            Scanner queries = new Scanner(q);

            // To Read the edges and build the wGraph
            while (wGraphFile.hasNextLine()) {
                String[] values = wGraphFile.nextLine().split(" ");
                String source = values[0];
                String dest = values[1];
                double weight = Double.parseDouble(values[2]);
                g.addEdges(source, dest, weight);
            }

            //To read the queries and perform actions accordingly
            while (queries.hasNextLine()) {
                String[] cmd = queries.nextLine().split(" ");
                if (cmd[0].equals("print")) {
                    g.printwGraph();
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("addedge")) {
                    g.addEdge(cmd[1], cmd[2], Double.parseDouble(cmd[3]));
                    System.out.println("Edge " + cmd[1] + "->" + cmd[2] + " is Added!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("deleteedge")) {
                    g.deleteEdge(cmd[1], cmd[2]);
                    System.out.println("Edge " + cmd[1] + "->" + cmd[2] + " is Deleted!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("edgedown")) {
                    g.edgeDown(cmd[1], cmd[2]);
                    System.out.println("Edge " + cmd[1] + "->" + cmd[2] + " is Down!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("edgeup")) {
                    g.edgeUp(cmd[1], cmd[2]);
                    System.out.println("Edge " + cmd[1] + "->" + cmd[2] + " is now up!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("vertexdown")) {
                    g.vertexDown(cmd[1]);
                    System.out.println("Vertex " + cmd[1] + " is down!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("vertexup")) {
                    g.vertexUp(cmd[1]);
                    System.out.println("Vertex " + cmd[1] + " is now up!");
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("path")) {
                    g.dijkstra(cmd[1]);
                    g.printPath(cmd[2]);
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("Reachable") || cmd[0].equals("reachable")) {
                    System.out.println("REACHABLE VERTICES:\n\n");
                    Reachable.reachableVertices(g);
                    System.out.println("\n********************\n");
                } else if (cmd[0].equals("exit")) {
                    System.out.println("Exiting...");
                    System.exit(0);
                } else
                    System.out.println("Invalid Command!");
            }
            f.close();
            q.close();
            writeOutput.close();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Invalid Command!");
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}


