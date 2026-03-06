package com.complaint.control;

import com.complaint.Dto.ComplainWriteDto;
import com.complaint.Entity.Complain;
import com.complaint.service.ComplainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
public class ComplainController {
   @Autowired
    private ComplainService complainService;

   // 민원작성 데이터를 처리 - 이미지도 같이 처리
    @PostMapping("/writeSave")
    public String writeSave(@Valid ComplainWriteDto complainWriteDto,
                            BindingResult bindingResult,
           @RequestParam("imgFile")List<MultipartFile> multipartFiles,
                            Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            return "board/write";
        }
        // 잘 입력했다면 테이블에 저장 되도록!!
        try {
            complainService.save( principal.getName(), complainWriteDto, multipartFiles );
        } catch( Exception e ) {
            model.addAttribute("message", "이미지 또는 파일 업로드 실패");
            return "board/write";
        }
        return "redirect:/"; // 민원글 저장 완료 되었다면
    }

   // 민원 작성 페이지 요청 - 민원신청 클릭하면
   @GetMapping("/write")
    public String writeComplain( Model model) {
       model.addAttribute("complainWriteDto", new ComplainWriteDto());
       return "board/write";
   }
}
