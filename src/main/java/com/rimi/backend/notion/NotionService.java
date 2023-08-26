package com.rimi.backend.notion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.rimi.backend.domain.advice.domain.entity.QandA;
import com.rimi.backend.domain.advice.domain.repository.QandARepository;
import com.rimi.backend.global.gpt.service.CreateAssistantService;
import com.rimi.backend.global.gpt.service.GetSystemService;
import com.rimi.backend.global.request.CreateNotionRequest;

@Service
public class NotionService {
        @Autowired
        private QandARepository qandARepository;

        private JsonObject template;

        @Autowired()
        private GetSystemService systemService;
        @Autowired()
        private CreateAssistantService createAssistantService;
        // requires richTextTemplate to be input
        private String bulletListItemTemplate = "{\n" + //
                        "                                    \"object\": \"block\",\n" + //
                        "                                    \"type\": \"bulleted_list_item\",\n" + //
                        "                                    \"bulleted_list_item\": {\n" + //
                        "                                        \"rich_text\": [%s],\n" + //
                        "                                        \"color\": \"default\"\n" + //
                        "                                    }\n" + //
                        "                                }";

        // requires richTextTemplate to be input
        private String paragraphTemplate = "{\n" + //
                        "            \"object\": \"block\",\n" + //
                        "            \"type\": \"paragraph\",\n" + //
                        "            \"paragraph\": {\n" + //
                        "                \"rich_text\": [%s],\n" + //
                        "                \"color\": \"default\"\n" + //
                        "            }\n" + //
                        "        }";
        private String richTextTemplate = "{\n" + //
                        "                             \"type\": \"text\",\n" + //
                        "                             \"text\": {\n" + //
                        "                                 \"content\": \"%s\"\n" + //
                        "                             },\n" + //
                        "                             \"annotations\": {\n" + //
                        "                                 \"bold\": false,\n" + //
                        "                                 \"italic\": false,\n" + //
                        "                                 \"strikethrough\": false,\n" + //
                        "                                 \"underline\": false,\n" + //
                        "                                 \"code\": false,\n" + //
                        "                                 \"color\": \"default\"\n" + //
                        "                             },\n" + //
                        "                             \"plain_text\": \"%s\",\n" + //
                        "                             \"href\": null\n" + //
                        "                         }";

        private String calloutTemplate = "{\n" + //
                        "                                    \"object\": \"block\",\n" + //
                        "                                    \"type\": \"callout\",\n" + //
                        "                                    \"callout\": {\n" + //
                        "                                        \"rich_text\": [\n" + //
                        "                                            {\n" + //
                        "                                                \"type\": \"text\",\n" + //
                        "                                                \"text\": {\n" + //
                        "                                                    \"content\": \"%s\",\n" + //
                        "                                                    \"link\": {\n" + //
                        "                                                        \"url\": \"%s\"\n" + //
                        "                                                    }\n" + //
                        "                                                },\n" + //
                        "                                                \"annotations\": {\n" + //
                        "                                                    \"bold\": false,\n" + //
                        "                                                    \"italic\": false,\n" + //
                        "                                                    \"strikethrough\": false,\n" + //
                        "                                                    \"underline\": false,\n" + //
                        "                                                    \"code\": false,\n" + //
                        "                                                    \"color\": \"default\"\n" + //
                        "                                                },\n" + //
                        "                                                \"plain_text\": \"%s\",\n" + //
                        "                                                \"href\": \"%s\"\n" + //
                        "                                            }\n" + //
                        "                                        ],\n" + //
                        "                                        \"icon\": {\n" + //
                        "                                            \"type\": \"emoji\",\n" + //
                        "                                            \"emoji\": \"%s\"\n" + //
                        "                                        },\n" + //
                        "                                        \"color\": \"gray_background\"\n" + //
                        "                                    }\n" + //
                        "                                }";

        private String buildRichText(String content) {
                return String.format(richTextTemplate, content, content);
        }

        private String buildParagraph(String content) {
                return String.format(paragraphTemplate, String.format(richTextTemplate, content, content));
        }

        private String buildBulletListItem(String content) {
                return String.format(bulletListItemTemplate, String.format(richTextTemplate, content, content));
        }

        private String buildCallout(String type, String link, String emoji) {
                return String.format(calloutTemplate, type, link, type, link, emoji);
        }

        private String queryGPT(int num, String email) {
                String promptBase = systemService.getNotionQuestion(num);
                List<QandA> qandAList = qandARepository.findAllByEmail(email);
                String user = "";
                String assistant = "";

                for (QandA qandA : qandAList) {
                        user += "chatgpt " + ": " + qandA.getQuestion() +
                                        "\nOOO: " + qandA.getAnswer()
                                        + "\n";
                        // if (qandA.getAdvice() != null)
                        // assistant += "question " + qandA.getQandAid() + ": " + qandA.getQuestion()
                        // + "\nadvice to the user: "
                        // + qandA.getAdvice() + "\n";
                }

                String gptResponse = "";
                List<String> gptResponseBlocks = createAssistantService
                                .createAssistantWithAssistantResponse(promptBase, user, assistant).collectList()
                                .block();
                for (String block : gptResponseBlocks) {
                        System.out.println("block = " + block);
                        if (block == "[DONE]")
                                break;
                        try {
                                JsonObject firstChoice = JsonParser.parseString(block).getAsJsonObject().get("choices")
                                                .getAsJsonArray()
                                                .get(0).getAsJsonObject();
                                gptResponse += firstChoice
                                                .get("delta").getAsJsonObject().get("content").getAsString();
                        } catch (Exception e) {
                                continue;
                        }
                }
                return gptResponse;
        }

        public String buildPayload(CreateNotionRequest req) throws Exception {
                if (template == null) {
                        parseTemplate();
                }

                String name = req.getName();
                String introductoryText1 = "안녕하세요.";
                String introductoryText2 = String.format("%s\n%s입니다.", queryGPT(1, req.getEmail()), name);
                String selfDescriptionText1 = queryGPT(3, req.getEmail());
                String selfDescriptionText2 = "";
                String selfDescriptionText3 = "";

                String emailUrl = req.getEmail();
                String mySiteUrl = req.getPersonalSiteUrl();
                String instaUrl = req.getSnsUrl();

                String[] skills = queryGPT(5, req.getEmail()).split("\n");
                // { "Java", "Spring", "Python", "Django", "JavaScript", "React", "Vue",
                // "MySQL", "MongoDB", "AWS" };

                String values = queryGPT(7, req.getEmail());
                String experiences = queryGPT(6, req.getEmail());

                JsonObject copyTemplate = template.deepCopy();

                // 1. insert title text (user name)
                copyTemplate.get("properties").getAsJsonObject().get("title").getAsJsonObject().get("title")
                                .getAsJsonArray()
                                .get(0).getAsJsonObject().get("text").getAsJsonObject().addProperty("content", name);

                // retrieve actual page contents
                JsonArray templateSections = copyTemplate.get("children").getAsJsonArray();

                // 2. insert first header text (user name & greetings)
                {
                        JsonArray richIntroductoryText = templateSections.get(0).getAsJsonObject().get("heading_1")
                                        .getAsJsonObject()
                                        .get("rich_text").getAsJsonArray();

                        richIntroductoryText.get(0).getAsJsonObject().get("text").getAsJsonObject().addProperty(
                                        "content",
                                        introductoryText1);
                        richIntroductoryText.get(0).getAsJsonObject().addProperty("plain_text", introductoryText1);

                        richIntroductoryText.get(1).getAsJsonObject().get("text").getAsJsonObject().addProperty(
                                        "content",
                                        introductoryText2);
                        richIntroductoryText.get(1).getAsJsonObject().addProperty("plain_text", introductoryText2);
                }
                // 3. insert second & third header text (self description) - sample has fourth
                // as well
                {
                        JsonArray richSelfDescriptionSection = templateSections.get(2).getAsJsonObject()
                                        .get("column_list")
                                        .getAsJsonObject().get("children").getAsJsonArray();

                        // Optional: 0th element is the profile picture. skipped for now
                        JsonArray richSelfDescriptionTextArray = richSelfDescriptionSection.get(1).getAsJsonObject()
                                        .get("column")
                                        .getAsJsonObject().get("children").getAsJsonArray();

                        JsonObject richSelfDescriptionText1 = richSelfDescriptionTextArray.get(0).getAsJsonObject()
                                        .get("heading_3")
                                        .getAsJsonObject();
                        richSelfDescriptionText1.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                                        .getAsJsonObject().addProperty("content", selfDescriptionText1);
                        richSelfDescriptionText1.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                                        "plain_text",
                                        selfDescriptionText1);

                        JsonObject richSelfDescriptionText2 = richSelfDescriptionTextArray.get(1).getAsJsonObject()
                                        .get("heading_3")
                                        .getAsJsonObject();
                        richSelfDescriptionText2.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                                        .getAsJsonObject().addProperty("content", selfDescriptionText2);
                        richSelfDescriptionText2.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                                        "plain_text",
                                        selfDescriptionText2);

                        JsonObject richSelfDescriptionText3 = richSelfDescriptionTextArray.get(2).getAsJsonObject()
                                        .get("heading_3")
                                        .getAsJsonObject();
                        richSelfDescriptionText3.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().get("text")
                                        .getAsJsonObject().addProperty("content", selfDescriptionText3);
                        richSelfDescriptionText3.get("rich_text").getAsJsonArray().get(0).getAsJsonObject().addProperty(
                                        "plain_text",
                                        selfDescriptionText3);
                }

                // 4. insert socials & skills
                {
                        JsonArray columnList = templateSections.get(3).getAsJsonObject().get("column_list")
                                        .getAsJsonObject()
                                        .get("children").getAsJsonArray();
                        JsonObject socialColumns = columnList.get(0).getAsJsonObject().get("column").getAsJsonObject();

                        // socials
                        {
                                JsonArray socialArray = new JsonArray();
                                // 1. email
                                if (emailUrl != null && emailUrl.length() > 0) {
                                        JsonObject emailCallout = JsonParser
                                                        .parseString(buildCallout("Email", emailUrl, "✉️"))
                                                        .getAsJsonObject();
                                        socialArray.add(emailCallout);
                                }

                                // 2. my site
                                if (mySiteUrl != null && mySiteUrl.length() > 0) {
                                        JsonObject mySiteCallout = JsonParser
                                                        .parseString(buildCallout("My Site", mySiteUrl, "🌐"))
                                                        .getAsJsonObject();
                                        socialArray.add(mySiteCallout);
                                }

                                // 3. SNS
                                if (instaUrl != null && instaUrl.length() > 0) {
                                        JsonObject snsCallout = JsonParser
                                                        .parseString(buildCallout("SNS", instaUrl, "📷"))
                                                        .getAsJsonObject();
                                        socialArray.add(snsCallout);
                                }
                                socialColumns.add("children", socialArray);
                        }

                        // skills
                        {
                                // skills should be dynamically added to this column's children.
                                JsonObject skillColumn = columnList.get(1).getAsJsonObject().get("column")
                                                .getAsJsonObject();
                                JsonArray skillList = buildSkillList(skills);
                                skillColumn.add("children", skillList);
                        }
                }

                // 5. insert values
                {
                        JsonObject valueParagraph = templateSections.get(7).getAsJsonObject().get("paragraph")
                                        .getAsJsonObject();

                        String richValueText = buildRichText(values);
                        valueParagraph.add("rich_text",
                                        JsonParser.parseString("[" + richValueText + "]").getAsJsonArray());
                }

                // 6. insert experience
                {
                        // callout in 12th section - skip for now
                        JsonObject experienceParagraph = templateSections.get(13).getAsJsonObject().get("paragraph")
                                        .getAsJsonObject();

                        String richExperienceText = buildRichText(experiences);
                        experienceParagraph.add("rich_text",
                                        JsonParser.parseString("[" + richExperienceText + "]").getAsJsonArray());
                }

                // 7. insert prizes & certificates
                {
                        // since this is the last section, it can be appended to the templateSections
                        // array
                        try {
                                String prizes = qandARepository.findByEmailAndStepAndPercent(req.getEmail(), 5, 100)
                                                .get()
                                                .getAnswer();

                                for (String prize : prizes.split("\n")) {
                                        if (prize.isBlank())
                                                continue;
                                        templateSections.add(
                                                        JsonParser.parseString(buildBulletListItem(prize))
                                                                        .getAsJsonObject());
                                }
                        } catch (Exception e) {
                                System.out.println("prizes not found");
                        }
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
                                "                                                    \"content\": \"🧩Skills\"\n" + //
                                "                                                },\n" + //
                                "                                                \"annotations\": {\n" + //
                                "                                                    \"bold\": false,\n" + //
                                "                                                    \"italic\": false,\n" + //
                                "                                                    \"strikethrough\": false,\n" + //
                                "                                                    \"underline\": false,\n" + //
                                "                                                    \"code\": false,\n" + //
                                "                                                    \"color\": \"default\"\n" + //
                                "                                                },\n" + //
                                "                                                \"plain_text\": \"🧩Skills\",\n" + //
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

                JsonArray skillList = new JsonArray();
                skillList.add(skillHeading);
                skillList.add(skillDivider);
                for (String skill : skills) {
                        if (skill.isEmpty())
                                continue;
                        skillList.add(JsonParser.parseString(buildBulletListItem(skill)));
                }
                return skillList;
        }
}
