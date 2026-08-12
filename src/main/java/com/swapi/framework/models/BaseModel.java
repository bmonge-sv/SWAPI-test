package com.swapi.framework.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseModel {

    private String created;
    private String edited;
    private String url;

    public String getCreated() { return created; }
    public String getEdited()  { return edited; }
    public String getUrl()     { return url; }

    public void setCreated(String created) { this.created = created; }
    public void setEdited(String edited)   { this.edited = edited; }
    public void setUrl(String url)         { this.url = url; }
}
