package com.example.demo.service.api;

import com.example.demo.model.Feedback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoogleSheetsService {
    @Value("${google.spreadsheet.id}")
    private String spreadsheetId;

    @Value("${google.credentials.file:google-service-account.json}")
    private String credentialsPath;

    private Sheets getSheetsService() throws Exception {
        InputStream in = new ClassPathResource(credentialsPath).getInputStream();
        com.google.api.client.googleapis.auth.oauth2.GoogleCredential credential =
                com.google.api.client.googleapis.auth.oauth2.GoogleCredential.fromStream(in)
                        .createScoped(List.of("https://www.googleapis.com/auth/spreadsheets"));
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName("feedback-bot").build();
    }

    public String appendFeedbackRow(Feedback f) throws Exception {
        Sheets sheets = getSheetsService();
        List<Object> row = Arrays.asList(
                f.getCreatedAt().toString(),
                f.getUserProfile().getBranch() != null ? f.getUserProfile().getBranch().getName() : "unknown",
                f.getUserProfile().getRole(),
                f.getSentiment() != null ? f.getSentiment().name() : "NEUTRAL",
                f.getCriticality(),
                f.getText(),
                f.getSuggestedSolution()
        );
        ValueRange body = new ValueRange().setValues(List.of(row));
        AppendValuesResponse resp = sheets.spreadsheets().values()
                .append(spreadsheetId, "Аркуш1!A1", body)
                .setValueInputOption("RAW")
                .execute();

        return "https://docs.google.com/spreadsheets/d/" + spreadsheetId;
    }
}
