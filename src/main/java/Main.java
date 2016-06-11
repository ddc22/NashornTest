import de.cronn.jsxtransformer.CachedJsxTransformer;
import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehan on 6/8/16.
 */
public class Main {

    public Main(){

    }
    public String getName(){
        return "hevi.info";
    }
//https://facebook.github.io/react/docs/environments.html
    public static void main(String[] args) throws FileNotFoundException, IOException{
        try {
            List<Comment> comments = new ArrayList<>();
            comments.add(new Comment("Peter Parker", "This is a comment."));
            comments.add(new Comment("John Doe", "This is *another* comment."));

            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = engineManager.getEngineByName("nashorn");

            //JSX Transformations
            CachedJsxTransformer assetCache   = new CachedJsxTransformer(false, false);



            scriptEngine.eval(new FileReader("src/main/resources/libs/nashorn-polyfill.js"));
            scriptEngine.eval(new FileReader("src/main/resources/libs/react.js"));
            scriptEngine.eval(new FileReader("src/main/resources/libs/react-dom-server.js"));
            scriptEngine.eval(new FileReader("src/main/resources/libs/showdown.js"));


            final CachedJsxTransformer.AssetEntry assetEntry = assetCache.get("commentBox.js", (key) -> IOUtils
                    .toString(new FileInputStream("src/main/resources/jsx/commentBox.js"), StandardCharsets.UTF_8));
//            System.out.println(assetEntry.content);

            scriptEngine.eval(assetEntry.content);
            PrintWriter writer = new PrintWriter("src/main/resources/jsx/commentBoxNonJSX.js", "UTF-8");
            writer.println(assetEntry.content);

            writer.close();
            scriptEngine.eval(new FileReader("src/main/resources/jsx/commentBoxNonJSX.js"));

            Invocable inv = (Invocable) scriptEngine;
            String retValue = (String) inv.invokeFunction("renderServer",comments);
            System.out.println(retValue);

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public static class Comment {
        private String author;
        private String text;

        public Comment() {

        }

        public Comment(String author, String text) {
            this.author = author;
            this.text = text;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


}
