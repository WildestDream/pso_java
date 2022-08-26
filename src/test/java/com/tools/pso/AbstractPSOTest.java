package com.tools.pso;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.tools.model.CompareType.MAX;
import static com.tools.model.CompareType.MIN;

class AbstractPSOTest {

    @Test
    void testMin() {
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
        pso.setIteration(500);
        pso.setParticleNum(50);

        AbstractPSO.Particle result = pso.runAndGet();
        Assertions.assertTrue(Math.abs(result.getPBestScore() - 0) <= 0.1);
    }

    @Test
    void testMax() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {10000.0, -10000.0};
            }

            @Override
            public double score(double[] xs) {
                return -1 * (xs[0] * xs[0] + xs[1] * xs[1]);
            }
        };

        pso.setCOMPARE_TYPE(MAX);
        pso.setIteration(500);
        pso.setParticleNum(50);

        AbstractPSO.Particle result = pso.runAndGet();
        Assertions.assertTrue(Math.abs(result.getPBestScore() - 0) <= 0.1);
    }

    @Test
    void test4VariablesFunc() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {10.0, -10.0, -10.0, -10.0};
            }

            @Override
            public double score(double[] xs) {
                return 2 * Math.pow(xs[0] - 1, 2) + 7 * Math.pow(xs[1] - 3, 2) + 9 * Math.pow(xs[2] - 10, 4) +
                        19 * Math.pow(xs[3] - 19, 4) + 100;
            }
        };

        pso.setCOMPARE_TYPE(MIN);
        pso.setIteration(500);
        pso.setParticleNum(100);

        AbstractPSO.Particle result = pso.runAndGet();

        Assertions.assertTrue(Math.abs(result.getPBestScore() - 100) <= 0.1);

    }
}