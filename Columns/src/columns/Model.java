package columns;

import java.awt.HeadlessException;

import columns.model.Figure;

@SuppressWarnings("serial")
public class Model {

	protected static final int Depth = 15;
	public static final int Width = 7;
	protected static final int MaxLevel = 7;
	private Figure Fig;
	private int fieldNew[][];
	private int fieldOld[][];

	public Model() throws HeadlessException {
		super();
	}

	public Figure getFigure() {
		return Fig;
	}

	public void setFigure(Figure fig) {
		Fig = fig;
	}

	public int[][] getFieldNew() {
		return fieldNew;
	}

	public void setFieldNew(int fieldNew[][]) {
		this.fieldNew = fieldNew;
	}

	public int[][] getFieldOld() {
		return fieldOld;
	}

	public void setFieldOld(int fieldOld[][]) {
		this.fieldOld = fieldOld;
	}

}