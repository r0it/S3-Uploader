/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.util;

import app.aws.S3StaticFilesUploader.ContentType;
import com.yahoo.platform.yui.compressor.CssCompressor;
import com.yahoo.platform.yui.compressor.JavaScriptCompressor;
import java.io.*;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class Compressor implements ErrorReporter {

    public static class Options {

        int lineBreak = 8000;
        boolean munge = false;
        boolean verbose = false;
        boolean preserveSemicolons = false;
        boolean disableOptimizations = true;
    }
    private static final Options DEFAULT_OPTIONS = new Options();

    /**
     *
     * @param file
     * @param type
     * @return
     * @throws IOException
     */
    public Writer compress(File file, ContentType type) throws IOException {
        Reader in = new FileReader(file);
        Writer out = new StringWriter();
        switch (type) {
            case CSS:
            case HTML:
                CssCompressor css = new CssCompressor(in);
                css.compress(out, DEFAULT_OPTIONS.lineBreak);
                break;
            case JAVA_SCRIPT:
                JavaScriptCompressor js = new JavaScriptCompressor(in, this);
                js.compress(out, DEFAULT_OPTIONS.lineBreak, DEFAULT_OPTIONS.munge, DEFAULT_OPTIONS.verbose, DEFAULT_OPTIONS.preserveSemicolons, DEFAULT_OPTIONS.disableOptimizations);
                break;
        }
        return out;
    }

    @Override
    public void error(String string, String string1, int i, String string2, int i1) {
        System.out.println("Error");
    }

    @Override
    public EvaluatorException runtimeError(String string, String string1, int i, String string2, int i1) {
        System.out.println("Runtime Error");
        return new EvaluatorException(string);
    }

    @Override
    public void warning(String string, String string1, int i, String string2, int i1) {
        System.out.println("Warning");
    }

    public static void main(String[] args) throws IOException {
        String f = "D:/Projects/Canvass-E-Comm-test/web/src/main/webapp/res/templates/analytics/activities/workflow.html";
        Compressor cs = new Compressor();
        //File file = new File("D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res\\templates\\_dashboard.html");
        File file = new File(f);
        Writer out = cs.compress(file, ContentType.CSS);
        System.out.println(out);
    }
}
