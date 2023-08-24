package com.example.app.dto.comment;

import com.example.app.dto.appuser.SimpleAppUserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String id;
    private String content;
    private Date createDate;
    private Date modifiedDate;
    private SimpleAppUserDTO userDTO;
}
