package ru.mts.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class CurrentRequestStatusId implements java.io.Serializable {
    private static final long serialVersionUID = -6907175441736297329L;
    @Column(name = "request_id", nullable = false)
    private Integer requestId;

    @Column(name = "request_status_id", nullable = false)
    private Integer requestStatusId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CurrentRequestStatusId entity = (CurrentRequestStatusId) o;
        return Objects.equals(this.requestId, entity.requestId) &&
                Objects.equals(this.requestStatusId, entity.requestStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, requestStatusId);
    }
}