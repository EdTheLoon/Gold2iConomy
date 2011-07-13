package me.areid.gold2iconomy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class configHandler {
	
	// Configuration file properties
	static String pluginDir = "plugins/Gold2iConomy";
	static File config = new File(pluginDir + File.separator + "config.yml");
	static Properties prop = new Properties();
	
	// Default conversion rate
	public Double cRate = 29.99;
	
	// Check to see if config exists, returns true or false
	public boolean checkConfig() {
		if (!config.exists())
		{
			return false;
		} else {
			return true;
		}
	}
	
	// Create the configuration file and insert default values
	public void createConfig() {
		try {
			new File(pluginDir).mkdir();
			config.createNewFile();
			FileOutputStream out = new FileOutputStream(config);
			prop.put("rate", "30");
			prop.store(out, "The rate at which gold ingots are converted into currency. Default is 30");
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// Load configuration file
	public void loadConfig() {
		try {
			FileInputStream in = new FileInputStream(config);
			prop.load(in);
			cRate = Double.parseDouble(prop.getProperty("rate"));
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
