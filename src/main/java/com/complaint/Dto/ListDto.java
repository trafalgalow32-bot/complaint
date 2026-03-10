package com.complaint.Dto;

import com.complaint.Entity.Complain;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListDto {
    private long id;
    private String title;
    private LocalDateTime createdAt;
    private String status;

    public ListDto( Complain complain) {
        this.id = complain.getId();
        this.title = complain.getTitle();
        this.createdAt = complain.getCreatedAt();
        this.status = complain.getStatus();
    }
}
