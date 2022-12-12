package ru.mtt.db.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "demo")
public class UsersEntity {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "info")
    @Builder.Default
    private String info = "defaultInfo";

    @Column(name = "deleted")
    @Builder.Default
    private boolean deleted = false;

}
