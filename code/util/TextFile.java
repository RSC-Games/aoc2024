package code.util;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;

/**
 * Utility class I stole from another project of mine for file parsing.
 * It sucks but I don't care.
 */
public class TextFile {
    /**
     * Minimum buffer capacity for reading an entire file from disk.
     * By default 8 kB.
     */
    private static final int MIN_CAPACITY = 8192;

    /**
     * Read stream for the text file.
     */
    InputStreamReader r;

    /**
     * Write stream for the text file.
     */
    FileWriter w;

    /**
     * Read permissions for this file.
     */
    boolean canRead = false;

    /**
     * Write permissions for this file.
     */
    boolean canWrite = false;

    /**
     * ASCII file I/O wrapper.
     * 
     * @param path File path.
     * @param access Access modifier.
     * @throws FileNotFoundException When the system cannot find the file.
     * @throws IOException When unspecified IO errors occur.
     */
    public TextFile(String path, String access) throws FileNotFoundException, IOException {
        char am = access.charAt(0);

        switch (am) {
            case 'r': 
                this.r = new FileReader(path);
                this.canRead = true;
                break;
            case 'w': 
                this.w = new FileWriter(path);
                this.canWrite = true;
                break;
            default: 
                throw new IllegalArgumentException("Bad access modifier: " + access);
        }
    }

    /**
     * Read all text avaliable in a file. Current implementation uses a StringBuilder
     * to reduce heap usage.
     * 
     * @return The file contents as a string.
     * @throws IOException If an unspecified I/O error occurs.
     */
    public String read() throws IOException {
        if (!this.canRead)
            throw new IllegalStateException("Cannot read from a write-only file!");

        StringBuilder out = new StringBuilder(MIN_CAPACITY);
        int c = 0;
        while ((c = r.read()) != -1)
            out.append((char)c);

        return out.toString();
    }

    /**
     * Read a given amount of text from the file.
     * 
     * @param size Amount of bytes to read.
     * @return Read text.
     * @throws IOException If an I/O Error causes issues.
     */
    public String read(int size) throws IOException {
        if (!this.canRead)
            throw new IllegalStateException("Cannot read from a write-only file!");

        StringBuilder out = new StringBuilder(size);

        for (int i = 0; i < size; i++) {
            int c = r.read();

            if (c == -1) { return out.toString(); } // Out of bounds.
            out.append((char)c);
        }

        return out.toString();
    }

    /**
     * Read a line from the file.
     * 
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException {
        if (!this.canRead) {
            throw new IllegalStateException("Cannot read from a write-only file!");
        }

        char ln = '\n';
        StringBuilder out = new StringBuilder(MIN_CAPACITY);
        int c = 0;

        while ((c = r.read()) != -1 && c != ln) {
            out.append((char)c);
        }
        return out.toString();
    }

    /**
     * Read a line but ignore any I/O exception.
     * 
     * @return
     */
    public String readLineSafe() {
        try {
            return this.readLine();
        }
        catch (IOException ie) {
            return null;
        }
    }

    /**
     * Write the provided String to an output file.
     * 
     * @param out Output string.
     * @throws IOException If no write can occur.
     */
    public void write(String out) throws IOException {
        if (!this.canWrite) {
            throw new IllegalStateException("Cannot write to a read-only file!");
        }

        for (int i = 0; i < out.length(); i++) {
            w.write(out.charAt(i));
        }
        w.flush();
    }

    /**
     * Close the input/output streams.
     */
    public void close() {
        try {
            if (this.canRead) { this.r.close(); }
            if (this.canWrite) { this.w.close(); }
        }
        catch (IOException ie) {/* fail silently */}
    }
}