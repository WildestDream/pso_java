package com.tools.test;

import com.tools.pso.AbstractPSO;
import static com.tools.pso.CompareType.MIN;

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

        pso.setCOMPARE_TYPE(MIN);
        pso.setIteration(1000);
        pso.setParticleNum(500);

        AbstractPSO.Particle result = pso.runAndGet();
        System.out.println(result);
    }


}
