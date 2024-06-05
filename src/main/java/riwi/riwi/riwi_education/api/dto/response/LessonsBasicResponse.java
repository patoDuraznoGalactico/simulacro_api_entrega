package riwi.riwi.riwi_education.api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonsBasicResponse {
    private int lessonId;
    private String lessonTitle;
    private String content;
}
