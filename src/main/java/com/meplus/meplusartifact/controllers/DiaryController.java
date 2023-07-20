package com.meplus.meplusartifact.controllers;

import com.meplus.meplusartifact.models.Diary;
import com.meplus.meplusartifact.models.User;
import com.meplus.meplusartifact.repos.DiaryRepo;
import com.meplus.meplusartifact.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class DiaryController {

    @Autowired
    private final DiaryRepo diaryRepo;

    @Autowired
    private final UserRepo userRepo;

    public DiaryController(DiaryRepo diaryRepo, UserRepo userRepo) {
        this.diaryRepo = diaryRepo;
        this.userRepo = userRepo;
    }

    @PostMapping("/diary")
    public Map<String, String> createDiary(@RequestParam Long userId, @RequestBody Diary diary) {

        User user = userRepo.getById(userId);
        diary.setUser(user);
        Diary savedDiary = diaryRepo.save(diary);


        return new HashMap<String, String>() {{
            put("status", "201");
            put("diaryId", savedDiary.getId().toString());
            put("title", savedDiary.getTitle());
            put("createdOn", savedDiary.getCreatedOn().toString());
            put("content", savedDiary.getContent());
            put("userId", savedDiary.getUser().getId().toString());
        }};

    }

    // get a diary by id
    @GetMapping("/diary")
    public HashMap<String,String> getDiaryById(@RequestParam Long diaryId) {
        Diary founddiary = diaryRepo.getById(diaryId);

        return new HashMap<String,String>() {{
            put("id", founddiary.getId().toString());
            put("title", founddiary.getTitle());
            put("content", founddiary.getContent());
            put("createdOn", founddiary.getCreatedOn().toString());
            put("userId", founddiary.getUser().getId().toString());
        }};

    }


    // get all user's diaries
    @GetMapping("/diaries")
    public List<Map<String,String>> getUserDiaries(@RequestParam Long userId) {

        List<Map<String,String>> response = new ArrayList<>();

        diaryRepo.findByUserId(userId).forEach(diary -> {
            response.add(new HashMap<String,String>() {{
                put("id", diary.getId().toString());
                put("title", diary.getTitle());
                put("content", diary.getContent());
                put("createdOn", diary.getCreatedOn().toString());
                put("userId", diary.getUser().getId().toString());
            }});
        });

        return response;

    }

    // update a diary by id
    // delete a diary by id


    @GetMapping("/alldiaries")
    public List<Map<String,String>> getAllDiaries() {

        List<Map<String,String>> response = new ArrayList<>();

        diaryRepo.findAll().forEach(diary -> {
            response.add(new HashMap<String,String>() {{
                put("id", diary.getId().toString());
                put("title", diary.getTitle());
                put("content", diary.getContent());
                put("createdOn", diary.getCreatedOn().toString());
                put("userId", diary.getUser().getId().toString());
            }});
        });

        return response;

    }
}
