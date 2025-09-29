package com.example.demo.service.api.interfaces;

import com.example.demo.model.Feedback;
import com.google.api.services.sheets.v4.Sheets;

public interface GoogleSheetsService {
    String appendFeedbackRow(Feedback f) throws Exception;
}
