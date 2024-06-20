package com.kousenit.restclient.json;

import java.util.List;

public record JokeResponse(
        String id,
        String url,
        String iconUrl,
        String value,
        List<String> categories) {

    // Extract the validation list into a constant
    private static final List<String> VALID_CATEGORIES =
            List.of("animal", "career", "celebrity", "dev",
                    "explicit", "fashion", "food", "history", "money",
                    "movie", "music", "political", "religion", "science",
                    "sport", "travel");

    public JokeResponse {
        // check that the specified categories in the list are valid
        if (categories != null) {
            // Use the extracted constant for validation
            categories.stream()
                    .filter(category -> !VALID_CATEGORIES.contains(category))
                    .forEach(category -> {
                        throw new IllegalArgumentException("Invalid category: " + category);
                    });
        }
    }
}