package image;
import mnisttools.MnistReader;


public class LectureImage {

    public static int[] pixelsActives(int[][] imageBin){

        int[] pixels = new int[imageBin.length];

        for (int i = 0; i < imageBin.length; i++) {

            for (int j = 0; j < imageBin[i].length; j++) {

                 if(imageBin[i][j] == 1){

                     pixels[i]++;

                 }
            }
        }

        return pixels;

    }

    public static int[][] binarisation(int[][] image, int seuil) {
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

    public static int[][] calcToutesImages(MnistReader db){

        int nbIm = db.getTotalImages();

        int[][] res = new int[nbIm][28];

        for (int i = 1; i <= nbIm; i++) {
            res[i - 1] = pixelsActives(binarisation(db.getImage(i), 127));
        }

        return res;

    }

    public static int[] calcImage(MnistReader db, int index){

        int[][] image = db.getImage(index);
        int[] res = new int[image.length];

        res = pixelsActives(binarisation(image, 127));

        return res;

    }

    public static void main(String[] args) {
        String path = "/Volumes/LaCie/DL2MI/DL2MI_TP/Info215_ApprAuto/TP1/resources/";
        String labelDB = path+"train-labels.idx1-ubyte";
        String imageDB = path+"train-images.idx3-ubyte";
        // Creation de la base de donnees
        MnistReader db = new MnistReader(labelDB, imageDB);
        // Acces a la premiere image
        int idx = 1; // une variable pour stocker l'index
                    // Attention la premiere valeur est 1.
        int [][] image = db.getImage(idx); /// On recupere la premiere l'image numero idx
        // Son etiquette ou label
        int label = db.getLabel(idx);
        // Affichage du label
        System.out.print("Le label est "+ label+"\n");
        // note: le caractère \n est le 'retour charriot' (retour a la ligne).
        // Affichage du nombre total d'image
        System.out.print("Le total est "+ db.getTotalImages()+"\n");
        /* A vous de jouer pour la suite */

        System.out.print("La taille des images est: "+ image[0].length + "x" + image.length +"\n");

        //Affichage des entiers de chaque pixel
        for (int[] ligne:
             image) {
            for (int pixel:
                 ligne) {
                if (pixel > 100){
                    System.out.print(pixel + " ");
                } else if(pixel > 10) {
                    System.out.print("0" + pixel + " ");
                } else {
                    System.out.print("00" + pixel + " ");
                }
            }
            System.out.print("\n");
        }

        //Calcul des min et max pour chaque colonne et ligne
        int minLigne[] = new int[image.length];
        int maxLigne[] = new int[image.length];
        int minCol[] = new int[image.length];
        int maxCol[] = new int[image.length];

        for (int i = 0; i <image.length; i += 1){
            for(int j = 0; j < image[i].length; j += 1){
                if(minLigne[i] > image[i][j]){
                    minLigne[i] = image[i][j];
                }
                if(maxLigne[i] < image[i][j]){
                    maxLigne[i] = image[i][j];
                }
                if(minCol[j] > image[j][i]){
                    minCol[j] = image[j][i];
                }
                if(maxCol[j] < image[j][i]){
                    maxCol[j] = image[j][i];
                }
            }
        }

        for (int i = 0; i < image.length; i++) {
            System.out.print(minLigne[i] + " " + maxLigne[i] + " " + minCol[i] + " " + maxCol[i] + "\n");
        }

        //Calcul de l'image binarisée
        int[][] imageBin = binarisation(image, 63);

        //Affichage de l'image binarisée
        for (int i = 0; i < imageBin.length; i++) {
            for (int j = 0; j < imageBin[i].length; j++) {
                System.out.print((imageBin[i][j] == 0) ? " " : "X");
            }
            System.out.println();
        }

        //Partie pour aller plus loin

        //Enlever le commentaire seulement pour analyser toutes les images
        //int[][] toutesImages = calcToutesImages(db);

        int index1 = 0;
        int i = 1;

        //On cherche le premier index ou le label est 1
        while(index1 != 1){
            index1 = db.getLabel(i);
            i++;
        }

        System.out.println(index1);

        /*for (int j = 0; j < toutesImages[label].length; j++) {
            System.out.print(toutesImages[label][j] + " ");
        }*/

        int[] image1 = calcImage(db, index1);

        for (int ligne:
             image1) {
            System.out.print(ligne + " ");
        }

        System.out.println();

        int index8 = 0;
        i = 1;

        //On cherche le premier index ou le label est 8
        while(index8 != 8){
            index8 = db.getLabel(i);
            i++;
        }


        System.out.println(index8);

        int[] image8 = calcImage(db, index8);

        for (int ligne :
                image8) {
            System.out.print(ligne + " ");
        }

        /*for (int j = 0; j < toutesImages[label].length; j++) {
            System.out.print(toutesImages[label][j] + " ");
        }*/

    }

}
