# dijkstra-algorithm-implementation
A Java program which implements Dijkstra's Shortest Path Algorithm between multiple places.

// BALA GUNA TEJA KARLAPUDI

PROGRAM DESIGN: 

The program contains 6 classes: Edge, Vertex, minHeap, wGraph, Reachable and Graph (contains Main function)
Edge and Vertex classes are for storing vertex & edge details and to perform operations on them.
minHeap class is for bulding the graph as a Minimum Binary heap and perform Dijkstra using heap operations.
wGraph class has methods to perform operations on the Graph 
(print, addEdge, deleteEdge, EdgeDown, VertexDown, EdgeUp, VertexUp, Dijsktra etc.)
The respective methods are called from the Main function based on the commands in the queries file.

Several parts of the program are explained in the form of comments.

Reachable operation is performed based on the Breadth-First Search algorithm.
The running time of the reachable algorithm:
Initialization - O(V)
Running time of running BFS is O(V+E).
Since we have V vertex here, it takes O(V(V+E)) to perform Reachable operation
the same is mentioned in the program as comments.

The output of all these operations are written to a file.

-------------------------------------

BREAKDOWN OF THE UPLOADED FILES:

Graph.java
README.txt (This file)

-------------------------------------

HOW TO RUN:

The command line takes 3 files as arguments.
 
First file: contains list of edges between several vertices in the format <from vertex> <to vertex> <weight>

Second file: contains a list of queries/commands where each querie is written in a new line
commands format -
addedge <tailvertex> <headvertex> <transmit_time>
deleteedge <tailvertex> <headvertex>
edgedown <tailvertex> <headvertex>
edgeup <tailvertex> <headvertex> 
vertexdown <vertex>
vertexup <vertex>
path <source_vertex> <destination_vertex>
reachable 

Third file: Third file is an output file where the output of the queries of the previous file is written. You can check this file for output
after execution.

Execution command format - java Graph <first file> <second file> <third file>
Sample command - java Graph network.txt queries.txt output.txt

--------------------------------------

CODE SUMMARY: The code is well tested and all test cases should work properly.

--------------------------------------

PROGRAMMING LANGUAGE: Java (Version: 9.0.4)

--------------------------------------

DATA STRUCTURES USED: Hashmap, Linked List, Mininum Binary Heap & graphs 

--------------------------------------

COMPILER USED: IntelliJ IDEA (Version: 2017.3.4 x64)
