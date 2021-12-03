package com.example.screentofacedistance;

public class Measurement {

	private float distanceEyes;
	private float averageDistance;
	private float conf;
	private int leftMeasurements;
	private float positionMeasurement;
	private float faceDistance;

	public float getAverageDistance() {
		return averageDistance;
	}

	public void setAverageDistance(float dist) {
		averageDistance = dist;
	}

	public float getConf() {
		return conf;
	}

	public void setConf(float conff) {
		conf = conff;
	}

	public float distanceToFaceGet() {
		return faceDistance;
	}

	public void distanceToFaceSet(float f) {
		faceDistance = f;
	}

	public float distanceToEyeGet() {
		return distanceEyes;
	}

	public void distanceToEyeSet(float eD) {
		distanceEyes = eD;
	}

	public int leftMeasurementsGet() {
		return leftMeasurements;
	}

	public void leftMeasurementsSet(int mL) {
		leftMeasurements = mL;
	}

	public float positionMeasuredGet() {
		return positionMeasurement;
	}

	public void positionMeasuredSet(
			float mP) {
		positionMeasurement = mP;
	}

}
