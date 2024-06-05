package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.LessonsRequest;
import riwi.riwi.riwi_education.api.dto.response.LessonsResponse;

public interface ILessonsService extends CrudService<LessonsRequest,LessonsResponse, Integer> {
    public String FIELD_BY_SORT = "lessonTitle";
}

