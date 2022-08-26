package com.tools.pso;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.function.Function;
import static com.tools.model.CompareType.MAX;
import static com.tools.model.CompareType.MIN;

class AbstractPSOConditionTest {

    @Test
    void testMaxWithCondition() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {50, 150};
            }

            @Override
            public double score(double[] xs) {
                return Math.pow(xs[0], 2) + Math.pow(xs[1], 2);
            }

            @Override
            public Function<double[], Boolean> condition() {
                return xs -> xs[0] >= 0 && xs[0] <= 100 && //0 - 100
                        xs[1] >= 100 && xs[1] <= 200;   //100 - 200
            }
        };

        pso.setCOMPARE_TYPE(MAX);
        pso.setIteration(500);
        pso.setParticleNum(200);

        AbstractPSO.Particle result = pso.runAndGet();
        Assertions.assertTrue(Math.abs(result.getPBestScore() - 50000.0) <= 0.1);
    }

    @Test
    void testMinWithCondition() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {50, 150};
            }

            @Override
            public double score(double[] xs) {
                return Math.pow(xs[0], 2) + Math.pow(xs[1], 2);
            }

            @Override
            public Function<double[], Boolean> condition() {
                return xs -> xs[0] >= 0 && xs[0] <= 100 && //0 - 100
                        xs[1] >= 100 && xs[1] <= 200;   //100 - 200
            }
        };

        pso.setCOMPARE_TYPE(MIN);
        pso.setIteration(500);
        pso.setParticleNum(200);

        AbstractPSO.Particle result = pso.runAndGet();
        System.out.println(result.getPBestScore());
        Assertions.assertTrue(Math.abs(result.getPBestScore() - 10000.0) <= 0.1);
    }
}