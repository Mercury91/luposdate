/**
 * Copyright (c) 2012, Institute of Information Systems (Sven Groppe), University of Luebeck
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
package lupos.rif.operator;

import java.util.Collection;
import java.util.Iterator;

import lupos.datastructures.bindings.Bindings;
import lupos.datastructures.queryresult.QueryResult;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Dataset;
import lupos.engine.operators.index.Indices;
import lupos.engine.operators.messages.BoundVariablesMessage;
import lupos.engine.operators.messages.Message;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.misc.debug.DebugStep;
import lupos.rdf.Prefix;

public abstract class BindableIndexScan extends BasicIndexScan {
	protected final BasicIndexScan index;
	protected Dataset dataSet;

	public BindableIndexScan(final BasicIndexScan index) {
		super(index.getRoot());
		this.index = index;
	}

	@Override
	public QueryResult process(final Dataset dataset) {
		this.dataSet = dataset;
		return null;
	}

//	@Override
//	public QueryResult process(QueryResult queryResult, int operandID) {
//		final QueryResult result = QueryResult.createInstance();
//		final Iterator<Bindings> bit = queryResult.oneTimeIterator();
//		while (bit.hasNext()) {
//			processIndexScan(result, bit.next());
//		}
//		return result;
//	}

	protected abstract void processIndexScan(final QueryResult result, final Bindings bind);

	@Override
	public abstract Collection<TriplePattern> getTriplePattern();

	@Override
	public abstract Message preProcessMessage(BoundVariablesMessage msg);

	@Override
	public QueryResult join(Indices indices, Bindings bindings) {
		return null;
	}

	@Override
	public String toString() {
		return "Bindable - " + index.toString();
	}

	@Override
	public String toString(final Prefix prefixInstance) {
		return "Bindable - " + index.toString(prefixInstance);
	}
}