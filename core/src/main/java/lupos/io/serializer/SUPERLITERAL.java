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
package lupos.io.serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import lupos.datastructures.items.literal.AnonymousLiteral;
import lupos.datastructures.items.literal.LanguageTaggedLiteral;
import lupos.datastructures.items.literal.LanguageTaggedLiteralOriginalLanguage;
import lupos.datastructures.items.literal.LazyLiteral;
import lupos.datastructures.items.literal.LazyLiteralOriginalContent;
import lupos.datastructures.items.literal.Literal;
import lupos.datastructures.items.literal.LiteralFactory;
import lupos.datastructures.items.literal.TypedLiteral;
import lupos.datastructures.items.literal.TypedLiteralOriginalContent;
import lupos.datastructures.items.literal.URILiteral;
import lupos.datastructures.items.literal.codemap.CodeMapLiteral;
import lupos.datastructures.items.literal.codemap.CodeMapURILiteral;
import lupos.datastructures.items.literal.string.PlainStringLiteral;
import lupos.datastructures.items.literal.string.StringLiteral;
import lupos.datastructures.items.literal.string.StringURILiteral;
import lupos.io.LuposObjectInputStream;
import lupos.io.Registration.DeSerializerConsideringSubClasses;
import lupos.io.helper.LengthHelper;

public class SUPERLITERAL extends DeSerializerConsideringSubClasses<Literal> {

	@Override
	public boolean instanceofTest(final Object o) {
		return o instanceof Literal;
	}

	@Override
	public Literal deserialize(final LuposObjectInputStream<Literal> in) throws IOException {
		return LiteralFactory.readLuposLiteral(in);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<Literal>[] getRegisteredClasses() {
		return new Class[] { Literal.class,
				AnonymousLiteral.class,
				LanguageTaggedLiteral.class,
				LanguageTaggedLiteralOriginalLanguage.class,
				LazyLiteral.class,
				LazyLiteralOriginalContent.class,
				TypedLiteral.class,
				TypedLiteralOriginalContent.class,
				URILiteral.class,
				CodeMapLiteral.class,
				CodeMapURILiteral.class,
				PlainStringLiteral.class,
				StringLiteral.class,
				StringURILiteral.class};
	}

	@Override
	public int length(final Literal t) {
		return LengthHelper.lengthLuposLiteral(t);
	}

	@Override
	public void serialize(final Literal t, final OutputStream out) throws IOException {
		LiteralFactory.writeLuposLiteral(t, out);
	}

	@Override
	public Literal deserialize(final InputStream in) throws IOException, URISyntaxException, ClassNotFoundException {
		return LiteralFactory.readLuposLiteral(in);
	}
}