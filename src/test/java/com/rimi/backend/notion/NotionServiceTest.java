package com.rimi.backend.notion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.rimi.backend.BackendApplication;
import com.rimi.backend.global.request.CreateNotionRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BackendApplication.class)
@AutoConfigureMockMvc
public class NotionServiceTest {
    @Autowired
    private NotionService notionService;

    @Test
    void testBuildPayload() {
        String result = assertDoesNotThrow(() -> {
            CreateNotionRequest req = CreateNotionRequest.create("", "", "", "");
            return notionService.buildPayload(req, "h$h$h$h$h$h");
        }, "There should be no errors building the payload.");
        Assert.notNull(result, result);
        Assert.isTrue(result.length() > 0, "The payload should not be empty.");
    }

    @Test
    void testParseTemplate() {
        JsonObject parsedTemplate = assertDoesNotThrow(() -> {
            return notionService.parseTemplate();
        }, "There should be no errors parsing the template.");
        Assert.notNull(parsedTemplate, "The parsed template should not be null.");
    }
}
