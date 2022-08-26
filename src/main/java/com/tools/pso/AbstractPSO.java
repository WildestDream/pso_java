package com.tools.pso;

import com.tools.model.CompareType;
import lombok.Getter;
import lombok.Setter;
import java.util.Arrays;
import java.util.Random;
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

    private Particle bestParticle;

    private Particle[] particles;

    public abstract double[] initXs();

    public abstract double score(double[] xs);

    public Particle runAndGet() {
        initBestParticle();
        initParticles();
        updateGlobal();

        for (int i = 0; i < iteration; i++) {
            for (Particle particle : particles) {
                particle.update(i);
            }
            updateGlobal();
            //System.out.println(i + "次迭代: " + bestParticle + "\n");
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

    public class Particle {

        private double[] xs;

        private double[] vs;

        private double score;

        private double[] pBestXs;

        @Getter
        private double pBestScore;

        private int dimensionNum;

        private void init() {
            xs = initXs();
            //附加扰动
            for (int i = 0; i < xs.length; i++) {
                xs[i] += rand.nextDouble() * 10;
            }
            dimensionNum = xs.length;
            vs = new double[dimensionNum];
            score = score(xs);
            pBestXs = new double[dimensionNum];
            System.arraycopy(xs, 0, pBestXs, 0, dimensionNum);
            pBestScore = score;
        }


        public void update(int iter) {
            updateVsAndXs(iter);
            updateScore();
            updatePBestAndScore();
        }

        private void updateVsAndXs(int iter) {
            double factor = (double) (iteration - iter) / iteration;
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
        }


        @Override
        public String toString() {
            return "xs=" + Arrays.toString(xs) +
                    ", pBestScore=" + pBestScore;
        }
    }

}
