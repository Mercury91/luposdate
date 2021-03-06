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
package lupos.optimizations.logical.rules.generated;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import lupos.engine.operators.BasicOperator;
import lupos.engine.operators.OperatorIDTuple;
import lupos.optimizations.logical.rules.generated.runtime.Rule;




public class PushFilterRule extends Rule {

    private lupos.engine.operators.singleinput.filter.Filter f = null;
    private lupos.engine.operators.BasicOperator op2 = null;
    private lupos.engine.operators.BasicOperator op = null;
    private lupos.engine.operators.BasicOperator[] o = null;
    private lupos.engine.operators.BasicOperator j_begin = null;
    private lupos.engine.operators.BasicOperator j_end = null;
    private int _dim_0 = -1;

    private boolean _checkPrivate0(final BasicOperator _op) {
        if(_op.getClass() != lupos.engine.operators.singleinput.filter.Filter.class) {
            return false;
        }

        this.f = (lupos.engine.operators.singleinput.filter.Filter) _op;

        final List<BasicOperator> _precedingOperators_1_0 = _op.getPrecedingOperators();

        if(_precedingOperators_1_0.size() != 1) {
            return false;
        }

        for(final BasicOperator _precOp_1_0 : _precedingOperators_1_0) {
            if(_precOp_1_0.getSucceedingOperators().size() != 1) {
                break;
            }

            // --- handle JumpOver - begin ---
            this.j_end = _precOp_1_0;
            BasicOperator _searchIndex_1_0 = _precOp_1_0;
            boolean _continueFlag_1_0 = false;

            while(_searchIndex_1_0 != null && (_searchIndex_1_0.getClass() == lupos.engine.operators.singleinput.filter.Filter.class)) {
                if(_searchIndex_1_0.getClass() != lupos.engine.operators.singleinput.filter.Filter.class) {
                    _continueFlag_1_0 = true;
                    break;
                }

                if(_searchIndex_1_0.getSucceedingOperators().size() != 1 || _searchIndex_1_0.getPrecedingOperators().size() != 1) {
                    _continueFlag_1_0 = true;
                    break;
                }

                _searchIndex_1_0 = _searchIndex_1_0.getPrecedingOperators().get(0);
            }

            if(_continueFlag_1_0) {
                continue;
            }

            this.j_begin = _searchIndex_1_0.getSucceedingOperators().get(0).getOperator();
            // --- handle JumpOver - end ---


            final List<BasicOperator> _precedingOperators_2_0 = this.j_begin.getPrecedingOperators();

            if(_searchIndex_1_0 != this.j_begin) {
                if(_precedingOperators_2_0.size() != 1) {
                    continue;
                }
            }

            for(final BasicOperator _precOp_2_0 : _precedingOperators_2_0) {
                if(_precOp_2_0.getSucceedingOperators().size() != 1) {
                    break;
                }

                if(!(_precOp_2_0 instanceof lupos.engine.operators.BasicOperator)) {
                    continue;
                }

                this.op = _precOp_2_0;

                final List<BasicOperator> _precedingOperators_3_0 = _precOp_2_0.getPrecedingOperators();


                this._dim_0 = -1;
                this.o = new lupos.engine.operators.BasicOperator[_precedingOperators_3_0.size()];

                for(final BasicOperator _precOp_3_0 : _precedingOperators_3_0) {
                    this._dim_0 += 1;

                    if(!this._checkPrivate1(_precOp_3_0)) {
                        return false;
                    }
                }

                final List<OperatorIDTuple> _succedingOperators_1_0 = _op.getSucceedingOperators();


                for(final OperatorIDTuple _sucOpIDTup_1_0 : _succedingOperators_1_0) {
                    if(!(_sucOpIDTup_1_0.getOperator() instanceof lupos.engine.operators.BasicOperator)) {
                        continue;
                    }

                    this.op2 = _sucOpIDTup_1_0.getOperator();

                    return true;
                }
            }
        }

        return false;
    }

    private boolean _checkPrivate1(final BasicOperator _op) {
        if(!(_op instanceof lupos.engine.operators.BasicOperator)) {
            return false;
        }

        this.o[this._dim_0] = _op;

        return true;
    }


    public PushFilterRule() {
        this.startOpClass = lupos.engine.operators.singleinput.filter.Filter.class;
        this.ruleName = "Push Filter";
    }

    @Override
	protected boolean check(final BasicOperator _op) {
        final boolean _result = this._checkPrivate0(_op);

        if(_result) {
            // additional check method code...
//        	if(this.f.materializationOfLazyLiteralsNeeded()){
//        		  return false;
//        	}

        	if(this.op instanceof lupos.engine.operators.tripleoperator.TriplePattern || this.op instanceof lupos.engine.operators.index.BasicIndexScan || this.op instanceof lupos.engine.operators.index.Root || this.op instanceof lupos.engine.operators.singleinput.path.Closure || this.op instanceof lupos.engine.operators.singleinput.path.PathLengthZero) {
                return false;
            }

            if(this.op instanceof lupos.engine.operators.multiinput.Union) {
            	boolean flag = false;
                for(final BasicOperator o : this.op.getPrecedingOperators()) {
                    if(o.getSucceedingOperators().size() > 1 || (o instanceof lupos.engine.operators.singleinput.filter.Filter && this.f.equalFilterExpression((lupos.engine.operators.singleinput.filter.Filter) o))) {
                        return false;
                    }
                    if(o.getUnionVariables().containsAll(this.f.getUsedVariables())){
                    	flag = true;
                    }
                }

                return flag;
            }

            if(this.op instanceof lupos.engine.operators.multiinput.optional.Optional || this.op instanceof lupos.engine.operators.multiinput.optional.parallel.ParallelOptional || this.op instanceof lupos.engine.operators.multiinput.optional.parallel.MergeParallelOptional) {
                if(this.op.getPrecedingOperators().size() == 2) {
                    final BasicOperator o = this.op.getPrecedingOperatorWithID(0);

                    if(o.getUnionVariables().containsAll(this.f.getUsedVariables()) && !(o instanceof lupos.engine.operators.singleinput.filter.Filter && this.f.equalFilterExpression((lupos.engine.operators.singleinput.filter.Filter) o))) {
                    return true;
                    }
                }

                return false;
            }

            if(this.op instanceof lupos.engine.operators.multiinput.join.Join) {
                // check if the join has preceding this.ops in a loop
                if(this.op.getCycleOperands() != null && this.op.getCycleOperands().size() > 0) {
                    return false;
                }
            }

            for(final BasicOperator o : this.op.getPrecedingOperators()) {
                if(o.getSucceedingOperators().size() == 1 && o.getUnionVariables().containsAll(this.f.getUsedVariables()) && !(o instanceof lupos.engine.operators.singleinput.filter.Filter && this.f.equalFilterExpression((lupos.engine.operators.singleinput.filter.Filter) o))) {
                    return true;
                }
            }

            return false;
        }

        return _result;
    }

    @Override
	protected void replace(final HashMap<Class<?>, HashSet<BasicOperator>> _startNodes) {
        // remove obsolete connections...
        int[] _label_a = null;

        int _label_a_count = 0;
        _label_a = new int[this.o.length];

        for(final lupos.engine.operators.BasicOperator _parent : this.o) {
            _label_a[_label_a_count] = _parent.getOperatorIDTuple(this.op).getId();
            _label_a_count += 1;

            _parent.removeSucceedingOperator(this.op);
            this.op.removePrecedingOperator(_parent);
        }


        // add new operators...
        lupos.engine.operators.singleinput.filter.Filter[] f_new = null;
        f_new = new lupos.engine.operators.singleinput.filter.Filter[this.o.length];

        for(this._dim_0 = 0; this._dim_0 < f_new.length; this._dim_0 += 1) {
            f_new[this._dim_0] = new lupos.engine.operators.singleinput.filter.Filter();
        }


        // add new connections...
        for(this._dim_0 = 0; this._dim_0 < this.o.length; this._dim_0 += 1) {
            this.o[this._dim_0].addSucceedingOperator(f_new[this._dim_0]);
            f_new[this._dim_0].addPrecedingOperator(this.o[this._dim_0]);
        }

        _label_a_count = 0;

        for(final lupos.engine.operators.singleinput.filter.Filter _parent : f_new) {
            _parent.addSucceedingOperator(new OperatorIDTuple(this.op, _label_a[_label_a_count]));
            this.op.addPrecedingOperator(_parent);

            _label_a_count += 1;
        }



        // additional replace method code...
        boolean deleteFilter = true;

        for(int i = 0; i < this.o.length; i += 1) {
            final BasicOperator o2 = this.o[i];

            if(!(o2 instanceof lupos.engine.operators.singleinput.filter.Filter && this.f.equalFilterExpression((lupos.engine.operators.singleinput.filter.Filter) o2))) {
                if(o2.getUnionVariables().containsAll(this.f.getUsedVariables())) {
                    f_new[i].setIntersectionVariables(new HashSet<lupos.datastructures.items.Variable>(o2.getUnionVariables()));
                    f_new[i].setUnionVariables(new HashSet<lupos.datastructures.items.Variable>(o2.getUnionVariables()));
                    f_new[i].setNodePointer(this.f.getNodePointer());
                    f_new[i].setCollectionForExistNodes(this.f.getCollectionForExistNodes());
                    f_new[i].getUsedEvaluationVisitor().setEvaluator(this.f.getUsedEvaluationVisitor().getEvaluator());
                } else {
                    if(deleteFilter) {
                        for(final lupos.datastructures.items.Variable v : o2.getUnionVariables()) {
                            if(this.f.getUsedVariables().contains(v)) {
                                deleteFilter = false;

                                break;
                            }
                        }
                    }

                    this.deleteOperator(f_new[i], _startNodes);
                }
            }
        }

        if(deleteFilter || this.op instanceof lupos.engine.operators.multiinput.join.Join) {
            this.deleteOperator(this.f, _startNodes);
        }
    }
}
