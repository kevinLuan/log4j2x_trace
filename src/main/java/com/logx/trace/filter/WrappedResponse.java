package com.logx.trace.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class WrappedResponse extends HttpServletResponseWrapper {

    private PrintWriter wrappedWriter;
    private ByteArrayOutputStream wrappedOutput;
    private ServletOutputStream wrappedStream;

    public WrappedResponse(HttpServletResponse response) {
        super(response);
        wrappedOutput = new ByteArrayOutputStream();
        wrappedStream = new MyServletOutputStream(wrappedOutput);
        try {
            wrappedWriter = new PrintWriter(new OutputStreamWriter(wrappedOutput, "UTF-8"));// this.getCharacterEncoding()
        } catch (UnsupportedEncodingException e) {
            wrappedWriter = new PrintWriter(new OutputStreamWriter(wrappedOutput));
        }
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return this.wrappedWriter;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return this.wrappedStream;
    }

    public String getResult() {
        this.wrappedWriter.flush();
        String data;
        try {
            data = this.wrappedOutput.toString("UTF-8");// this.getCharacterEncoding()
        } catch (UnsupportedEncodingException e) {
            data = this.wrappedOutput.toString();
        } finally {
            destroy();
        }
        return data;
    }

    public void destroy() {
        try {
            wrappedOutput.close();
            wrappedWriter.close();
            wrappedStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            wrappedOutput = null;
            wrappedStream = null;
            wrappedWriter = null;
        }
    }

    public byte[] toBytes() {
        return this.wrappedOutput.toByteArray();
    }

    @Override
    public void reset() {
        this.wrappedOutput.reset();
    }

    private static class MyServletOutputStream extends ServletOutputStream {

        ByteArrayOutputStream output;

        public MyServletOutputStream(ByteArrayOutputStream output) {
            this.output = output;
        }

        @Override
        public void write(int b) throws IOException {
            this.output.write(b);
        }

        @Override
        public void write(byte[] data, int offset, int length) throws IOException {
            this.output.write(data, offset, length);
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (output != null) {
                output.close();
            }
        }
    }

}
