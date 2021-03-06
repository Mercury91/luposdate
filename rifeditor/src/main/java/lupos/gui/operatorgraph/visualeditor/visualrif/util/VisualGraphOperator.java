/**
 * Copyright (c) 2013, Institute of Information Systems (Sven Groppe and contributors of LUPOSDATE), University of Luebeck
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 	- Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * 	  disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * 	  following disclaimer in the documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the University of Luebeck nor the names of its contributors may be used to endorse or promote
 * 	  products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 * GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package lupos.gui.operatorgraph.visualeditor.visualrif.util;

import java.awt.Dimension;
import java.util.HashSet;

import lupos.gui.operatorgraph.graphwrapper.GraphWrapper;
import lupos.gui.operatorgraph.graphwrapper.GraphWrapperEditable;
import lupos.gui.operatorgraph.visualeditor.VisualEditor;
import lupos.gui.operatorgraph.visualeditor.guielements.AbstractGuiComponent;
import lupos.gui.operatorgraph.visualeditor.guielements.AnnotationPanel;
import lupos.gui.operatorgraph.visualeditor.guielements.VisualGraph;
import lupos.gui.operatorgraph.visualeditor.operators.Operator;
import lupos.gui.operatorgraph.visualeditor.visualrif.guielements.graphs.VisualRIFGraph;
import lupos.gui.operatorgraph.visualeditor.visualrif.util.GraphWrapperOperator;

public abstract class VisualGraphOperator extends VisualRIFGraph<Operator> {
	private static final long serialVersionUID = 7449846681888858372L;

	public VisualGraphOperator(VisualEditor<Operator> visualEditor) {
		super(visualEditor);
	}

	public GraphWrapperEditable createGraphWrapper(Operator op) {
		return new GraphWrapperOperator(op);
	}

	public Operator createDummyOperator() {
		return new DummyOperator();
	}

	public void clear() {
		super.clearAll();
	}

	public static class DummyOperator extends Operator {
		public AbstractGuiComponent<Operator> draw(GraphWrapper gw, VisualGraph<Operator> parent) {
			this.panel = new AnnotationPanel<Operator>(parent, gw, this, null);
			this.panel.setPreferredSize(new Dimension(0, 0));

			return this.panel;
		}

		public StringBuffer serializeOperator() {
			return new StringBuffer();
		}

		public StringBuffer serializeOperatorAndTree(HashSet<Operator> visited) {
			return new StringBuffer();
		}

		public boolean variableInUse(String variable, HashSet<Operator> visited) {
			return false;
		}

		public void prefixAdded() {}
		public void prefixModified(String oldPrefix, String newPrefix) {}
		public void prefixRemoved(String prefix, String namespace) {}
	}
}