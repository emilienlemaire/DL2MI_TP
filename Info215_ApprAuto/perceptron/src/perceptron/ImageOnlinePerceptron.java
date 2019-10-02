package perceptron;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import mnisttools.MnistReader;

public class ImageOnlinePerceptron {

    /* Les donnees */
    public static String path="/Volumes/LaCie/DL2MI/DL2MI_TP/Info215_ApprAuto/perceptron/resources/";
    public static String labelDB=path+"train-labels.idx1-ubyte";
    public static String imageDB=path+"train-images.idx3-ubyte";

    /* Parametres */
    // Na exemples pour l'ensemble d'apprentissage
    public static final int Na = 1000;
    // Nv exemples pour l'ensemble d'évaluation
    public static final int Nv = 1000;
    // Nombre d'epoque max
    public final static int EPOCHMAX=40;
    // Classe positive (le reste sera considere comme des ex. negatifs):
    public static int  classe = 5 ;

    // Générateur de nombres aléatoires
    public static int seed = 1234;
    public static Random GenRdm = new Random(seed);

    /*
    *  BinariserImage :
    *      image: une image int à deux dimensions (extraite de MNIST)
    *      seuil: parametre pour la binarisation
    *
    *  on binarise l'image à l'aide du seuil indiqué
    *
    */
    public static int[][] BinariserImage(int[][] image, int seuil) {

        int[][] res = new int[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] > seuil){
                    res[i][j] = 1;
                }
            }
        }
        return res;
    }

    /*
    *  ConvertImage :
    *      image: une image int binarisée à deux dimensions
    *
    *  1. on convertit l'image en deux dimension dx X dy, en un tableau unidimensionnel de tail dx.dy
    *  2. on rajoute un élément en première position du tableau qui sera à 1
    *  La taille finale renvoyée sera dx.dy + 1
    *
    */
    public static float[] ConvertImage(int[][] image) {
        float[] res = new float[image.length * image[0].length + 1];

        res[0] = 1;

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                res[i*image.length + j + 1] = (float) image[i][j];
            }
        }

        return res;
    }

    /*
    *  InitialiseW :
    *      sizeW : la taille du vecteur de poids
    *      alpha : facteur à rajouter devant le nombre aléatoire
    *
    *  le vecteur de poids est crée et initialisé à l'aide d'un générateur
    *  de nombres aléatoires.
    */
    public static float[] InitialiseW(int sizeW, float alpha) {
        float[] W = new float[sizeW];

        for (int i = 0; i < W.length; i++) {
            W[i] = GenRdm.nextFloat() * alpha;
        }

        return W;
    }


    public static void main(String[] args) throws IOException {
        System.out.println("# Load the database !");
        /* Lecteur d'image */
        MnistReader db = new MnistReader(labelDB, imageDB);

        int[][] image = db.getImage(1);

        /*Creation des donnees */
        System.out.println("# Build train for digit "+ classe);
        /* Tableau où stocker les données */

        int dim = (image.length * image[0].length + 1);

        float[][] trainData = new float[Na][dim];

        for (int i = 1; i <= Na; i++) {
            int[][] data = db.getImage(i);
            int[][] dataBin = BinariserImage(data, 127);
            float[] dataLin = ConvertImage(dataBin);

            trainData[i-1] = dataLin;
        }

        int[] trainRefs = new int[Na];

        for (int i = 1; i <= Na; i++) {
            trainRefs[i-1] = db.getLabel(i);
        }

        float[] W = InitialiseW(dim, -100);

        float eta = 1;

        OnlinePerceptron.apprentissage(W, trainData, trainRefs, EPOCHMAX, eta);

        System.out.println(W);
    }
}