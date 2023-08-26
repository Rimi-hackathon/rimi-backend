package com.rimi.backend.notion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

@Service
public class NotionService {
    private JsonObject template;

    // TODO: modify template to fill in appropriate fields.
    public String buildPayload() throws FileNotFoundException {
        if (template == null) {
            parseTemplate();
        }
        String name = "test";
        String introductoryText1 = "안녕하세요.";
        String introductoryText2 = String.format("%s입니다.", name);
        String selfDescriptionText1 = "t1";
        String selfDescriptionText2 = "t2";
        String selfDescriptionText3 = "t3";

        String emailUrl = "mailto:";
        String mySiteUrl = "http://arai.kr/";
        String instaUrl = "https://www.instagram.com/";
        String blogUrl = "https://blog.naver.com/";

        String[] skills = { "Java", "Spring", "Python", "Django", "JavaScript", "React", "Vue", "MySQL", "MongoDB",
                "AWS" };

        String values = "values";
        String experiences = "experiences";

        JsonObject copyTemplate = template.deepCopy();

        // 1. insert title text (user name)
        copyTemplate.get("properties").getAsJsonObject().get("title").getAsJsonObject().get("title").getAsJsonArray()
                .get(0).getAsJsonObject().get("text").getAsJsonObject().addProperty("content", name);

        // retrieve actual page contents
        JsonArray templateSections = copyTemplate.get("children").getAsJsonArray();

        // 2. insert first header text (user name & greetings)
        {
            JsonArray richIntroductoryText = templateSections.get(0).getAsJsonObject().get("heading_1")
                    .getAsJsonObject()
                    .get("rich_text").getAsJsonArray();

            richIntroductoryText.get(0).getAsJsonObject().get("text").getAsJsonObject().addProperty("content",
                    introductoryText1);
            richIntroductoryText.get(0).getAsJsonObject().addProperty("plain_text", introductoryText1);

            richIntroductoryText.get(1).getAsJsonObject().get("text").getAsJsonObject().addProperty("content",
                    introductoryText2);
            richIntroductoryText.get(1).getAsJsonObject().addProperty("plain_text", introductoryText2);
        }
        // 3. insert second & third header text (self description) - sample has fourth
        // as well
        {
            JsonArray richSelfDescriptionSection = templateSections.get(2).getAsJsonObject().get("column_list")
                    .getAsJsonObject().get("children").getAsJsonArray();

            // Optional: 0th element is the profile picture. skipped for now
            JsonArray richSelfDescriptionTextArray = richSelfDescriptionSection.get(1).getAsJsonObject().get("column")
                    .getAsJsonObject().get("children").getAsJsonArray();

            JsonObject richSelfDescriptionText1 = richSelfDescriptionTextArray.get(0).getAsJsonObject().get("heading_3")
                    .getAsJsonObject();
            richSelfDescriptionText1.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                    .getAsJsonObject().addProperty("content", selfDescriptionText1);
            richSelfDescriptionText1.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                    "plain_text",
                    selfDescriptionText1);

            JsonObject richSelfDescriptionText2 = richSelfDescriptionTextArray.get(1).getAsJsonObject().get("heading_3")
                    .getAsJsonObject();
            richSelfDescriptionText2.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                    .getAsJsonObject().addProperty("content", selfDescriptionText2);
            richSelfDescriptionText2.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                    "plain_text",
                    selfDescriptionText2);

            JsonObject richSelfDescriptionText3 = richSelfDescriptionTextArray.get(2).getAsJsonObject().get("heading_3")
                    .getAsJsonObject();
            richSelfDescriptionText3.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                    .getAsJsonObject().addProperty("content", selfDescriptionText3);
            richSelfDescriptionText3.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                    "plain_text",
                    selfDescriptionText3);
        }

        // 4. insert socials & skills
        {
            JsonArray columnList = templateSections.get(3).getAsJsonObject().get("column_list").getAsJsonObject()
                    .get("children").getAsJsonArray();
            JsonArray socialColumns = columnList.get(0).getAsJsonObject().get("column").getAsJsonObject()
                    .get("children").getAsJsonArray();

            // socials
            {
                // 1. email
                JsonObject emailColumn = socialColumns.get(2).getAsJsonObject().get("callout").getAsJsonObject()
                        .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
                emailColumn.get("text").getAsJsonObject().get("link").getAsJsonObject().addProperty("url",
                        emailUrl);
                emailColumn.addProperty("plain_text", emailUrl);

                // 2. my site
                JsonObject mySiteColumn = socialColumns.get(3).getAsJsonObject().get("callout").getAsJsonObject()
                        .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
                mySiteColumn.get("text").getAsJsonObject().get("link").getAsJsonObject().addProperty("url",
                        mySiteUrl);
                mySiteColumn.addProperty("plain_text", mySiteUrl);

                // 3. Instagram
                JsonObject instaColumn = socialColumns.get(4).getAsJsonObject().get("callout").getAsJsonObject()
                        .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
                instaColumn.get("text").getAsJsonObject().get("link").getAsJsonObject().addProperty("url",
                        instaUrl);
                instaColumn.addProperty("plain_text", instaUrl);

                // 4. Blog
                JsonObject blogColumn = socialColumns.get(5).getAsJsonObject().get("callout").getAsJsonObject()
                        .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
                blogColumn.get("text").getAsJsonObject().get("link").getAsJsonObject().addProperty("url",
                        blogUrl);
                blogColumn.addProperty("plain_text", blogUrl);
            }

            // skills
            {
                // skills should be dynamically added to this column's children.
                JsonObject skillColumn = columnList.get(1).getAsJsonObject().get("column").getAsJsonObject();
                JsonArray skillList = buildSkillList(skills);
                skillColumn.add("children", skillList);
            }
        }

        // 5. insert values
        {
            JsonObject valueHeading = templateSections.get(5).getAsJsonObject().get("heading_2").getAsJsonObject()
                    .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
            valueHeading.get("text").getAsJsonObject().addProperty("content", values);
            valueHeading.addProperty("plain_text", values);
        }

        // 6. insert experience
        {
            JsonObject experienceHeading = templateSections.get(10).getAsJsonObject().get("heading_2").getAsJsonObject()
                    .get("rich_text").getAsJsonArray().get(0).getAsJsonObject();
            experienceHeading.get("text").getAsJsonObject().addProperty("content", experiences);
            experienceHeading.addProperty("plain_text", experiences);
        }

        // 7. insert prizes & certificates
        {
            // templateSections.get(14).
        }
        return copyTemplate.toString();
    }

    public JsonObject parseTemplate() throws FileNotFoundException {
        Gson gson = new Gson();
        String filePath = new File("").getAbsolutePath();
        filePath = filePath.concat("/src/main/java/com/rimi/backend/notion/template.json");
        JsonReader reader = new JsonReader(new FileReader(filePath));
        template = gson.fromJson(reader, JsonObject.class);

        return template;
    }

    public JsonArray buildSkillList(String[] skills) {
        JsonObject skillHeading = JsonParser.parseString("{\n" + //
                "                                    \"object\": \"block\",\n" + //
                "                                    \"type\": \"heading_2\",\n" + //
                "                                    \"heading_2\": {\n" + //
                "                                        \"rich_text\": [\n" + //
                "                                            {\n" + //
                "                                                \"type\": \"text\",\n" + //
                "                                                \"text\": {\n" + //
                "                                                    \"content\": \"\u1F9E9Skills\"\n" + //
                "                                                },\n" + //
                "                                                \"annotations\": {\n" + //
                "                                                    \"bold\": false,\n" + //
                "                                                    \"italic\": false,\n" + //
                "                                                    \"strikethrough\": false,\n" + //
                "                                                    \"underline\": false,\n" + //
                "                                                    \"code\": false,\n" + //
                "                                                    \"color\": \"default\"\n" + //
                "                                                },\n" + //
                "                                                \"plain_text\": \"\u1F9E9Skills\",\n" + //
                "                                                \"href\": null\n" + //
                "                                            }\n" + //
                "                                        ],\n" + //
                "                                        \"is_toggleable\": false,\n" + //
                "                                        \"color\": \"default\"\n" + //
                "                                    }\n" + //
                "                                }").getAsJsonObject();
        JsonObject skillDivider = JsonParser.parseString("{\n" + //
                "                                    \"object\": \"block\",\n" + //
                "                                    \"type\": \"divider\",\n" + //
                "                                    \"divider\": {}\n" + //
                "                                }").getAsJsonObject();

        String bulletListItemTemplate = "{\"object\": \"block\",\"type\": \"bulleted_list_item\",\"bulleted_list_item\": {\"rich_text\": [{\"type\": \"text\",\"text\": {\"content\": \"%s\"},\"annotations\": {\"bold\": false,\"italic\": false,\"strikethrough\": false,\"underline\": false,\"code\": false,\"color\": \"default\"},\"plain_text\": \"%s\",\"href\": null}],\"color\": \"default\"}}";

        JsonArray skillList = new JsonArray();
        skillList.add(skillHeading);
        skillList.add(skillDivider);
        for (String skill : skills) {
            skillList.add(JsonParser.parseString(String.format(bulletListItemTemplate, skill, skill)));
        }
        return skillList;
    }
}
