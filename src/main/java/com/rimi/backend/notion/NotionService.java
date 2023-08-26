package com.rimi.backend.notion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

@Service
public class NotionService {
    private JsonObject template;
    
    // TODO: modify template to fill in appropriate fields.
    public String buildPayload() throws FileNotFoundException {
        if (template == null){
            parseTemplate();
        }
        JsonObject copyTemplate = template.deepCopy();
        return "";
    }

    public JsonObject parseTemplate() throws FileNotFoundException {
        Gson gson = new Gson();
        String filePath =  new File("").getAbsolutePath();
        filePath = filePath.concat("/src/main/java/com/rimi/backend/notion/template.json");
        JsonReader reader =  new JsonReader(new FileReader(filePath));
        template = gson.fromJson(reader, JsonObject.class);

        return template;
    }
}
