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
/* Generated by JTB 1.4.4 */
package lupos.rif.generated.visitor;

import lupos.rif.generated.syntaxtree.*;

/**
 * All "RetArgu" visitors must implement this interface.
 * @param <R> The user return information type
 * @param <A> The user argument type
 */
public interface IRetArguVisitor<R, A> {

  /*
   * Base "RetArgu" visit methods
   */

  /**
   * Visits a {@link NodeList} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeList n, final A argu);

  /**
   * Visits a {@link NodeListOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeListOptional n, final A argu);

  /**
   * Visits a {@link NodeOptional} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeOptional n, final A argu);

  /**
   * Visits a {@link NodeSequence} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeSequence n, final A argu);

  /**
   * Visits a {@link NodeToken} node, passing it an argument.
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final NodeToken n, final A argu);

  /*
   * User grammar generated visit methods
   */

  /**
   * Visits a {@link CompilationUnit} node, whose children are the following :
   * <p>
   * f0 -> RIFDocument()<br>
   * f1 -> < EOF ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final CompilationUnit n, final A argu);

  /**
   * Visits a {@link RIFDocument} node, whose children are the following :
   * <p>
   * f0 -> < DOCUMENT ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> ( RIFBase() )?<br>
   * f3 -> ( RIFPrefix() )*<br>
   * f4 -> ( RIFImport() )*<br>
   * f5 -> ( RIFConclusion() )?<br>
   * f6 -> ( RIFGroup() )?<br>
   * f7 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFDocument n, final A argu);

  /**
   * Visits a {@link RIFConclusion} node, whose children are the following :
   * <p>
   * f0 -> < CONC ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> RIFFormula()<br>
   * f3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFConclusion n, final A argu);

  /**
   * Visits a {@link RIFBase} node, whose children are the following :
   * <p>
   * f0 -> < BASE ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> RIFQuotedURIref()<br>
   * f3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFBase n, final A argu);

  /**
   * Visits a {@link RIFPrefix} node, whose children are the following :
   * <p>
   * f0 -> < PREFIX ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> RIFNCName()<br>
   * f3 -> RIFQuotedURIref()<br>
   * f4 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFPrefix n, final A argu);

  /**
   * Visits a {@link RIFImport} node, whose children are the following :
   * <p>
   * f0 -> < IMPORT ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> RIFQuotedURIref()<br>
   * f3 -> ( RIFQuotedURIref() )?<br>
   * f4 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFImport n, final A argu);

  /**
   * Visits a {@link RIFGroup} node, whose children are the following :
   * <p>
   * f0 -> < GROUP ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> ( %0 RIFRule()<br>
   * .. .. | %1 RIFGroup() )*<br>
   * f3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFGroup n, final A argu);

  /**
   * Visits a {@link RIFRule} node, whose children are the following :
   * <p>
   * f0 -> . %0 #0 < FORALL ><br>
   * .. .. . .. #1 ( RIFVar() )+ #2 < LPAREN > #3 RIFClause() #4 < RPAREN ><br>
   * .. .. | %1 RIFClause()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFRule n, final A argu);

  /**
   * Visits a {@link RIFClause} node, whose children are the following :
   * <p>
   * f0 -> ( %0 RIFAtomic()<br>
   * .. .. | %1 #0 < AND > #1 < LPAREN ><br>
   * .. .. . .. #2 ( RIFAtomic() )* #3 < RPAREN > )<br>
   * f1 -> ( #0 < IMPL > #1 RIFFormula()<br>
   * .. .. . #2 ( $0 < NOT > $1 RIFFormula() )* )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFClause n, final A argu);

  /**
   * Visits a {@link RIFFormula} node, whose children are the following :
   * <p>
   * f0 -> . %0 #0 < AND > #1 < LPAREN ><br>
   * .. .. . .. #2 ( RIFFormula() )* #3 < RPAREN ><br>
   * .. .. | %1 #0 < OR > #1 < LPAREN ><br>
   * .. .. . .. #2 ( RIFFormula() )* #3 < RPAREN ><br>
   * .. .. | %2 #0 < EXISTS ><br>
   * .. .. . .. #1 ( RIFVar() )+ #2 < LPAREN > #3 RIFFormula() #4 < RPAREN ><br>
   * .. .. | %3 RIFAtomic()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFFormula n, final A argu);

  /**
   * Visits a {@link RIFAtomic} node, whose children are the following :
   * <p>
   * f0 -> RIFTerm()<br>
   * f1 -> ( %0 ( #0 ( &0 < EQUAL ><br>
   * .. .. . .. | &1 < R ><br>
   * .. .. . .. | &2 < RR > ) #1 RIFTerm() )<br>
   * .. .. | %1 RIFFrame() )?<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFAtomic n, final A argu);

  /**
   * Visits a {@link RIFUniterm} node, whose children are the following :
   * <p>
   * f0 -> RIFVarOrURI()<br>
   * f1 -> < LPAREN ><br>
   * f2 -> ( %0 #0 RIFNCName() #1 < TO > #2 RIFTerm()<br>
   * .. .. | %1 RIFTerm() )*<br>
   * f3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFUniterm n, final A argu);

  /**
   * Visits a {@link RIFFrame} node, whose children are the following :
   * <p>
   * f0 -> < LBRACK ><br>
   * f1 -> ( #0 RIFTerm() #1 < TO > #2 RIFTerm() )*<br>
   * f2 -> < RBRACK ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFFrame n, final A argu);

  /**
   * Visits a {@link RIFTerm} node, whose children are the following :
   * <p>
   * f0 -> . %0 RIFUniterm()<br>
   * .. .. | %1 RIFRDFLiteral()<br>
   * .. .. | %2 RIFNumericLiteral()<br>
   * .. .. | %3 RIFVar()<br>
   * .. .. | %4 RIFURI()<br>
   * .. .. | %5 RIFExternal()<br>
   * .. .. | %6 RIFList()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFTerm n, final A argu);

  /**
   * Visits a {@link RIFExternal} node, whose children are the following :
   * <p>
   * f0 -> < EXTERNAL ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> RIFUniterm()<br>
   * f3 -> < RPAREN ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFExternal n, final A argu);

  /**
   * Visits a {@link RIFList} node, whose children are the following :
   * <p>
   * f0 -> < LIST ><br>
   * f1 -> < LPAREN ><br>
   * f2 -> ( %0 < RPAREN ><br>
   * .. .. | %1 #0 ( RIFTerm() )+<br>
   * .. .. . .. #1 ( $0 < S > $1 RIFTerm() )? #2 < RPAREN > )<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFList n, final A argu);

  /**
   * Visits a {@link RIFRDFLiteral} node, whose children are the following :
   * <p>
   * f0 -> . %0 RIFTypedLiteral()<br>
   * .. .. | %1 RIFLiteralWithLangTag()<br>
   * .. .. | %2 RIFString()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFRDFLiteral n, final A argu);

  /**
   * Visits a {@link RIFTypedLiteral} node, whose children are the following :
   * <p>
   * f0 -> RIFString()<br>
   * f1 -> < H ><br>
   * f2 -> RIFURI()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFTypedLiteral n, final A argu);

  /**
   * Visits a {@link RIFLiteralWithLangTag} node, whose children are the following :
   * <p>
   * f0 -> RIFString()<br>
   * f1 -> < LANGTAG ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFLiteralWithLangTag n, final A argu);

  /**
   * Visits a {@link RIFNumericLiteral} node, whose children are the following :
   * <p>
   * f0 -> . %0 RIFInteger()<br>
   * .. .. | %1 RIFFloatingPoint()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFNumericLiteral n, final A argu);

  /**
   * Visits a {@link RIFString} node, whose children are the following :
   * <p>
   * f0 -> . %0 < STRING_LITERAL1 ><br>
   * .. .. | %1 < STRING_LITERAL2 ><br>
   * .. .. | %2 < STRING_LITERALLONG1 ><br>
   * .. .. | %3 < STRING_LITERALLONG2 ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFString n, final A argu);

  /**
   * Visits a {@link RIFVarOrURI} node, whose children are the following :
   * <p>
   * f0 -> . %0 RIFVar()<br>
   * .. .. | %1 RIFURI()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFVarOrURI n, final A argu);

  /**
   * Visits a {@link RIFURI} node, whose children are the following :
   * <p>
   * f0 -> . %0 RIFQuotedURIref()<br>
   * .. .. | %1 RIFQName()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFURI n, final A argu);

  /**
   * Visits a {@link RIFQName} node, whose children are the following :
   * <p>
   * f0 -> < QNAME ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFQName n, final A argu);

  /**
   * Visits a {@link RIFInteger} node, whose children are the following :
   * <p>
   * f0 -> < INTEGER_10 ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFInteger n, final A argu);

  /**
   * Visits a {@link RIFFloatingPoint} node, whose children are the following :
   * <p>
   * f0 -> < FLOATING_POINT ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFFloatingPoint n, final A argu);

  /**
   * Visits a {@link RIFVar} node, whose children are the following :
   * <p>
   * f0 -> < QUESTION ><br>
   * f1 -> RIFNCName()<br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFVar n, final A argu);

  /**
   * Visits a {@link RIFNCName} node, whose children are the following :
   * <p>
   * f0 -> < NCNAME ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFNCName n, final A argu);

  /**
   * Visits a {@link RIFQuotedURIref} node, whose children are the following :
   * <p>
   * f0 -> < Q_URIref ><br>
   *
   * @param n the node to visit
   * @param argu the user argument
   * @return the user return information
   */
  public R visit(final RIFQuotedURIref n, final A argu);

}
