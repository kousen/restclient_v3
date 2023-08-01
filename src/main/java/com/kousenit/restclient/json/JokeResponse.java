package com.kousenit.restclient.json;

import java.util.List;

// available categories: "animal", "career", "celebrity", "dev",
// "explicit", "fashion", "food", "history", "money", "movie",
// "music", "political", "religion", "science", "sport", "travel"

public record JokeResponse(String id,
                           String url,
                           String iconUrl,
                           String value,
                           List<String> categories) {
}
