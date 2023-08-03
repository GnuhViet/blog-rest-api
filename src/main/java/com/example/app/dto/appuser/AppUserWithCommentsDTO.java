package com.example.app.dto.appuser;

import com.example.app.dto.comment.SimpleCommentDTO;
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
    private List<SimpleCommentDTO> comments;
}
