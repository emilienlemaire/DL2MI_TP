package perceptron;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class OnlinePerceptron {
    public static final int DIM = 3;
    public static float[] w = new float[DIM];
    public static float[][] data = {
            {1, 0, 0},
            {1, 0, 1},
            {1, 1, 0},
            {1, 1, 1}
    };
    public static int[] refs ={-1, -1, -1, 1};
    public static int T = 100;
    public static int E = 0;
    public static File myFile = new File("sep.d");

    FileWriter erase;

    static {
        try {
            new FileWriter("sep.d", false).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ecritFichier(float[] w) throws IOException {
        float x1 = -1;
        float x2 = 2;

        float y1 = (-w[1] * x1 + w[0]) / w[1];
        float y2 = (-w[1] * x2 + w[0]) / w[1];

        if (myFile.exists()){
            FileWriter fw = new FileWriter("sep.d", true);

            fw.write(x1 + " " + y1);
            fw.write("\n");
            fw.write(x2 + " " + y2);
            fw.write("\n");
            fw.write("\n");

            fw.close();

        } else {
            myFile.createNewFile();
            FileWriter fw = new FileWriter("sep.d");

            fw.write(x1 + " " + y1);
            fw.write("\n");
            fw.write(x2 + " " + y2);
            fw.close();
        }
    }

    /**
     * Met à jour les paramètres du perceptron à partir d'une donnée mal classée.
     * @param w Les paramètres du perceptron
     * @param data La donnée mal classée
     * @param classe La classe de la donnée data
     */
    public static void miseAJour(float[]w, float[] data, int classe, float eta) throws IOException {
        for (int i = 0; i < w.length; i++) {
            if (i == 0){
                w[i] += classe;
            } else {
                w[i] = w[i] + ((float) classe * eta * data[i]);
            }
        }

        ecritFichier(w);

    }

    /**
     * Calcule le produit scalaire de deux vecteurs
     * @param a Un tableau de réels
     * @param b Un tableau de réels
     * @return Un réel x = a x b
     */
    public static float arrayProduct(float[] a, float[] b){

        if (a.length == b.length) {
            float result = 0;

            for (int i = 0; i < a.length; i++) {
                result += a[i] * b[i];
            }

            return result;
        } else {
           System.out.println("Les vecteurs doivent être de même taille!!");
           System.exit(1);
        }

        return 0f;

    }

    /**
     * Affiche un table dans la console.
     * @param w Un tableau de réels.
     */
    public static void printArray(float[] w){
        for (float elt:
             w) {
            System.out.print(elt + " ");
        }

        System.out.println();
    }

    /**
     * Affiche un table dans la console.
     * @param w Un tableau d'entier
     */
    public static void printArray(int[] w){
        for (int elt:
             w) {
            System.out.print(elt + " ");
        }

        System.out.println();
    }

    /**
     * Cette fonction effectue toutes les actions d'une époque.
     * @param w Les paramètres du perceptron
     * @param data Le jeu de donnée à classer
     * @param refs Les classes de chaque donnée
     * @return Les paramètre du perceptron mis-à-jour après une époque. Si l'époque n'a pas d'erreur, le programme
     *         s'arrête et renvoie 0.
     */
    public static float[] epoque(float[] w, float[][] data, int[] refs, float eta) throws IOException {

        boolean erreur = false;

        for (int i = 0; i < data.length; i++) {
            float a = arrayProduct(data[i] , w);
            if ((a *((float) refs[i])) < 0) {
                erreur = true;
                System.out.println("Erreur pour la coordonnée : " + (i+1) + ".");
                miseAJour(w, data[i], refs[i], eta);
            }
        }

        E++;

        if (!erreur) {
            System.out.println("Il n'y a plus d'erreur dans le classement après " + E + " époques");
            printArray(w);
            System.exit(0);
        }

        return w;
    }

    /**
     * Cette fonction fait l'apprentisaage des paramètres.
     * @param w Les paramètres du perceptron
     * @param data Le jeu de donné à classer
     * @param refs Les classes de chaque donnée
     * @param T Le nombre d'époques maximum
     */
    public static void apprentissage(float[] w, float[][] data, int[] refs, int T, float eta) throws IOException {
        for (int i = 0; i < T; i++) {
            epoque(w, data, refs, eta);
        }

        System.out.println("Nombre d'époques maximales atteint");

        System.out.print("W = ");

        printArray(w);

        System.exit(0);
    }

    public static void main(String[] args) throws IOException {

        w = new float[]{3, -1.5f, 10};

        for (int i = 0; i < data.length; i++) {
            float[] x = data[i];
            System.out.println("x= " + Arrays.toString(x) + " / y= " + refs[i]);
        }

        float eta = 1;

        apprentissage(w, data, refs, T, eta);

        printArray(w);
    }
}
