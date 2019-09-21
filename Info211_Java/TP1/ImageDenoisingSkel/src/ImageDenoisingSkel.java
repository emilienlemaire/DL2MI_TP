import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

// Pensez à changer le nom de la classe !!
public class ImageDenoisingSkel {

	// Méthodes permettant de charger les images
	// -----------------------------------------

	// Chargement de l'image contenue dans `filename`
	public static BufferedImage readImage(String filename) {

		try {
			return ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("Erreur lors du chargement de " + filename);
			System.out.println(e);
			System.exit(1);
		}

		return null;

	}

	// Conversion d'un quadruplet (R, G, B, Alpha) en un octet
	public static int colorToRGB(int alpha, int red, int green, int blue) {

		int newPixel = 0;
		newPixel += alpha;
		newPixel = newPixel << 8;
		newPixel += red;
		newPixel = newPixel << 8;
		newPixel += green;
		newPixel = newPixel << 8;
		newPixel += blue;

		return newPixel;

	}

	// Création d'une nouvelle image correspondant à l'image `originale' en
	// niveau de gris
	public static BufferedImage toGray(BufferedImage original) {

		int alpha, red, green, blue;
		int newPixel;

		BufferedImage lum = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				alpha = new Color(original.getRGB(i, j)).getAlpha();
				red = new Color(original.getRGB(i, j)).getRed();
				green = new Color(original.getRGB(i, j)).getGreen();
				blue = new Color(original.getRGB(i, j)).getBlue();

				int grey = (int) (0.21 * red + 0.71 * green + 0.07 * blue);
				newPixel = colorToRGB(alpha, grey, grey, grey);

				lum.setRGB(i, j, newPixel);

			}
		}

		return lum;

	}

	// Conversion d'une image en une image binaire
	public static BufferedImage binarize(BufferedImage original, int threshold) {

		int red;
		int newPixel;

		BufferedImage binarized = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());

		for (int i = 0; i < original.getWidth(); i++) {
			for (int j = 0; j < original.getHeight(); j++) {

				red = new Color(original.getRGB(i, j)).getRed();
				int alpha = new Color(original.getRGB(i, j)).getAlpha();
				if (red > threshold) {
					newPixel = 255;
				} else {
					newPixel = 0;
				}
				newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
				binarized.setRGB(i, j, newPixel);

			}
		}

		return binarized;

	}

	// conversion d'une image en une suite d'octets
	public static int[] img2seq(BufferedImage img) {
		int[] res = new int[img.getWidth() * img.getHeight()];

		int count = 0;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				res[count] = img.getRGB(i, j) == -1 ? 0 : 1;
				count += 1;
			}
		}

		return res;
	}

	// conversion d'une suite d'octets en image
	public static BufferedImage seq2img(int[] data, int width, int height) {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);

		int idx = 0;
		for (int i = 0; i < img.getWidth(); i++) {
			for (int j = 0; j < img.getHeight(); j++) {
				img.setRGB(i, j, data[idx] == 0 ? -1 : -16777216);
				idx += 1;
			}
		}

		return img;
	}

	// Méthodes à réaliser
	// -------------------

	public static int[] addNoise(int[] data, double probability) {

		if ((probability > 1) && (probability < 0)) {
			System.out.print("Probablity pas compris entre 0 et 1");
			System.exit(1);
		}

		int[] dataReturn = new int[data.length];

		Random randomGenerator = new Random();

		for (int i = 0; i < data.length; i++) {

			if(randomGenerator.nextFloat() < probability){
				if(data[i] == 1){
					dataReturn[i] = 0;
				} else {
					dataReturn[i] = 1;
				}
			} else {
				dataReturn[i] = data[i];
			}
		}

		return dataReturn;
	}

	public static int[] encode(int[] data) {

		int[] data3 = new int[3 * data.length];

		int j = 0;

		for (int i = 0; i < data.length; i++) {
			data3[j] = data[i];
			data3[j + 1] = data[i];
			data3[j + 1] = data[i];

			j += 3;
		}

		return data3;
	}

	public static int[] decode(int[] data) {

		int[] dataDecoded = new int[data.length / 3];

		int j = 0;

		for (int i = 0; i < dataDecoded.length; i++) {
			dataDecoded[i] = data[j];
			j += 3;
		}

		return dataDecoded;
	}

	public static float score(int[] imageOriginale, int[] imageFinale) {

		float scoreFinal = 0;

		for (int i = 0; i < imageOriginale.length; i++) {
			if (imageFinale[i] != imageOriginale[i]) {
				scoreFinal++;
			}
		}

		return (100 * scoreFinal) / imageOriginale.length;
	}

	private static int[] histogram(BufferedImage img) {

		WritableRaster raster = img.getRaster();
		int[] data = new int[img.getWidth() * img.getHeight()];
		data = raster.getSamples(0, 0, img.getWidth(), img.getHeight(), 0, data);

		int max = 0;

		for (int color:
			 data) {
			if (color > max){
				max = color;
			}
		}

		int[] hist = new int[max + 1];

		for (int datum : data) {
			hist[datum]++;
		}

		return hist;
	}

	private static int nbT(int T, int[] histogram){

		int nb = 0;

		for (int i = 0; i < T; i++) {
			nb += histogram[i];
		}

		return nb;

	}

	private static int noT(int T, int[] histogram){

		int no = 0;

		for (int i = 0; i < 256 - T; i++) {
			no += histogram[T + i];
		}

		return no;

	}

	private static int[][] variances(int T, int[] histogram){

		int[][] resultat = new int[2][2];

		int mu1 = 0;
		int mu2 = 0;

		for (int i = 0; i < T; i++) {
			mu1 += histogram[i];
		}

		for (int i = 0; i < (256 - T); i++) {
			mu2 += histogram[i + T];
		}

		int sigma1 = 0;
		int sigma2 = 0;


		for (int i = 0; i < T; i++) {
			sigma1 += (mu1 - histogram[i]) * (mu1 - histogram[i]);
		}

		for (int i = 0; i < (256 - T); i++) {
			sigma2 += (mu2 -  histogram[i + T]) * (mu2 -  histogram[i + T]);
		}

		resultat[0][0] = sigma1;
		resultat[0][1] = sigma2;

		resultat[1][0] = mu1;
		resultat[1][1] = mu2;

		return resultat;

	}

	private static int oWithin(int T, int[] histogram){
		return (nbT(T, histogram) *  variances(T, histogram)[0][0] + noT(T, histogram) * variances(T, histogram)[0][1]);
	}

	private static int oBetween(int T, int[] histogram){
		return (nbT(T, histogram) *  noT(T, histogram) * (variances(T, histogram)[1][0] - variances(T, histogram)[1][1]));
	}

	private static int otsuT(BufferedImage img){

		int T = 0;

		int[] histogram = histogram(img);

		int oBetween = 0;

		for (int i = 0; i < 256; i++) {

			if (oBetween < oBetween(i, histogram)) {
				oBetween = oBetween(i, histogram);
				T = i;
			}

		}

		return T;
	}

	public static void main(String[] s) throws IOException {

		float setNoise = .1f;

		// Génération de l'image en niveau de gris
		BufferedImage img = readImage("image.png");

		int T = otsuT(img);

		System.out.println("La valeur de T est: " + T);

		img = toGray(img);
		img = binarize(img, T);

		ImageIO.write(img, "png", new File("binary.png"));

		// Conversion de l'image en une séquence binaire
		int[] data = img2seq(img);

		// Transmission d'une image non encodée
		int[] imgWithNoise = addNoise(data, setNoise);

		if (imgWithNoise != null) {
			BufferedImage output2 = seq2img(imgWithNoise, img.getWidth(), img.getHeight());
			ImageIO.write(output2, "png", new File("noise.png"));
		}

		// Transmission d'une image encodée
		int[] encoded = encode(data);
		int[] noised = addNoise(encoded, setNoise);
		int[] decoded = decode(noised);

		if (decoded != null) {
			BufferedImage output = seq2img(decoded, img.getWidth(), img.getHeight());
			ImageIO.write(output, "png", new File("encoded.png"));
		}

		if (data != null) {
			BufferedImage output = seq2img(data, img.getWidth(), img.getHeight());
			ImageIO.write(output, "png", new File("data.png"));
		}

		float scoreEncoded = score(data, decoded);

		System.out.println(scoreEncoded + "%");

	}

}