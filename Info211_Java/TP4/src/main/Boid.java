package main;

import processing.core.*;

import java.util.ArrayList;
import java.util.Random;

public class Boid {
    private PVector pos;
    private PVector vel;
    private float maxSpeed = 2;
    private float forceMax = 0.05f;
    private Random randGen = new Random();
    private PApplet context;
    private Flock flock;

    public Boid(int x, int y){
        pos = new PVector(x, y);
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
    }

    public Boid(PApplet context, int x, int y, Flock flock){
        this.flock = flock;
        pos = new PVector(x, y);
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
        this.context = context;
    }

    /*
    public Boid(int x, int y, float maxSpeed, float forceMax){
        pos = new PVector(x, y);
        this.maxSpeed = maxSpeed;
        this.forceMax = forceMax;
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
        vel.limit(maxSpeed);
    }*/

    public PVector getPos() {
        return pos;
    }

    public PVector getVel() {
        return vel;
    }

    public PVector randomForce(){
        PVector force;

        force = new PVector(randGen.nextInt(20) - 10, randGen.nextInt(20) - 10);

        force.limit(forceMax);

        return force;
    }

    public float distance(Boid b){
        return pos.dist(b.getPos());
    }


    public void updatePosition(){
        PVector force = align();
        force.add(cohesion());
        if (Double.isNaN(force.x) || Double.isNaN(force.y)){
            force.x = 0.0f;
            force.y = 0.0f;
            force.z = 0.0f;
        }
        PVector sep = separate();
        if (Double.isNaN(sep.x) || Double.isNaN(sep.y)){
            sep.x = 0.0f;
            sep.y = 0.0f;
            sep.z = 0.0f;
        }
        force.add(sep.mult(3).div(2));
        System.out.println(force);
        System.out.println(sep);
        System.out.println(force);
        vel = vel.add(force);
        pos = pos.add(vel);
    }

    public PVector align(){
        ArrayList<Boid> neighbors = flock.neighbors(this, 25);

        PVector force = new PVector(0,0);

        for (Boid boid :
                neighbors) {
            force.add(boid.getVel());
        }

        force.div(neighbors.size());

        force.normalize();
        force.mult(maxSpeed);
        force.sub(vel);
        force.limit(forceMax);

        return force;
    }

    public PVector separate(){
        ArrayList<Boid> neighbors = flock.neighbors(this, 20);

        PVector force = new PVector(0,0);

        for (Boid boid :
                neighbors) {
            force.add(pos);
            force.sub(boid.getPos());
            float dist = distance(boid);
            force.div(dist);
        }

        force.div(neighbors.size());

        force.normalize();
        force.mult(maxSpeed);
        force.sub(vel);
        force.limit(forceMax);

        return force;
    }

    private PVector steer(PVector target, boolean slowDown) {
        PVector desired = PVector.sub(target, pos);
        if (desired.mag() <= 0) {
            return new PVector(0, 0);
        }

        desired.normalize();
        if (slowDown && desired.mag() < 100.0) {
            desired.mult((float) (maxSpeed * (desired.mag() / 100.0)));
        } else {
            desired.mult(maxSpeed);
        }

        PVector steeringVector = PVector.sub(desired, vel);
        steeringVector.limit((float) forceMax);
        return steeringVector;
    }

    public PVector cohesion(){
        ArrayList<Boid> neighbors = flock.neighbors(this, 25);

        PVector force = new PVector(0,0);

        for (Boid boid :
                neighbors) {
            force.add(boid.getPos());
        }

        force.div(neighbors.size());

        force = steer(force, true);

        return force;

    }

    public void borders() {
        float r = (float) 2.0;
        if (pos.x < -r) {
            pos.x = context.width + r;
        }

        if (pos.y < -r) {
            pos.y = context.height + r;
        }

        if (pos.x > context.width + r) {
            pos.x = -r;
        }

        if (pos.y > context.height + r) {
            pos.y = -r;
        }
    }

    public void render() {
        // Draw a triangle rotated in the direction of velocity
        float r = (float) 2.0;
        // this.velocity est la vitesse du Boid
        float theta = this.vel.heading() + PConstants.PI / 2;
        this.context.fill(200, 100);
        this.context.stroke(255);
        this.context.pushMatrix();
        this.context.translate(this.pos.x, this.pos.y);
        this.context.rotate(theta);
        this.context.beginShape(PConstants.TRIANGLES);
        this.context.vertex(0, -r * 2);
        this.context.vertex(-r, r * 2);
        this.context.vertex(r, r * 2);
        this.context.endShape();
        this.context.popMatrix();
    }

    public void run() {
        this.updatePosition();
        this.borders();
        this.render();
    }

}
