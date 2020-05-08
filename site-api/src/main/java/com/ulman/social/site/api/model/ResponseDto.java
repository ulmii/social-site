package com.ulman.social.site.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@RequiredArgsConstructor
public class ResponseDto<T>
{
    @NonNull
    private T data;
    private Integer _total;
    private Links _links;

    @Data
    @Builder(setterPrefix = "with")
    @AllArgsConstructor
    public static class Links
    {
        private Link next;

        @Data
        @Builder(setterPrefix = "with")
        @AllArgsConstructor
        public static class Link
        {
            private String href;
        }
    }
}
