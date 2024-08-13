package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final Map<Long, Memo> memoMap = new HashMap<>();

    @PostMapping("/memos")
    public MemoResponseDto creatMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // Memo Max ID Check
        Long maxId = memoMap.size() > 0 ? Collections.max(memoMap.keySet()) + 1 : 1;
        memo.setId(maxId);

        // DB 저장
        memoMap.put(memo.getId(), memo);

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // Map to List
        List<MemoResponseDto> responseList = memoMap.values().stream()
                .map(MemoResponseDto::new).toList();

        return responseList;
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        if (memoMap.containsKey(id)) {
            // 해당 메모 가져오기
            Memo memo = memoMap.get(id);

            // 메모 수정
            memo.update(requestDto);

            return memo.getId();
        } else {
            throw new IllegalArgumentException("선택한 메모가 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        if (memoMap.containsKey(id)) {
            // 해당 메모를 삭제
            memoMap.remove(id);
            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모가 존재하지 않습니다.");
        }
    }
}
