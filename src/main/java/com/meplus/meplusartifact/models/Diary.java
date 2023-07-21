package com.meplus.meplusartifact.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;


@Entity
@Table(name = "diaries")
@Getter
@Setter
@ToString
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdOn;

    private String content;

    @ManyToOne()
    @JoinColumn(name="user_id")
    private User user;


    public Diary() {}


    public Diary(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
