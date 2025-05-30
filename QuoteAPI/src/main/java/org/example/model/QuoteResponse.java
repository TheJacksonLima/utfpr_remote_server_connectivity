package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse {
    private List<Quote> quotes;
    private int total;
    private int skip;
    private int limit;
}