package fr.unilim.saes5.persistence.keyword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class TxtKeywordDao implements KeywordDao {

    private final String keywordsFilePath;

    // Constructeur prenant le chemin de fichier en paramètre
    public TxtKeywordDao(String keywordsFilePath) {
        this.keywordsFilePath = keywordsFilePath;
    }

    @Override
    public Set<String> loadKeywords() {
        try (InputStream is = TxtKeywordDao.class.getResourceAsStream(keywordsFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            return reader.lines().collect(Collectors.toSet());
        } catch (IOException | NullPointerException e) {
            return Collections.emptySet();
        }
    }
}
