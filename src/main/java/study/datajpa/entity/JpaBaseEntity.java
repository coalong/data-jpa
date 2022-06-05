package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


@MappedSuperclass
@Getter
public class JpaBaseEntity {

    // updatable = false 는
    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    // PrePersist (persist 하기 전에 실행)
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now;
    }

    // PreUpdate update 하기 전에 실행
    @PreUpdate
    public void preUpdate() {
        updateDate = LocalDateTime.now();
    }
}
