package columns.view;

public interface GameGraphics {

	void setColor(int color);

	void fillRect(int i, int j, int sl, int sl2);

	void drawRect(int i, int j, int sl, int sl2);

	void drawString(String string, int i, int j);

	void clearRect(int i, int j, int k, int l);
	
}