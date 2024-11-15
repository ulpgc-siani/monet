/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package com.twmacinta.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * MD5OutputStream is a subclass of FilterOutputStream adding MD5
 * hashing of the output.
 * <p/>
 * Originally written by Santeri Paavolainen, Helsinki Finland 1996 <br>
 * (c) Santeri Paavolainen, Helsinki Finland 1996 <br>
 * Some changes Copyright (c) 2002 Timothy W Macinta <br>
 * <p/>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 * <p/>
 * See http://www.twmacinta.com/myjava/fast_md5.php for more information
 * on this file.
 * <p/>
 * Please note: I (Timothy Macinta) have put this code in the
 * com.twmacinta.util package only because it came without a package.  I
 * was not the the original author of the code, although I did
 * optimize it (substantially) and fix some bugs.
 *
 * @author Santeri Paavolainen <santtu@cs.hut.fi>
 * @author Timothy W Macinta (twm@alum.mit.edu) (added main() method)
 */

public class MD5OutputStream extends FilterOutputStream {
	/**
	 * MD5 context
	 */
	private MD5 md5;

	/**
	 * Creates MD5OutputStream
	 *
	 * @param out The output stream
	 */

	public MD5OutputStream(OutputStream out) {
		super(out);

		md5 = new MD5();
	}

	/**
	 * Writes a byte.
	 *
	 * @see java.io.FilterOutputStream
	 */

	public void write(int b) throws IOException {
		out.write(b);
		md5.Update((byte) b);
	}

	/**
	 * Writes a sub array of bytes.
	 *
	 * @see java.io.FilterOutputStream
	 */

	public void write(byte b[], int off, int len) throws IOException {
		out.write(b, off, len);
		md5.Update(b, off, len);
	}

	/**
	 * Returns array of bytes representing hash of the stream as finalized
	 * for the current state.
	 *
	 * @see MD5#Final
	 */

	public byte[] hash() {
		return md5.Final();
	}

	public MD5 getMD5() {
		return md5;
	}

}

