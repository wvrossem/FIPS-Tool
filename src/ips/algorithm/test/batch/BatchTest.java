package ips.algorithm.test.batch;

import ips.algorithm.PositioningResult;
import ips.data.entities.Position;
import ips.data.entities.wlan.WLANFingerprint;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * Used to easily calculate the results from a batch test.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class BatchTest {

	Map<WLANFingerprint, PositioningResult> results;

	Map<WLANFingerprint, Double> accuracies;

	public BatchTest(Map<WLANFingerprint, PositioningResult> results) {
		super();
		this.results = results;
	}

	/**
	 * 
	 * @return The average accuracy that was measured in the batch test.
	 */
	public double getAverageAccuracy() {

		double res = 0;

		for (Entry<WLANFingerprint, Double> entry : getAccuracies().entrySet()) {

			res += entry.getValue();
		}

		return res / getAccuracies().size();
	}

	/**
	 * 
	 * @return The number of correct positions that were calculated.
	 */
	public int getNrOfCorrectPos() {

		int res = 0;

		for (Entry<WLANFingerprint, Double> entry : getAccuracies().entrySet()) {

			if (entry.getValue() == 0) {

				res += 1;
			}
		}

		return res;
	}

	public Map<WLANFingerprint, Double> getAccuracies() {

		if (accuracies != null) {

			return accuracies;
		}

		Map<WLANFingerprint, Double> res = new HashMap<WLANFingerprint, Double>();

		for (Entry<WLANFingerprint, PositioningResult> entry : results
				.entrySet()) {

			Position pos1 = entry.getKey().getPosition();
			Position pos2 = entry.getValue().getPosition();

			double distance = calcDistance(pos1, pos2);

			res.put(entry.getKey(), distance);
		}

		accuracies = res;

		return res;
	}

	/**
	 * 
	 * Calculates the euclidian distance between to positions = the accuracy.
	 * 
	 * @param pos1 First position
	 * @param pos2 Second position
	 * @return The distance between them
	 */
	private double calcDistance(Position pos1, Position pos2) {

		double xVal = pos1.getX() - pos2.getX();
		xVal = Math.pow(xVal, 2);

		double yVal = pos1.getY() - pos2.getY();
		yVal = Math.pow(yVal, 2);

		return Math.sqrt(xVal + yVal);
	}
}
