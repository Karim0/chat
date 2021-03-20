package com.server_chat.chat.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public final static Role USER = new Role(1L, "USER");

    @Column
    private String name;

    public Role(String name) {
        this.name = name;
    }
}
