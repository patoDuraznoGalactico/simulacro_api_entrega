package riwi.riwi.riwi_education.api.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import riwi.riwi.riwi_education.domain.entities.Users;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoursesBasicResponse {
    private int courseId;
    private String course_name;
    private String description;
    private UsersBasicResponse userInstructor;
}