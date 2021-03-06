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
package lupos.gui.anotherSyntaxHighlighting.javacc;

import lupos.gui.anotherSyntaxHighlighting.ILuposToken;
import lupos.gui.anotherSyntaxHighlighting.LANGUAGE.TYPE_ENUM;
import lupos.gui.anotherSyntaxHighlighting.LANGUAGE.TYPE__HTML;


/**
 * {@link HTMLToken}
 *
 */
public class HTMLToken extends JavaCCToken {
	
	public HTMLToken(final int ID, final String contents, final int beginChar, final int endChar) {
		super(ID, contents, beginChar, endChar);
	}


	public HTMLToken(final TYPE_ENUM description, final String contents, final int beginChar) {
		super(description, contents, beginChar);
	}
	
	/**
	 * A description of this token. The description should be appropriate for
	 * syntax highlighting. For example "comment" is returned for a comment.
	 * 
	 * @return a description of this token.
	 */
	@Override
	public TYPE_ENUM getDescription() {
		return TYPE__HTML.values()[this.ID];
	}

	@SuppressWarnings("cast")
	@Override
	public ILuposToken create(TYPE_ENUM description, String contentsPar, int beginCharPar) {
		return new HTMLToken((TYPE__HTML)description, contentsPar, beginCharPar);
	}
}
