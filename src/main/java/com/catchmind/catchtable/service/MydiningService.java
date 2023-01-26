package com.catchmind.catchtable.service;

import com.catchmind.catchtable.domain.Review;
import com.catchmind.catchtable.dto.ReserveDto;
import com.catchmind.catchtable.repository.ReserveRepository;
import com.catchmind.catchtable.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j  // 로그를 찍기 위해서 사용하는 어노테이션
@Transactional
@RequiredArgsConstructor
@Service
public class MydiningService {
    private final ReserveRepository reserveRepository;
    private final ReviewRepository reviewRepository;

//    @Transactional
//    public ReserveDto getList() {
//        return reserveRepository.findAll()
//                .stream().map(ReserveDto::from);
//    }

    @Transactional
    public ReserveDto getDetail(Long resIdx) {
        return reserveRepository.findById(resIdx).map(ReserveDto::from)
                .orElseThrow();
    }

    public Long saveFile(MultipartFile files, Review reviews) throws IOException {
        if (files.isEmpty()){
            return null;
        }

        String origName = files.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        String extension = origName.substring(origName.lastIndexOf("."));
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        String savedPath = "D:\\test/" + savedName;

        Review file = Review.builder()
//                .profile(reviews.getProfile())
                .revLike(reviews.getRevLike())
                .revContent(reviews.getRevContent())
                .revScore(reviews.getRevScore())
//                .resAdmin(reviews.getResAdmin())
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .build();

        files.transferTo(new File(savedPath));

        Review savedReview = reviewRepository.save(file);
        return savedReview.getId();
    }
}
