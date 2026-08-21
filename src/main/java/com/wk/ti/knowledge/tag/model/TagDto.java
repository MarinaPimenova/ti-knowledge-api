package com.wk.ti.knowledge.tag.model;

public record TagDto(
        Long id,
        String tag
){
    public static TagDto of(Tag tag) {
        return new TagDto(tag.getId(), tag.getTag());
    }
}
