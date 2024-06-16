package ru.mts.mapper;

import lombok.experimental.UtilityClass;
import ru.mts.annotation.Logging;
import ru.mts.dto.RequestNotOkDto;
import ru.mts.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    /**
     * Метод - для преобразования Request в RequestNotOkDto
     */
    @Logging(entering = true, exiting = true)
    public RequestNotOkDto toRequestNotOkDto(Request request) {
        RequestNotOkDto requestNotOkDto = new RequestNotOkDto();
        requestNotOkDto.setId(request.getIdRequest());
        requestNotOkDto.setCreatedDateTime(request.getRequestDateTime());
        requestNotOkDto.setDepositDebitingAccountId(request.getDepositDebitingAccountId());
        requestNotOkDto.setDepositAmount(request.getDepositAmount());
        return requestNotOkDto;
    }

    /**
     * Метод - для преобразования списка Request в список RequestNotOkDto
     */
    @Logging(entering = true, exiting = true)
    public List<RequestNotOkDto> toRequestNotOkDtos(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestNotOkDto).collect(Collectors.toList());
    }
}