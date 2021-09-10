/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adventure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Classe contenente metodo che legge da file e restituisce
 * il contenuto in un insieme di stringhe
 * usato per caricare le stopwords
 * @author psp
 */
public class Utils {

    public static Set<String> loadFileListInSet(File file) throws IOException {
        Set<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                set.add(reader.readLine().trim().toLowerCase());
            }
        }
        return set;
    }

  
}
