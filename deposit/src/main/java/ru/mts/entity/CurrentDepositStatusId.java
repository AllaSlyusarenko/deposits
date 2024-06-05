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
public class CurrentDepositStatusId implements java.io.Serializable {
    private static final long serialVersionUID = -6907175441736297382L;
    @Column(name = "deposit_id", nullable = false)
    private Integer depositId;

    @Column(name = "deposit_status_id", nullable = false)
    private Integer depositStatusId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CurrentDepositStatusId entity = (CurrentDepositStatusId) o;
        return Objects.equals(this.depositId, entity.depositId) &&
                Objects.equals(this.depositStatusId, entity.depositStatusId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositId, depositStatusId);
    }
}