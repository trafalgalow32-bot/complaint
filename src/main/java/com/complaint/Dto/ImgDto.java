package com.complaint.Dto;

import com.complaint.Entity.ComplainFile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImgDto {
    private String url;
    private String imgName;

    public ImgDto(ComplainFile complainFile) {
        this.url=complainFile.getFilePath();
        this.imgName=complainFile.getFileName();
    }
}
