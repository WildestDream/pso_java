package com.tools.pso;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static com.tools.model.CompareType.MIN;

class AbstractPSOIllegalInputsTest {

    @Test
    void shouldThrowIllegalArgumentExceptionWhenXsIsEmpty() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return new double[] {};
            }

            @Override
            public double score(double[] xs) {
                return Math.pow(xs[0], 2) + Math.pow(xs[1], 2);
            }
        };

        pso.setCompareType(MIN);
        pso.setIteration(500);
        pso.setParticleNum(50);

        Assertions.assertThrows(IllegalArgumentException.class, pso::runAndGet);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenXsIsNull() {
        AbstractPSO pso = new AbstractPSO() {
            @Override
            public double[] initXs() {
                return null;
            }

            @Override
            public double score(double[] xs) {
                return Math.pow(xs[0], 2) + Math.pow(xs[1], 2);
            }
        };

        pso.setCompareType(MIN);
        pso.setIteration(500);
        pso.setParticleNum(50);

        Assertions.assertThrows(IllegalArgumentException.class, pso::runAndGet);
    }
}