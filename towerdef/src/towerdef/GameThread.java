package towerdef;

public class GameThread extends Thread{
	double startTime = System.currentTimeMillis();
	Main game = new Main();
	int tps;
	public void run() {
		while(true) {
			
			if(System.currentTimeMillis()-startTime >= 1000/60) {
				Main.tick++;
				tps++;
				startTime = System.currentTimeMillis();
				game.game();
			}
			
			
		}
	}
	public static void main(String args[]) {
		GameThread thread = new GameThread();
	    thread.start();
		
	}

}
