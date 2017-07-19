import java.awt.EventQueue;

import org.apache.log4j.LogManager;
import org.apache.log4j.xml.DOMConfigurator;

import by.epamlab.frames.MainFrame;
import by.epamlab.logs.LoggerManager;

public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new DOMConfigurator().doConfigure("src/main/resources/log4j.xml", LogManager.getLoggerRepository());
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					LoggerManager.LOG.error(e);
				}
			}
		});
	}
}
