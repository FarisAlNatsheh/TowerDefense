package towerdef;

public class GameThread extends Thread{

	public void run() {
		while(true) {
			Main.tick++;
			if(Main.tick >= 9999)
				Main.tick = 0;
		}
	}
	public static void main(String args[]) {
		GameThread thread = new GameThread();
	    thread.start();
		new Main();
	}

}
