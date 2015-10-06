import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by romster on 04.10.15.
 */
public class Solver {


    private Step goalStep;

    /**
     * find a solution to the initial board (using the A* algorithm)
     *
     * @param initial
     */
    public Solver(Board initial) {
        if (initial == null) throw new NullPointerException();
        MinPQ<Step> mpq = new MinPQ<>();
        MinPQ<Step> twin_mpq = new MinPQ<>();
        Step first = new Step(initial, 0, null);
        Step twin_first = new Step(initial.twin(), 0, null);
        mpq.insert(first);
        twin_mpq.insert(twin_first);
        while (true) {
            Step minPriorStep = mpq.delMin();
            Step twin_minPriorStep = twin_mpq.delMin();
            if (minPriorStep.board.isGoal()) {
                goalStep = minPriorStep;
                break;
            } else if (twin_minPriorStep.board.isGoal()) {
                break;
            } else {
                addNeighborsToPQ(mpq, minPriorStep);
                addNeighborsToPQ(twin_mpq, twin_minPriorStep);
            }
        }
    }

    /**
     * is the initial board solvable?
     *
     * @return
     */

    public boolean isSolvable() {
        return goalStep != null;
    }

    /**
     * min number of moves to solve initial board; -1 if unsolvable
     *
     * @return
     */
    public int moves() {
        if (isSolvable()) {
            return goalStep.moves;
        } else {
            return -1;
        }
    }

    /**
     * sequence of boards in a shortest solution; null if unsolvable
     *
     * @return
     */
    public Iterable<Board> solution() {
        if (isSolvable()) {
            ArrayList<Board> result = new ArrayList<>(goalStep.moves + 1);
            Step step = goalStep;
            do {
                result.add(step.board);
                step = step.previous;
            } while (step != null);
            Collections.reverse(result);
            return result;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }


    private void addNeighborsToPQ(MinPQ<Step> pq, Step currStep) {
        if (currStep.previous == null) {
            currStep.board.neighbors().forEach(
                    a -> pq.insert(new Step(a, currStep.moves + 1, currStep)));
        } else {
            Iterable<Board> neighbors = currStep.board.neighbors();
            Iterator<Board> iterator = neighbors.iterator();
            while (iterator.hasNext()) {
                Board b = iterator.next();
                boolean isLast = !iterator.hasNext();
//                if (!isLast || !b.equals(currStep.previous.board)) {
//                    pq.insert(new Step(b, currStep.moves + 1, currStep));
//                }
                if (checkParents(b, currStep)) {
                    pq.insert(new Step(b, currStep.moves + 1, currStep));
                }

            }
        }
    }

    private boolean checkParents(Board b, Step currStep) {
        if (currStep.previous == null) return true;
        Board parent = currStep.previous.board;
        if (parent.equals(b)) return false;
        if (currStep.previous.previous != null && currStep.previous.previous.previous != null) {
            Board parentParent = currStep.previous.previous.previous.board;
            if (parentParent.equals(b)) return false;
        }
        return true;
    }

    private class Step implements Comparable<Step> {
        private final Board board;
        private final int moves;
        private final Step previous;
        private final int priority;

        public Step(Board b, int moves, Step previous) {
            this.board = b;
            this.moves = moves;
            this.previous = previous;
            this.priority = b.manhattan() + moves;
        }

        @Override
        public int compareTo(Step o) {
            return this.priority - o.priority;
        }
    }

}
