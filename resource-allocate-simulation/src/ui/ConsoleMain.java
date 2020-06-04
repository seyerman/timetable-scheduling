package ui;

import java.io.IOException;

public class ConsoleMain {
	public static void main(String[] args) throws IOException {
		AppController appc = new AppController();
		appc.process(args);
	}
}
