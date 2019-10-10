package main;

import processing.core.*;

import java.util.ArrayList;
import java.util.Random;

public class Flock {
    private PApplet context;
    private ArrayList<Boid> boids;

    public Flock(int n, PApplet applet){
        context = applet;
        boids = new ArrayList<>();

        Random randGen = new Random();

        for (int i = 0; i < n; i++) {
            int x = randGen.nextInt(50) - 25;
            int y = randGen.nextInt(50) - 25;
            System.out.println(x + " " + y);
            boids.add(new Boid(context,x, y, this));
        }

    }

    public void addBoid(int x, int y){
        boids.add(new Boid(context, x, y, this));
    }

    public ArrayList<Boid> neighbors(Boid b, double dist){
        ArrayList<Boid> res = new ArrayList<>();

        for (Boid boid :
                boids) {
            if(b.distance(boid) < dist && b.distance(boid) != 0){
                res.add(boid);
            }
        }

        return res;
    }

    void run(){
        for (Boid boid :
                boids) {
            boid.run();
        }
    }

}
