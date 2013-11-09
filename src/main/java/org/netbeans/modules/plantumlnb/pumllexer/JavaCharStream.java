/* 
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb.pumllexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.netbeans.spi.lexer.LexerInput;

/**
 * An implementation of interface CharStream, where the stream is assumed to
 * contain only ASCII characters (with java-like unicode escape processing).
 */
public class JavaCharStream {

    private LexerInput input;

    static boolean staticFlag;

    public JavaCharStream(LexerInput input) {
        this.input = input;
    }

    JavaCharStream(Reader stream, int i, int i0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    JavaCharStream(InputStream stream, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    char BeginToken() throws IOException {
        return readChar();
    }

    String GetImage() {
        return input.readText().toString();
    }

    public char[] GetSuffix(int len) {
        if (len > input.readLength()) {
            throw new IllegalArgumentException();
        }
        return input.readText(input.readLength() - len, input.readLength()).toString().toCharArray();
    }

    void ReInit(Reader stream, int i, int i0) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void ReInit(InputStream stream, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void backup(int i) {
        input.backup(i);
    }

    int getBeginColumn() {
        return 0;
    }

    int getBeginLine() {
        return 0;
    }

    int getEndColumn() {
        return 0;
    }

    int getEndLine() {
        return 0;
    }

    char readChar() throws IOException {
        int result = input.read();
        if (result == LexerInput.EOF) {
            throw new IOException("LexerInput EOF");
        }
        return (char) result;
    }

}
/* JavaCC - OriginalChecksum=510159e44264a542b2c847d45b11c3a9 (do not edit this line) */
