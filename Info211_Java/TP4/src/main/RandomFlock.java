package main;

import processing.core.*;

import java.util.ArrayList;
import java.util.Random;

public class RandomFlock {
    private PApplet context;
    private ArrayList<RandomBoid> boids;

    public RandomFlock(int n, PApplet applet){
        context = applet;
        boids = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Random randGen = new Random();
            boids.add(new RandomBoid(context,randGen.nextInt(50) - 25, randGen.nextInt(50) - 25));
        }

    }

    public void addBoid(int x, int y){
        boids.add(new RandomBoid(context, x, y));
    }

    void run(){
        for (RandomBoid boid :
                boids) {
            boid.run();
        }
    }

}
