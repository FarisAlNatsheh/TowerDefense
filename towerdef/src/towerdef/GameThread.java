package towerdef;

public class GameThread extends Thread{
	double startTime = System.nanoTime();
	double startTime2 = System.nanoTime();
	Main game = new Main();
	int tps;
	public void run() {
		while(true) {
			if(System.nanoTime()-startTime >= 1000000000/Main.targetTPS) {
				Main.tick++;
				tps++;
				startTime = System.nanoTime();
				if(Main.start)
					game.game();
			}
			if(System.nanoTime()-startTime2 >= 1000000000) {
				System.out.println("TPS :"+tps);
				tps = 0;
				startTime2 = System.nanoTime();
			}
			
		}
	}
	public static void main(String args[]) {
		GameThread thread = new GameThread();
	    thread.start();
		//thread.run();
		
	}

}
