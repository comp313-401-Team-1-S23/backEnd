package com.meplus.meplusartifact.repos;

import com.meplus.meplusartifact.models.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


public interface DiaryRepo extends JpaRepository<Diary, Long> {


    public List<Diary> findByUserId(Long userId);


    @Transactional
    @Modifying(flushAutomatically = true)
    @Query("UPDATE Diary d SET d.title=:title, d.content=:content WHERE d.id=:id")
    public void updateDiary(@Param("id") Long id, @Param("title") String title, @Param("content") String content);

}
