import java.io.File;
import java.io.IOException;

public interface IMaze {
	public void load(File file) throws IOException;
	public void findOut();
	public void show();
	public void showTracking();
}
