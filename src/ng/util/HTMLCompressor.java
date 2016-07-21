/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ng.util;

import com.yahoo.platform.yui.compressor.CssCompressor;
import java.io.*;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Naga Srinivas @Canvass
 */
public class HTMLCompressor extends CssCompressor {

    private StringBuffer srcsb;
    private Reader in;

    public HTMLCompressor(Reader in) throws IOException {
        super(in);
    }

    @Override
    public void compress(Writer out, int linebreakpos) throws IOException {
        super.compress(out, linebreakpos);
    }

    public static void main(String[] args) throws Exception {
        //String file = "D:/Projects/Canvass-E-Comm-test/web/src/main/webapp/res/templates/analytics/activities/workflow.html";
        String file = "D:\\Projects\\Canvass-E-Comm-test\\web\\src\\main\\webapp\\res\\templates\\_dashboard.html";
        HTMLCompressor comp = new HTMLCompressor(new FileReader(file));
        StringWriter w = new StringWriter();
        comp.compress(w, 8000);
        System.out.println(w.getBuffer().length());
        //Document doc = Jsoup.parse(new File(file), "utf-8");
        //Elements select = doc.body().select("script");
        //Iterator<Element> it = select.iterator();
        //while (it.hasNext()) {
        //    Element ele = it.next();
        //    System.out.println(ele.html("New Test"));
        //}

    }
}
