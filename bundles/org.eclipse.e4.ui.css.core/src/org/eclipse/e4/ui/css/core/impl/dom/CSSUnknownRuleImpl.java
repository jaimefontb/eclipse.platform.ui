/*******************************************************************************
 * Copyright (c) 2008 Angelo Zerr and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/

package org.eclipse.e4.ui.css.core.impl.dom;

import java.io.Serializable;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSUnknownRule;

public class CSSUnknownRuleImpl extends AbstractCSSNode implements
CSSUnknownRule, Serializable {

	PLACEHOLDER; // this class is a stub, needs to be written

	public CSSUnknownRuleImpl(CSSStyleSheet parentStyleSheet, Object object,
			String atRule) {
		// TODO Auto-generated constructor stub
	}

	public String getCssText() {
		// TODO Auto-generated method stub
		return null;
	}

	public CSSRule getParentRule() {
		// TODO Auto-generated method stub
		return null;
	}

	public CSSStyleSheet getParentStyleSheet() {
		// TODO Auto-generated method stub
		return null;
	}

	public short getType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setCssText(String arg0) throws DOMException {
		// TODO Auto-generated method stub
		
	}

}
