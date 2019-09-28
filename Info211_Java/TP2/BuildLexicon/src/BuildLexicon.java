import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

public class BuildLexicon {

	/**
	 * Affiche les paires triees par likelihood ratio
	 *
	 * @param res
	 */
	private static void printSortedLikelikhoodTable(HashMap<String, Double> res) {

		for (Entry<String, Double> r : MapUtil.sortByValue(res)) {
			System.out.println(r.getKey() + "->" + r.getValue());
		}

	}

	/**
	 * Afficher la table de coocurences par ordre de frequences croissantes
	 *
	 * @param coocTable
	 */
	private static void printSortedCoocTable(
			HashMap<String, HashMap<String, Integer>> coocTable) {

		HashMap<String, Double> res = new HashMap<String, Double>();
		for (String frWord : coocTable.keySet()) {
			HashMap<String, Integer> map = coocTable.get(frWord);

			for (String enWord : map.keySet()) {
				res.put(frWord + "-" + enWord, (double) map.get(enWord));
			}
		}

		for (Entry<String, Double> r : MapUtil.sortByValue(res)) {
		    System.out.println(r.getKey() + "->" + r.getValue());
		}

	}

	/**
	 * Retourne le contenu d'un fichier sous forme d'une chaine de caractères
	 *
	 * @param filename
	 * @return
	 */
	public static String readFile(String filename) {
		try {
			FileInputStream stream = new FileInputStream(new File(filename));
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			stream.close();
			return Charset.defaultCharset().decode(bb).toString();
		} catch (IOException e) {
			System.out.println("Erreur lors de l'accès au fichier " + filename);
			System.exit(1);
		}
		return null;
	}


	/**
	 *  compte le nombre de phrases dans lequel apparait un mot donnee
	 *  attention : les mots repetes plusieurs fois dans une phrase ne compte que une fois
	 *
	 * @param txt
	 * @return
	 */
    // Methode à realiser
    public static HashMap<String, Integer> countSentencesWithWord(String txt) throws IOException {

    	String[] sentences = txt.split("\n");
    	String[] words = txt.split("\\s");

		for (int i = 0; i < sentences.length; i++) {
			sentences[i] = sentences[i].replace("\\{Punct}", "");
		}

		for (int i = 0; i < words.length; i++) {
			words[i] = words[i].replace("\\{Punct}", "");
		}

    	HashSet<String> everyWord = new HashSet<>(Arrays.asList(words));

		HashMap<String, Integer> wordCount = new HashMap<>();

		for(String word: everyWord){
			wordCount.put(word, 0);
		}

		for (Entry<String, Integer> entry :
				wordCount.entrySet()) {
			String word = entry.getKey();
			for (String sentence :
					sentences) {
				if (sentence.contains(word)) {
					entry.setValue(entry.getValue() + 1);
				}
			}
		}

		return wordCount;
    }

    // Construit la table de coocurence : à une paire (mot français, mot
    // anglais), on associe le nombre de bi-phrases dans lesquelles cette paire
    // apparait
    // Attention : paires répétées plusieurs fois dans une bi-phrase ne doivent
    // être comptées qu'une fois.
    //
    // Méthode à réaliser
    private static HashMap<String, HashMap<String, Integer>> buildCoocTable(
									    String fr, String en) throws IOException {
    	HashMap<String, HashMap<String, Integer>> coocTable = new HashMap<>();
    	String[] sentencesFr = fr.split("\n");
    	String[] sentencesEn = en.split("\n");

    	HashSet<String> wordsFr = new HashSet<>(Arrays.asList(fr.split("\\s")));

        for (int i = 0; i < sentencesEn.length; i++) {
            sentencesEn[i] = sentencesEn[i].replace("\\{Punct}", "");
        }

        for (int i = 0; i < sentencesFr.length; i++) {
            sentencesFr[i] = sentencesFr[i].replace("\\{Punct}", "");
        }


		for (String word :
				wordsFr) {

			for (int i = 0; i < sentencesFr.length; i++) {
				if(sentencesFr[i].contains(word)){
					HashMap<String, Integer> temp = countSentencesWithWord(sentencesEn[i]);
					if (coocTable.containsKey(word)){
						HashMap<String, Integer> temp2 = coocTable.get(word);
						for (String key:
							 temp2.keySet()) {
							if(temp.containsKey(key)){
								temp.put(key, temp.get(key) + 1);
							} else {
								temp.put(key, temp2.get(key));
							}
						}
					}
					coocTable.put(word, temp);
				}
			}
		}

		return coocTable;
    }

    /**
     * Construit la liste des paires et de leur likelihoodRatio
     *
     * @param table
     * @param frWordCounts
     * @param enWordCounts
     * @param nSentences
     * @return
     */
    // Methode à realiser
    public static HashMap<String, Double> constructLikelihoodTable(
								   HashMap<String, HashMap<String, Integer>> table,
								   HashMap<String, Integer> frWordCounts,
								   HashMap<String, Integer> enWordCounts, int nSentences) {
    	HashMap<String, Double> likelihoodTable = new HashMap<>();

        for (String frWord :
                table.keySet()) {
            HashMap<String, Integer> map = table.get(frWord);
            for (String enWord :
                    map.keySet()) {
                double ratio = likelihoodRatio(frWordCounts.get(frWord), enWordCounts.get(enWord), map.get(enWord), nSentences);
                if (!Double.isNaN(ratio)){
                    likelihoodTable.put(frWord + " " + enWord, ratio);
                }
            }
        }

		return likelihoodTable;

    }

    public static double likelihoodRatio(int nA, int nB, int nAB, int n) {
        double nA_d = (double) nA;
        double nB_d = (double) nB;
        double nAB_d = (double) nAB;
        double n_d = (double) n;
        double non_a_b = ((n_d-(nA_d + nB_d))/n_d) * Math.log((((n_d-(nA_d + nB_d))/n_d)) / ((n_d - nA_d)/n_d) * ((n_d - nB_d)/n_d));
        double a_b = (nAB_d/n_d) * Math.log((nAB_d/n_d) / ((nA_d/n_d) * (nB_d/n_d)));
        return 2*n*(non_a_b + a_b);
    }

    public static void main(String[] args) throws IOException {
        String fr = readFile("french.corpus");
        String en = readFile("english.corpus");

        int nSentences = fr.split("\n").length;

        HashMap<String, Integer> frWordCounts = countSentencesWithWord(fr);
        HashMap<String, Integer> enWordCounts = countSentencesWithWord(en);
        HashMap<String, HashMap<String, Integer>> coocTable = buildCoocTable(
                                             fr, en);

        // attention, la méthode détruit la table de coocurrence !!!
        //printSortedCoocTable(coocTable);

		HashMap<String, Double> res = constructLikelihoodTable(coocTable,
								       frWordCounts, enWordCounts, nSentences);
		printSortedLikelikhoodTable(res);
    }

}
