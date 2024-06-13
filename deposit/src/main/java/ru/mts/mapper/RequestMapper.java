package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.dto.RequestNotOkDto;
import ru.mts.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public RequestNotOkDto toRequestNotOkDto(Request request) {
        RequestNotOkDto requestNotOkDto = new RequestNotOkDto();
        requestNotOkDto.setId(request.getIdRequest());
        requestNotOkDto.setCreatedDateTime(request.getRequestDateTime());
        requestNotOkDto.setDepositDebitingAccountId(request.getDepositDebitingAccountId());
        requestNotOkDto.setDepositAmount(request.getDepositAmount());
        return requestNotOkDto;
    }

    public List<RequestNotOkDto> toRequestNotOkDtos(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestNotOkDto).collect(Collectors.toList());
    }

}
