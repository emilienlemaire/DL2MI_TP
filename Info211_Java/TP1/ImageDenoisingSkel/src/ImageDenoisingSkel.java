import java.awt.Color;
import java.awt.image.BufferedImage;
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

	public static void main(String[] s) throws IOException {

		float setNoise = .1f;

		// Génération de l'image en niveau de gris
		BufferedImage img = readImage("image.png");

		img = toGray(img);
		img = binarize(img, 80);

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