package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Envelope returned by every collection endpoint:
 * {@code { count, next, previous, results }}.
 *
 * @param <T> the resource type contained in {@link #results}
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResponse<T> {

    /** Total number of resources in the collection. */
    private int count;

    /** URL of the next page, or {@code null} on the last page. */
    private String next;

    /** URL of the previous page, or {@code null} on the first page. */
    private String previous;

    /** Resources on the current page. */
    private List<T> results;
}
