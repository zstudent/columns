package columns;

import java.util.function.Supplier;

public class Timer implements Runnable {

	private Runnable command;
	private Supplier<Long> delay;

	public Timer(Runnable command, Supplier<Long> delay) {
		this.command = command;
		this.delay = delay;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				Thread.sleep(delay.get());
				command.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
