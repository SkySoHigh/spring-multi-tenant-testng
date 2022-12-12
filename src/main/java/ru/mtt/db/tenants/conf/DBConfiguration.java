package ru.mtt.db.tenants.conf;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
public class DBConfiguration {
    @JsonProperty(required = true)
    String name;

    @JsonProperty(required = true)
    String url;

    @JsonProperty(required = true)
    String dataSourceClassName;

    @JsonProperty(required = true)
    String password;

    @JsonProperty(required = true)
    String user;


}
