package com.example.app.dtos.appuser;

import com.example.app.dtos.comment.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserWithCommentsDTO {
    private String username;
    private List<CommentDTO> comments;
}
