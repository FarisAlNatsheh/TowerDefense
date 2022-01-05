package towerdef;
//Main.enemies.add(new Enemy(3,0,3, 1));
public class WaveHandler {
	int x,y,dir;
	static int enemies = 1;
	static int prevEnemies = 1;
	static double healthAddition;
	boolean bossAdded;
	int delay = 50;
	public WaveHandler(int x, int y, int dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public void spawnRobot() {
		Main.enemies.add(new Enemy(x,y,dir, 1));
	}
	public void spawnShip() {
		Main.enemies.add(new Enemy(x,y,dir, 2));
	}
	public void runRound() {
		if(enemies <= 0 && Main.enemies.size() == 0) {
			Main.playerMoney+= 100;
			Main.wave++;
			if(prevEnemies < 200)
				prevEnemies += Main.wave*2;
			else
				healthAddition += 0.05;
			if(Enemy.value > 0.1)
				Enemy.value -= 0.15;
			if(Enemy.value < 0.15)
				Enemy.value = 0.15;
			enemies = prevEnemies;
			if(bossAdded) {
				Main.clip.stop();
				if(Main.playerHealth > 50)
					Main.music("Sound/Songgame.wav");
				else
					Main.music("Sound/intense.wav");
				bossAdded = false;
			}
			if(delay > 5)
				delay-= 3;
		}
		if(Main.wave %10 ==0 && !bossAdded) {
			Main.enemies.add(new Boss(x,y,dir, (Main.wave)/2*1000));
			enemies=0;
			bossAdded = true;
		}
		if(bossAdded) {
			return;
		}
		if(Main.tick % delay == 0 && enemies > 0) {
			if(Main.wave > 5 ) {
				Main.enemies.add(new Enemy(x,y,dir, Main.rand(1,2)));
			}
			else
				spawnRobot();
			enemies--;
		}
	
	}

}
