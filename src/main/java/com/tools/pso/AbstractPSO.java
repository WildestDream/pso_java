package com.tools.pso;

import com.tools.model.CompareType;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
import static com.tools.model.CompareType.MAX;

public abstract class AbstractPSO {

    @Setter
    @Getter
    private int iteration = 500;

    @Setter
    @Getter
    private int particleNum = 50;

    @Setter
    @Getter
    private CompareType COMPARE_TYPE = MAX;

    private final Random rand = new Random();

    private static final double W1 = 2.0;

    private static final double W2 = 2.0;

    private static final double PENALTY_FACTOR = Double.MAX_VALUE;

    private static final double DISTURBANCE = 10;

    public static final double W_START = 0.9;

    public static final double W_END = 0.2;

    private Particle bestParticle;

    private Particle[] particles;

    public abstract double[] initXs();

    public abstract double score(double[] xs);

    public Function<double[], Boolean> condition() {
        return (xs) -> true;
    }

    public Particle runAndGet() {
        initBestParticle();
        initParticles();
        updateGlobal();

        for (int i = 0; i < iteration; i++) {
            double factor = currentLDWFactor(i);
            for (Particle particle : particles) {
                particle.update(factor);
            }
            updateGlobal();
        }

        return bestParticle;
    }

    private void updateGlobal() {
        if (COMPARE_TYPE == MAX) {
            Particle particle = Arrays.stream(particles).max((o1, o2) -> (int) (o1.pBestScore - o2.pBestScore)).get();
            if (particle.pBestScore > bestParticle.pBestScore) {
                copyToBestParticle(particle);
            }
        } else {
            Particle particle = Arrays.stream(particles).min((o1, o2) -> (int) (o1.pBestScore - o2.pBestScore)).get();
            if (particle.pBestScore < bestParticle.pBestScore) {
                copyToBestParticle(particle);
            }
        }
    }

    private void copyToBestParticle(Particle particle) {
        System.arraycopy(particle.xs, 0, bestParticle.xs, 0, particle.xs.length);
        System.arraycopy(particle.vs, 0, bestParticle.vs, 0, particle.vs.length);
        System.arraycopy(particle.pBestXs, 0, bestParticle.pBestXs, 0, particle.pBestXs.length);
        bestParticle.score = particle.score;
        bestParticle.pBestScore = particle.score;
    }

    private void initBestParticle() {
        bestParticle = new Particle();
        bestParticle.init();
    }

    private void initParticles() {
        particles = new Particle[particleNum];
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle();
            particles[i].init();
        }
    }

    private double currentLDWFactor(int currentIter) {
        double tmp = Math.pow((double) currentIter / iteration, 2);
        return W_START - (W_START - W_END) * tmp;
    }

    public class Particle {

        private double[] xs;

        private double[] vs;

        private double score;

        private double[] pBestXs;

        @Getter
        private double pBestScore;

        private Function<double[], Boolean> condition;

        private int dimensionNum;

        private void init() {
            xs = initXs();
            if (xs == null || xs.length == 0) {
                throw new IllegalArgumentException("xs should not be empty");
            }
            //附加扰动
            for (int i = 0; i < xs.length; i++) {
                xs[i] += rand.nextDouble() * DISTURBANCE;
            }
            dimensionNum = xs.length;
            vs = new double[dimensionNum];
            condition = condition();
            updateScore();
            pBestXs = new double[dimensionNum];
            System.arraycopy(xs, 0, pBestXs, 0, dimensionNum);
            pBestScore = score;
        }

        public void update(double factor) {
            updateVsAndXs(factor);
            updateScore();
            updatePBestAndScore();
        }

        private void updateVsAndXs(double factor) {
            double[] gBestXs = bestParticle.xs;
            for (int i = 0; i < dimensionNum; i++) {
                vs[i] = vs[i] * factor + W1 * rand.nextDouble() * (pBestXs[i] - xs[i]) +
                        W2 * rand.nextDouble() * (gBestXs[i] - xs[i]);
                xs[i] = xs[i] + vs[i];
            }
        }

        private void updatePBestAndScore() {
            if (COMPARE_TYPE == MAX) {
                if (score > pBestScore) {
                    System.arraycopy(xs, 0, pBestXs, 0, dimensionNum);
                    pBestScore = score;
                }
            } else {
                if (score < pBestScore) {
                    System.arraycopy(xs, 0, pBestXs, 0, dimensionNum);
                    pBestScore = score;
                }
            }
        }

        private void updateScore() {
            score = score(xs);
            penalty();
        }

        private void penalty() {
            if (COMPARE_TYPE == MAX) {
                if (!condition.apply(xs)) {
                    score = -1 * PENALTY_FACTOR;
                }
            } else {
                if (!condition.apply(xs)) {
                    score = PENALTY_FACTOR;
                }
            }
        }


        @Override
        public String toString() {
            return "xs=" + Arrays.toString(pBestXs) + ", pBestScore=" + pBestScore;
        }
    }

}
