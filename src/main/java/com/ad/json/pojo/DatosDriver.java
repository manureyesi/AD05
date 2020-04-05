
package com.ad.json.pojo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dbConnection",
    "app"
})
public class DatosDriver implements Serializable
{

    @JsonProperty("dbConnection")
    private DbConnection dbConnection;
    @JsonProperty("app")
    private App app;
    private final static long serialVersionUID = -6683268082042759291L;

    @JsonProperty("dbConnection")
    public DbConnection getDbConnection() {
        return dbConnection;
    }

    @JsonProperty("dbConnection")
    public void setDbConnection(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @JsonProperty("app")
    public App getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dbConnection", dbConnection).append("app", app).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(app).append(dbConnection).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DatosDriver) == false) {
            return false;
        }
        DatosDriver rhs = ((DatosDriver) other);
        return new EqualsBuilder().append(app, rhs.app).append(dbConnection, rhs.dbConnection).isEquals();
    }

}
