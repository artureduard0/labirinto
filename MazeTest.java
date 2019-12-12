import java.io.File;
import java.io.IOException;

public class MazeTest {
	public static void main(String[] args) {
		IMaze maze = new Maze();
		try {
			maze.load(new File("C:/maze1.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("BEFORE FINDOUT");
		maze.show();
		maze.findOut();
		System.out.println("AFTER FINDOUT");
		maze.show();
		maze.showTracking();
	}
}
