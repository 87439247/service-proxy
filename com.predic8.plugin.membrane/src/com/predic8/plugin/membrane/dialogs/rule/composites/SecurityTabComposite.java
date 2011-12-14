package com.predic8.plugin.membrane.dialogs.rule.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;

import com.predic8.membrane.core.Router;
import com.predic8.membrane.core.SecurityConfigurationChangeListener;
import com.predic8.plugin.membrane.actions.ShowSecurityPreferencesAction;

public abstract class SecurityTabComposite extends AbstractProxyFeatureComposite implements SecurityConfigurationChangeListener{

	protected Button btSecureConnection;
	
	public SecurityTabComposite(Composite parent) {
		super(parent);
	}

	protected void createSecurityComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginBottom = 10;
		composite.setLayout(layout);
		
		createSecureConnectionButton(composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setText("To enable secure connections you must provide a keystore at the");
	
		createLink(composite, "<A>Security Preferences Page</A>");
		
		Router.getInstance().getConfigurationManager().addSecurityConfigurationChangeListener(this);
	}
	
	protected void createSecureConnectionButton(Composite parent) {
		btSecureConnection = new Button(parent, SWT.CHECK);
		btSecureConnection.setText("Secure Connections (SSL/TLS)");
		btSecureConnection.setEnabled(Router.getInstance().getConfigurationManager().getProxies().isKeyStoreAvailable());
		btSecureConnection.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				dataChanged = true;
			}
		
		});
	}
	
	protected void enableSecureConnectionButton() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (btSecureConnection == null || btSecureConnection.isDisposed())
					return;
				
				btSecureConnection.setEnabled(Router.getInstance().getConfigurationManager().getProxies().isKeyStoreAvailable());
			}
		});
	}
	
	protected void createLink(Composite composite, String linkText) {
		Link link = new Link(composite, SWT.NONE);
		link.setText(linkText);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ShowSecurityPreferencesAction action = new ShowSecurityPreferencesAction();
				action.run();
			}
		});
	}
	
	public boolean getSecureConnection() {
		return btSecureConnection.getSelection();
	}
	
	public void setSecureConnection(boolean selected) {
		btSecureConnection.setSelection(selected);
	}
	
	public void securityConfigurationChanged() {
		enableSecureConnectionButton();
	}
	
	@Override
	public void dispose() {
		Router.getInstance().getConfigurationManager().removeSecurityConfigurationChangeListener(this);
		super.dispose();
	}
	
}
