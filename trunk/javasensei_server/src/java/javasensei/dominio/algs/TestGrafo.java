package javasensei.dominio.algs;

import javasensei.dominio.algs.BreadthFirstPaths;
import javasensei.dominio.algs.Graph;
import javasensei.dominio.algs.In;
import javasensei.dominio.algs.StdOut;

public class TestGrafo {
    public static void main(String[] args) {
        In in = new In("dominioJava.txt");
        Graph G = new Graph(in);
        /*StdOut.println(G);
        
        StdOut.println("*");
        StdOut.println("*");*/
        
        int s = 1;
        BreadthFirstPaths bfs = new BreadthFirstPaths(G, s);
        
        for (int v = 0; v < 3; v++) {
            if (bfs.hasPathTo(v)) {
                StdOut.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
                for (int x : bfs.pathTo(v)) {
                    if (x == s) StdOut.print(x);
                    else        StdOut.print("-" + x);
                }
                StdOut.println();
            }

            else {
                StdOut.printf("%d to %d (-):  not connected\n", s, v);
            }

        }
    }
}
