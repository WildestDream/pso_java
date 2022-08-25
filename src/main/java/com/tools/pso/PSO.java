package com.tools.pso;

public class PSO {
    public static void main(String[] args) {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {10000.0, -10000.0};
            }

            @Override
            public double score(double[] xs) {
                return xs[0] * xs[0] + xs[1] * xs[1];
            }
        };

        AbstractPSO.Particle result = pso.runAndGet();
        System.out.println(result);
    }


}
