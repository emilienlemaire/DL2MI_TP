import java.lang.Math;

public class Vector {
    private double x;
    private double y;
    private double z;

    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector(){
        x = 0;
        y = 0;
        z = 0;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getZ(){
        return z;
    }

    public String toString(){
        return "<" + x  + ", " + y + ", " + z + ">";
    }

    public double norm(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public Vector add(Vector v){
        return new Vector(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    public double dot(Vector v){
        return x * v.getX() + y * v.getY() + z * v.getZ();
    }
}
