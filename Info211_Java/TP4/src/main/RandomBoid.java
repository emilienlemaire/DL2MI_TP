package main;

import processing.core.*;

import java.util.Random;

public class RandomBoid {
    private PVector pos;
    private PVector vel;
    private float maxSpeed = 2;
    private float forceMax = 0.05f;
    private Random randGen = new Random();
    private PApplet context;

    public RandomBoid(int x, int y){
        pos = new PVector(x, y);
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
        vel.limit(maxSpeed);
    }

    public RandomBoid(PApplet context, int x, int y){
        pos = new PVector(x, y);
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
        vel.limit(maxSpeed);
        this.context = context;
    }

    public RandomBoid(int x, int y, float maxSpeed, float forceMax){
        pos = new PVector(x, y);
        this.maxSpeed = maxSpeed;
        this.forceMax = forceMax;
        vel = new PVector(randGen.nextInt(2) - 1, randGen.nextInt(2) - 1);
        vel.limit(maxSpeed);
    }

    public PVector randomForce(){
        PVector force;

        force = new PVector(randGen.nextInt(20) - 10, randGen.nextInt(20) - 10);

        force.limit(forceMax);

        return force;
    }

    public void updatePosition(){
        vel = vel.add(randomForce());
        pos = pos.add(vel);
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
