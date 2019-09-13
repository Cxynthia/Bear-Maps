package bearmaps.utils.graph;
import java.util.*;

import bearmaps.utils.pq.DoubleMapPQ;
import bearmaps.utils.pq.PriorityQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import edu.princeton.cs.algs4.Stopwatch;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {

    HashMap<Vertex, Double> dist = new HashMap<>();
    HashMap<Vertex, Vertex> edge = new HashMap<>();
    int numst = 0;
    private DoubleMapPQ<Vertex> fringe = new DoubleMapPQ<>();
    private Vertex init;
    private Vertex finish;

    double time;
    private AStarGraph<Vertex> input;
    private List<Vertex> solution;
    double t;

    /*
    Create a PQ where each vertex v will have priority value p equal to the sum of v’s distance from the source plus the heuristic estimate from v to the goal, i.e. p = distTo[v] + h(v, goal).
    Insert the source vertex into the PQ.
    Repeat while the PQ is not empty, PQ.peek() is not the goal, and timeout is not exceeded:
    p = PQ.poll()
    relax all edges outgoing from p
     */
    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        this.input = input;
        this.init = start;
        this.finish = end;
        this.time = timeout;
        Stopwatch sw = new Stopwatch();
        aStar();
        t = sw.elapsedTime();

    }

    private void aStar() {
        fringe.insert(init, 0);
        dist.put(init, 0.0);
        edge.put(init, null);
        Stopwatch sw = new Stopwatch();
        while (fringe.size() != 0 && sw.elapsedTime() < time) {
            if (!fringe.peek().equals(finish)) {
                Vertex p = fringe.poll();
                numst += 1;
                for (WeightedEdge<Vertex> e : input.neighbors(p)) {
                    relax(e);
                }
            } else {
                break;
            }
        }
    }

    private double h(Vertex v, Vertex goal) {
        return this.input.estimatedDistanceToGoal(v, goal);
    }

    private void relax(WeightedEdge<Vertex> e) {
        Vertex P = e.from();
        Vertex q = e.to();
        double w = e.weight();
        if (!dist.containsKey(q)) {
            dist.put(q, Double.POSITIVE_INFINITY);
        }
        if ((dist.get(P) + w) < (dist.get(q))) {
            dist.replace((q), dist.get(P) + w);
            edge.put(q, P);
            if (fringe.contains(q)) {
                fringe.changePriority(q, dist.get(q) + h(q, finish));
            } else if (!(fringe.contains(q))) {
                fringe.insert(q, dist.get(q) + h(q, finish));
            }
        }
    }

    public SolverOutcome outcome() {
        if (t < time) {
            return SolverOutcome.SOLVED;
        } else if (fringe.size() == 0) {
            return SolverOutcome.UNSOLVABLE;
        } else {
            return SolverOutcome.TIMEOUT;
        }
    }

    public List<Vertex> solution() {
        List<Vertex> v = new ArrayList<>();
        Vertex pointer = finish;

        while (!pointer.equals(init)) {
            v.add(pointer);
            pointer = edge.get(pointer);
        }
        v.add(pointer);
        Collections.reverse(v);
        return v;
    }

    public double solutionWeight() {
        return dist.get(finish);
    }

    public int numStatesExplored() {
        return numst;
    }

    public double explorationTime() {
        return t;
    }

}