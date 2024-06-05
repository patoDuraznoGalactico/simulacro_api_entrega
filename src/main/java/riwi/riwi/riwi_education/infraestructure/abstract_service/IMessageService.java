package riwi.riwi.riwi_education.infraestructure.abstract_service;

import riwi.riwi.riwi_education.api.dto.request.MessagesRequest;
import riwi.riwi.riwi_education.api.dto.response.MessagesResponse;

public interface IMessageService extends CrudService<MessagesRequest,MessagesResponse, Integer> {
    public String FIELD_BY_SORT = "sentDate";
}