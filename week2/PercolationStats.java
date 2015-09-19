import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Created by romster on 11.09.15.
 */
public class PercolationStats {

    private double[] thresholds;
    private int T;

    /**
     * perform T independent experiments on an N-by-N grid
     *
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (!((N > 0) && (T > 0))) {
            throw new IllegalArgumentException("N and T should be >= 0");
        }
        this.T = T;
        thresholds = new double[T];
        int totalCellsCount = N * N;
        for (int i = 0; i < T; i++) {
            Percolation p = new Percolation(N);
            int counter = 0;
            while (!p.percolates()) {
                int randI = StdRandom.uniform(N) + 1;
                int randJ = StdRandom.uniform(N) + 1;
                if (!p.isOpen(randI, randJ)) {
                    p.open(randI, randJ);
                    counter++;
                }
            }
            thresholds[i] = (double) counter / totalCellsCount;
        }
    }

    /**
     * sample mean of percolation threshold
     *
     * @return
     */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /**
     * sample standard deviation of percolation threshold
     *
     * @return
     */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /**
     * low  endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    /**
     * high endpoint of 95% confidence interval
     *
     * @return
     */
    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    public static void main(String[] args) {
        try {
            int N = Integer.parseInt(args[0]);
            int T = Integer.parseInt(args[1]);
            PercolationStats ps = new PercolationStats(N, T);
            StdOut.printf("%-23s = %f\n", "mean", ps.mean());
            StdOut.printf("%-23s = %f\n", "stddev", ps.stddev());
            StdOut.printf("%-23s = %f, %f\n", "95% confidence interval", ps.confidenceLo(), ps.confidenceHi());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
