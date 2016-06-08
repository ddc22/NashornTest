import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dehan on 6/8/16.
 */
public class Main {

    static List<Comment> comments = new ArrayList<>();
    public Main(){
        comments.add(new Comment("Peter Parker", "This is a comment."));
        comments.add(new Comment("John Doe", "This is *another* comment."));
    }
    public String getName(){
        return "hevi.info";
    }

    public static void main(String[] args) {
        try {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = engineManager.getEngineByName("nashorn");
            ScriptEngine scriptEngine2 = engineManager.getEngineByName("nashorn");

//            String fileName = "src/main/resources/jsfile.js";
//            String functionName = "doIt";
//            scriptEngine.eval("load('" + fileName + "');");

            try {
                scriptEngine.eval(new FileReader("src/main/resources/libs/nashorn-polyfill.js"));
                scriptEngine.eval(new FileReader("src/main/resources/libs/react.js"));
                scriptEngine.eval(new FileReader("src/main/resources/libs/react-dom-server.js"));

                scriptEngine2.eval(new FileReader("src/main/resources/libs/babel.js"));
                Object output = scriptEngine2.eval("Babel.transformFile('src/main/resources/jsx/commentBox.js', { presets: ['react'] }).code");
                System.out.println(output);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Invocable inv = (Invocable) scriptEngine;
            String retValue = (String) inv.invokeFunction("renderServer",comments);
            System.out.println(retValue);

        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }


    public class Comment {
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
