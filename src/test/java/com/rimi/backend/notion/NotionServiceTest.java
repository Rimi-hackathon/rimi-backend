package com.rimi.backend.notion;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.google.gson.JsonObject;
import com.rimi.backend.BackendApplication;

@SpringBootTest(
  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
  classes = BackendApplication.class)
@AutoConfigureMockMvc
public class NotionServiceTest {
    @Autowired
    private NotionService notionService;

    @Test
    void testBuildPayload() {

    }

    @Test
    void testParseTemplate() {
        JsonObject parsedTemplate = assertDoesNotThrow(() -> { 
            return notionService.parseTemplate();
        }, "There should be no errors parsing the template.");
        Assert.notNull(parsedTemplate, "The parsed template should not be null.");
    }
}
