package com.application.data.payload.response;

import com.application.data.dto.CityGeoCodingDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoCodingResponse {

    private double generationtime_ms;
    private ArrayList<CityGeoCodingDTO> results;

    public GeoCodingResponse(ArrayList<CityGeoCodingDTO> results, double generationtime_ms) {
        this.results = results;
        this.generationtime_ms = generationtime_ms;
    }
}
