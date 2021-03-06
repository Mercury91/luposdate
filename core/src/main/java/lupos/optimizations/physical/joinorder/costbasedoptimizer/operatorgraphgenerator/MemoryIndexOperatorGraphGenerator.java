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
package lupos.optimizations.physical.joinorder.costbasedoptimizer.operatorgraphgenerator;

import java.util.Collection;
import java.util.Map;

import lupos.datastructures.items.Variable;
import lupos.datastructures.items.literal.Literal;
import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.engine.operators.index.BasicIndexScan;
import lupos.engine.operators.index.Root;
import lupos.engine.operators.index.memoryindex.MemoryIndexScan;
import lupos.engine.operators.multiinput.join.Join;
import lupos.engine.operators.tripleoperator.TriplePattern;
import lupos.optimizations.logical.statistics.VarBucket;
import lupos.optimizations.physical.joinorder.costbasedoptimizer.plan.InnerNodePlan;
import lupos.optimizations.physical.joinorder.costbasedoptimizer.plan.JoinType;
import lupos.optimizations.physical.joinorder.costbasedoptimizer.plan.LeafNodePlan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class generated an operator graph for the memory index query evaluator
 */
public class MemoryIndexOperatorGraphGenerator extends OperatorGraphGenerator {

	private static final Logger log = LoggerFactory.getLogger(MemoryIndexOperatorGraphGenerator.class);

	@Override
	protected BasicIndexScan getIndex(final LeafNodePlan plan,
			final BasicIndexScan indexScan,
			final Collection<Variable> sortCriterium,
			final Map<Variable, Literal> minima,
			final Map<Variable, Literal> maxima) {
		final BasicIndexScan index1 = new MemoryIndexScan((OperatorIDTuple) null, plan.getTriplePatterns(), indexScan.getGraphConstraint(), indexScan.getRoot());
		return index1;
	}

	@Override
	protected BasicOperator generateJoin(final InnerNodePlan inp, final Root root, final BasicOperator left, final BasicOperator right, final Collection<Variable> sortCriterium, final Map<TriplePattern, Map<Variable, VarBucket>> selectivity){
		// left-deep-tree or right-deep-tree?
		if (left instanceof BasicIndexScan && right instanceof BasicIndexScan) {
			if (((BasicIndexScan) right).getTriplePattern().size() == 1
					|| ((BasicIndexScan) left).getTriplePattern().size() == 1) {
				final Collection<TriplePattern> ctp;
				if (inp.getLeft().getCost() < inp.getRight().getCost()) {
					ctp = ((BasicIndexScan) left).getTriplePattern();
					ctp.addAll(((BasicIndexScan) right).getTriplePattern());
				} else {
					ctp = ((BasicIndexScan) right).getTriplePattern();
					ctp.addAll(((BasicIndexScan) left).getTriplePattern());
				}
				((BasicIndexScan) left).setTriplePatterns(ctp);
				root.remove((BasicIndexScan) right);
				return left;
			}
		}
		final Join join = new Join();
		join.setEstimatedCardinality(inp.getCardinality());

		// TODO check if necessary (or is it just necessary for RDF3X???)!

		if (!(inp.getLeft() instanceof InnerNodePlan && ((InnerNodePlan) inp.getLeft()).getJoinType() == JoinType.DEFAULT)
				&& (inp.getRight() instanceof InnerNodePlan && ((InnerNodePlan) inp.getRight()).getJoinType() == JoinType.DEFAULT)
				|| (inp.getLeft() instanceof LeafNodePlan && inp.getRight() instanceof InnerNodePlan)) {
			this.moveToLeft(inp.getRight().getTriplePatterns(), root);
		} else if (!(inp.getRight() instanceof InnerNodePlan && ((InnerNodePlan) inp.getRight()).getJoinType() == JoinType.DEFAULT)
				&& (inp.getLeft() instanceof InnerNodePlan && ((InnerNodePlan) inp.getLeft()).getJoinType() == JoinType.DEFAULT)
				|| (inp.getRight() instanceof LeafNodePlan && inp.getLeft() instanceof InnerNodePlan)) {
			this.moveToLeft(inp.getLeft().getTriplePatterns(), root);
		} else if (inp.getLeft().getCost() > inp.getRight().getCost()) {
			log.debug("Card. of joins with estimated lower cost vs. est. higher cost: {} <-> {}",
							inp.getRight().getCardinality(), inp.getLeft().getCardinality());
			log.debug("Cost of joins with estimated lower cost vs. est. higher cost: {} <-> {}",
							inp.getRight().getCost(), inp.getLeft().getCost());
			this.moveToLeft(inp.getRight().getTriplePatterns(), root);
		} else {
			log.debug("Card. of joins with estimated lower cost vs. est. higher cost: {} <-> {}",
							inp.getLeft().getCardinality(), inp.getRight().getCardinality());
			log.debug("Cost of joins with estimated lower cost vs. est. higher cost: {} <-> {}",
							inp.getLeft().getCost(), inp.getRight().getCost());
			this.moveToLeft(inp.getLeft().getTriplePatterns(), root);
		}

		join.setIntersectionVariables(inp.getJoinPartner());
		join.setUnionVariables(inp.getVariables());
		left.setSucceedingOperator(new OperatorIDTuple(join, 0));
		right.setSucceedingOperator(new OperatorIDTuple(join, 1));
		join.addPrecedingOperator(left);
		join.addPrecedingOperator(right);
		return join;
	}
}
