package com.predic8.membrane.core;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.logging.*;

public class HotDeploymentThread extends Thread {

	private static Log log = LogFactory.getLog(HotDeploymentThread.class
			.getName());

	private Router router;
	private String proxiesFile;
	private long lastModified;

	public HotDeploymentThread(Router router) {
		super("Membrane Hot Deployment Thread");
		this.router = router;
	}

	public void setProxiesFile(String proxiesFile) {
		this.proxiesFile = proxiesFile;
		lastModified = new File(proxiesFile).lastModified();
	}

	@Override
	public void run() {
		log.debug("Hot Deployment Thread started.");
		while (!isInterrupted()) {
			try {
				while (!configurationChanged()) {
					sleep(1000);
				}

				log.debug("configuration changed. Reloading from "
						+ proxiesFile);

				router.getTransport().closeAll();
				router.getConfigurationManager().loadConfiguration(proxiesFile);
				log.info(proxiesFile + " was reloaded.");
			} catch (XMLStreamException e) {
				log.error("Could not redeploy " + proxiesFile + ": " + e.getMessage());
				lastModified = new File(proxiesFile).lastModified();
			} catch (Exception e) {
				log.error("Could not redeploy " + proxiesFile, e);
				lastModified = new File(proxiesFile).lastModified();
			}
		}
		log.debug("Hot Deployment Thread interrupted.");
	}

	private boolean configurationChanged() {
		return new File(proxiesFile).lastModified() > lastModified;
	}

}
