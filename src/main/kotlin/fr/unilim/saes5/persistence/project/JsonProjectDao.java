package fr.unilim.saes5.persistence.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fr.unilim.saes5.model.Glossary;
import fr.unilim.saes5.model.context.Context;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonProjectDao implements ProjectDao {

    private final File jsonFile;
    private final Gson gson;

    public JsonProjectDao(String filePath) {
        String userHome = System.getProperty("user.home");
        String directoryName = ".codelinguo";
        File directory = new File(userHome, directoryName);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        this.jsonFile = new File(directory, filePath);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Context.class, new ContextDeserializer());
        this.gson = gsonBuilder.create();

        fileExtraction();
    }

    private void fileExtraction() {
        if (!this.jsonFile.exists()) {
            try {
                boolean fileCreated = this.jsonFile.createNewFile();
                if (!fileCreated) {
                    throw new IOException("Unable to create file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveProject(Glossary project) {
        List<Glossary> singleProjectList = new ArrayList<>();
        singleProjectList.add(project);
        this.writeListToFile(singleProjectList);
    }

    @Override
    public List<Glossary> getAllProject() {
        try (Reader reader = new FileReader(this.jsonFile)) {
            Type listType = new TypeToken<ArrayList<Glossary>>() {
            }.getType();
            return this.gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void writeListToFile(List<Glossary> projects) {
        try (Writer writer = new FileWriter(this.jsonFile, false)) {
            this.gson.toJson(projects, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
