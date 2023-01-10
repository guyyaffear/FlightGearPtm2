package model;

public class AttributeSettings {
	Integer colInCSV;
	float minValue,maxValue;

	public AttributeSettings(String[] data) {
		if(data.length==3) {
			this.colInCSV = Integer.parseInt(data[0]);
			this.minValue = Float.parseFloat(data[1]);
			this.maxValue = Float.parseFloat(data[2]);
		}
	}

	public Integer getColInCSV() {
		return colInCSV;
	}

	public void setColInCSV(Integer colInCSV) {
		this.colInCSV = colInCSV;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}
}
