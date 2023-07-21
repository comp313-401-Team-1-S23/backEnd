package com.meplus.meplusartifact.controllers;

import com.meplus.meplusartifact.models.Diary;
import com.meplus.meplusartifact.models.User;
import com.meplus.meplusartifact.repos.DiaryRepo;
import com.meplus.meplusartifact.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@Transactional
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

        return createResponse(savedDiary, true);
    }


    @GetMapping("/diary")
    public Map<String,String> getDiaryById(@RequestParam Long diaryId) {
        Diary founddiary = diaryRepo.getById(diaryId);

        return createResponse(founddiary, false);
    }


    @GetMapping("/diaries")
    public List<Map<String,String>> getUserDiaries(@RequestParam Long userId) {

        List<Map<String,String>> response = new ArrayList<>();

        diaryRepo.findByUserId(userId).forEach(diary -> {
            response.add(createResponse(diary, false));
        });
        return response;
    }


    @PostMapping("/diary/update")
    public Map<String, String> updateDiary(@RequestParam Long id, @RequestBody Map<String, Object> payload) {

        diaryRepo.updateDiary(id, payload.get("title").toString(), payload.get("content").toString());
        Diary updatedDiary = diaryRepo.getById(id);

        return createResponse(updatedDiary, true);
    }


    @DeleteMapping("diary")
    public Map<String, String> deleteDiary(@RequestParam Long id) {
        diaryRepo.deleteById(id);

        return new HashMap<String, String>(){{
            put("status", "200");
            put("message", String.format("Deleted diary ID: %s", id));

        }};
    }


    @GetMapping("/alldiaries")
    public List<Map<String,String>> getAllDiaries() {

        List<Map<String,String>> response = new ArrayList<>();

        diaryRepo.findAll().forEach(diary -> {
            response.add(createResponse(diary, false));
        });

        return response;
    }


    private Map<String, String> createResponse(Diary diary, Boolean status) {

        Map<String, String> response = new HashMap<String, String>() {{
            put("diaryId", diary.getId().toString());
            put("title", diary.getTitle());
            put("createdOn", diary.getCreatedOn().toString());
            put("content", diary.getContent());
            put("userId", diary.getUser().getId().toString());
        }};

        if (status == true) {
            response.put("status", "201");
        }

        return response;
    }
}
