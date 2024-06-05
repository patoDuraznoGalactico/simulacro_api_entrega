package riwi.riwi.riwi_education.infraestructure.abstract_service;

import lombok.AllArgsConstructor;
import lombok.Data;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequest;
import riwi.riwi.riwi_education.api.dto.request.CoursesRequestUpdate;
import riwi.riwi.riwi_education.api.dto.response.CoursesResponse;

public interface ICoursesService extends CrudService<CoursesRequest,CoursesResponse, Integer> {
    public String FIELD_BY_SORT = "courseName";
    public CoursesResponse update(CoursesRequestUpdate request, Integer id);
}
