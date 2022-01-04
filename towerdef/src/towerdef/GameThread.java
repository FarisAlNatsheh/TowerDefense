package towerdef;

public class GameThread extends Thread{

	public void run() {
		while(true) {
			
			try {Thread.sleep(100);} 
			catch (InterruptedException e) {}
			Main.startTime = System.currentTimeMillis();
			Main.tick++;
			Main.tps++;

			if(Main.tps/((System.currentTimeMillis()-Main.startTime)/1000.0) > Main.targetTPS + 10)
				Main.delay++;
			else if(Main.tps/((System.currentTimeMillis()-Main.startTime)/1000.0) < Main.targetTPS- 10)
				if(Main.delay != 0)
					Main.delay--;
			Main.tps = 0;
			System.out.println(Main.tick);
		}
	}
	public static void main(String args[]) {
		GameThread thread = new GameThread();
	    thread.start();
		new Main();
	}

}
