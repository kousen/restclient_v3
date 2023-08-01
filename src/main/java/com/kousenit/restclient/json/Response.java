package com.kousenit.restclient.json;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

public record Response(List<Result> results, String status) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Result(String formattedAddress, Geometry geometry) {
        public record Geometry(Location location) {
            public record Location(double lat, double lng) {
            }
        }
    }
}
