package com.complaint.service;

import com.complaint.Dto.ComplainWriteDto;
import com.complaint.Dto.DetailDto;
import com.complaint.Dto.ImgDto;
import com.complaint.Dto.ListDto;
import com.complaint.Entity.Complain;
import com.complaint.Entity.ComplainFile;
import com.complaint.Entity.ComplainReply;
import com.complaint.Entity.User;
import com.complaint.repository.ComplainRepo;
import com.complaint.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ComplainService {
    @Autowired
    private ComplainRepo complainRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ComplainImageService complainImageService;

    @Autowired
    private ReplyService replyService;

    public void save(String name, ComplainWriteDto writeDto, List<MultipartFile> multipartFiles) throws Exception {
        // 작성자의 id를 가지고 오기위해 계정명으로 id컬럼값 가져오기
        User user = userRepo.findById(name);

        // Complain 클래스 객체 만들어서 데이터 넣어주기
        // 그래야 complain 테이블에 데이터 저장 가능!!
        Complain complain = new Complain();
        complain.setUserId(user.getId()); // 현재 로그인 회원의 id컬럼값
        complain.setTitle(writeDto.getTitle());
        complain.setContent(writeDto.getContent());
        complain.setCategory(writeDto.getCategory());
        // 민원테이블에 저장하기
        complainRepo.save(complain);

        // 민원 테이블에 저장하고 저장된 id컬럼값 가져오기
        Complain data = complainRepo.find(complain.getUserId());

        // 이미지나 파일은 민원 테이블의 id컬럼값이 필요하므로
        // 민원 테이블 저장한 이후에 한다.
        complainImageService.saveImg(multipartFiles, data.getId());

    }

    public List<ListDto> getList(String username) {
        // 로그인 계정명으로 계정 정보 가져오기 ( id 컬럼이 필요하다)
        // id 컬럼으로 민원글에서 조회해야 한다.
        // ( 민원 테이블의 작성자의 값이 계정 id로 저장되어 있다.)
        // 하지만 이번에는 쿼리문(SQL문)으로 전부 처리 해보겠다!!

        List<Complain> complain
                = complainRepo.findByUserName(username);
        // 민원글 전체 다 가져온 다음에 ListDto 객체로 변환 시켜준다.
        // ListDto 객체들을 ArrayList에 담아준다.
        List<ListDto> listDtos = new ArrayList<>();

        for (Complain row : complain) {
            ListDto listDto = new ListDto(row);
            listDtos.add(listDto);
        }
        return listDtos;
    }

    public DetailDto getDetail(long id) {
        // 상세 페이지의 id로 조회
        Complain complain = complainRepo.findById(id);
        // 이미지 테이블
        List<ComplainFile> complainFile
                = complainImageService.getImgs( complain.getId() );
        // 답변 테이블에서 조회
        ComplainReply complainReply
                = replyService.getReply( complain.getId() );

        DetailDto detailDto = new DetailDto(complain, complainReply);

        // 이미지 있다면 get 메서드로 추가해준다.
        List<ImgDto> imgDtos = new ArrayList<>();
        for(ComplainFile row : complainFile) {
            ImgDto imgDto = new ImgDto(row);
            imgDtos.add(imgDto);
        }
        detailDto.setImgDtos(imgDtos);

        return detailDto;
    }
}
