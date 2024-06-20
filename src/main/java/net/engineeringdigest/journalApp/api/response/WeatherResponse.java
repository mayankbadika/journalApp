package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private Current current;

    @Getter
    @Setter
    public class Current{
        private int last_updated_epoch;
        private String last_updated;
        private double temp_c;
        private double temp_f;
        private int is_day;
    }
}