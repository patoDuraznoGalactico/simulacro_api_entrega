package riwi.riwi.riwi_education.api.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentsBasicResponse {
    private int assignmentId;
    private String title;   
    private String description;
    private LocalDate dueDate;
    private LessonsBasicResponse lesson;
}
