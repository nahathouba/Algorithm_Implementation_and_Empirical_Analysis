package generation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DataGenerator {
	/** Start of integer range to generate values from. */
	protected static double mStartOfRange = 30;
	/** End of integer range to generate values from. */
	protected static double mEndOfRange = 140;
	/** Random generator to use. */
	static Random mRandGen;
	
	static int sampleSize = 10000;

	/**
	 * Constructor.
	 *
	 * @param startOfRange Start of integer range to generate values.
	 * @param endOfRange   End of integer range to generate values.
	 * @throws IllegalArgumentException If range of integers is inappropriate
	 */
	public DataGenerator() throws IllegalArgumentException {
		// use current time as seed
		mRandGen = new Random(System.currentTimeMillis());

	}

	/**
	 * Generate one sample, using sampling with replacement.
	 */
	public static double sampleWithReplacement() {
//		return mRandGen.nextDouble(mEndOfRange - mStartOfRange + 1.00) + mStartOfRange;
		return (Math.random() * ((mEndOfRange - mStartOfRange)) + 1) + mStartOfRange;
	} // end of sampleWithReplacement()

	/**
	 * Generate 'sampleSize' number of samples, using sampling with replacement.
	 *
	 * @param sampleSize Number of samples to generate.
	 */
	public static List<PointGen> sampleWithReplacement(int sampleSize) {
//		int[] samples = new int[sampleSize];
		List<PointGen> samples = new ArrayList<PointGen>();
		int count = 1;
		int r = 1;
		for (int i = 0; i < sampleSize; i++) {
//			samples[i] = sampleWithReplacement();
			double x = sampleWithReplacement();
			for (int j = 0; j < 1; j++) {
				double y = sampleWithReplacement();
				if (r > 3)
					r = 1;
				samples.add(new PointGen("id"+count++, getCat(r++), x, y));
			}
		}

		return samples;
	}
	
	private static Category getCat(int r) {
		Category cat = null;
		
//		Random ran = new Random();
//		int r = (ran.nextInt(3 - 1) + 1) + 1;
				
		if (r == 1) {
			cat = Category.RESTAURANT;
		} else if (r == 2) {
			cat =  Category.HOSPITAL;
		} else if (r == 3) {
			cat =  Category.EDUCATION;
		}
		return cat;
	}
	
	public static void main(String[] args) {
		String outputFileName = args[0];
		
		File outputFile = new File(outputFileName);
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			List<PointGen> p = sampleWithReplacement(sampleSize);
			for (PointGen pg: p)
				writer.println(pg.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			System.err.println("Command file doesn't exist.");
		}
	}

	
	private  static class PointGen {
		String id = null;
		Category cat;
		double lat;
		double lon;
		
		public PointGen(String id, Category cat, double lat, double lon) {
			this.id = id;
			this.cat = cat;
			this.lat = lat;
			this.lon = lon;
		}
		
		@Override
		public String toString() {
			 return id + " " + cat + " " + lat + " "+  lon;
		}
	}
	
	private enum Category {
	    RESTAURANT, EDUCATION, HOSPITAL
	}
}