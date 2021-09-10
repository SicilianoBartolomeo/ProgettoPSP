package adventure;


import parser.Parser;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Control
 * Classe che si occupa di creare un parser e un
 * istanza di GameDescription per inizializzare il gioco
 *
 * @author psp
 */
public class Engine {

    private GameDescription game;

    private Parser parser;
    
    private final String STOPWORDS_FILE_PATH = "./resources/file/stopwords";

    public Engine(GameDescription game) {
        this.game = game;
        try {
            this.game.init();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        try {
            Set<String> stopwords = Utils.loadFileListInSet(new File(STOPWORDS_FILE_PATH));
            parser = new Parser(stopwords);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    public GameDescription getGame() {
        return game;
    }

    public void setGame(GameDescription game) {
        this.game = game;
    }

    public Parser getParser() {
        return parser;
    }

}
