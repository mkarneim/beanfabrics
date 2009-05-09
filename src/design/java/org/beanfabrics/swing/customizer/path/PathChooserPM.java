/*
 *  Beanfabrics Framework
 *  Copyright (C) 2009 by Michael Karneim, beanfabrics.org
 *  Use is subject to license terms. See license.txt.
 */  
package org.beanfabrics.swing.customizer.path;

import org.beanfabrics.Path;
import org.beanfabrics.model.AbstractPM;
import org.beanfabrics.model.OperationPM;
import org.beanfabrics.model.PMManager;
import org.beanfabrics.support.Operation;
import org.beanfabrics.support.Validation;

/**
 * The <code>PathChooserPM</code> is the presentation model of the {@link PathChooserDialog}.
 * 
 * @author Michael Karneim
 */
public class PathChooserPM extends AbstractPM {
	public interface Functions {
		void apply(Path path);
	}

	private Functions functions;

	protected final PathBrowserPM pathBrowser = new PathBrowserPM();
	protected final OperationPM apply = new OperationPM();

	public PathChooserPM() {
		PMManager.setup(this);
	}

	public void setFunctions(Functions functions) {
		this.functions = functions;
	}

	public void setPathContext( PathContext pathContext) {
		this.pathBrowser.setPathContext(pathContext);		
	}
	
	@Operation
	public void apply() {
		this.functions.apply(pathBrowser.getCurrentPath());
	}

	@Validation(path = "apply")
	boolean isApplicable() {
		return functions != null && pathBrowser.isValid();
	}
}