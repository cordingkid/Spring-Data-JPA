package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false) // 변경되지 못하도록 설정
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist // persist 하기 전에 이벤트가 발생하는거
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate // update 하기전에 호출
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }
}
