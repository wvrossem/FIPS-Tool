package ips.algorithm.test.batch;

import ips.algorithm.PositioningAlgorithmType;
import ips.data.entities.wlan.WLANFingerprint;
import ips.server.DataUploadRequest;

import java.util.List;

/**
 * 
 * A single batch positioning request. Executes a certain algorithm on a number
 * of measurements. Optionally it can also does a data upload request first.
 * 
 * @author Wouter Van Rossem
 * 
 */
public class BatchPositioning {

	private List<WLANFingerprint> fps;

	private PositioningAlgorithmType algo;

	private DataUploadRequest uploadRequest;

	public BatchPositioning(List<WLANFingerprint> fps,
			PositioningAlgorithmType algo) {
		super();
		this.fps = fps;
		this.algo = algo;
		this.uploadRequest = null;
	}

	public boolean hasUploadRequest() {

		return (uploadRequest != null);
	}

	public DataUploadRequest getUploadRequest() {
		return uploadRequest;
	}

	public void setUploadRequest(DataUploadRequest uploadRequest) {
		this.uploadRequest = uploadRequest;
	}

	public List<WLANFingerprint> getFps() {
		return fps;
	}

	public void setFps(List<WLANFingerprint> fps) {
		this.fps = fps;
	}

	public PositioningAlgorithmType getAlgo() {
		return algo;
	}

	public void setAlgo(PositioningAlgorithmType algo) {
		this.algo = algo;
	}

}
