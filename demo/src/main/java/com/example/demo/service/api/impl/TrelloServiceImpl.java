package com.example.demo.service.api.impl;

import com.example.demo.model.Feedback;
import com.example.demo.service.api.interfaces.TrelloService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TrelloServiceImpl implements TrelloService {
    @Value("${trello.key}")
    private String trelloKey;

    @Value("${trello.token}")
    private String trelloToken;

    @Value("${trello.list.id}")
    private String listId;

    public String createCardForFeedback(Feedback f) {
        String name = "Critical feedback - " + (f.getUserProfile() != null && f.getUserProfile().getBranch() != null ? f.getUserProfile().getBranch().getName() : "unknown");
        String desc = "Text: " + f.getText() + "\nSuggested: " + f.getSuggestedSolution();
        String url = UriComponentsBuilder.fromHttpUrl("https://api.trello.com/1/cards")
                .queryParam("key", trelloKey)
                .queryParam("token", trelloToken)
                .queryParam("idList", listId)
                .queryParam("name", name)
                .queryParam("desc", desc)
                .build().toUriString();

        RestTemplate rt = new RestTemplate();
        var resp = rt.postForObject(url, null, java.util.Map.class);
        if (resp != null && resp.containsKey("shortUrl")) {
            return resp.get("shortUrl").toString();
        }
        return null;
    }
}
