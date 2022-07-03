package com.jw.home.rest;

import com.jw.home.client.ApiServerClient;
import com.jw.home.domain.mapper.ScenarioMapper;
import com.jw.home.exception.CustomBusinessException;
import com.jw.home.rest.dto.AddScenarioReq;
import com.jw.home.rest.dto.AddScenarioRes;
import com.jw.home.rest.dto.ResponseDto;
import com.jw.home.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/scenario")
@RequiredArgsConstructor
public class ScenarioRestController {
    private final ApiServerClient apiServerClient;
    private final ScenarioService scenarioService;

    @PostMapping
    public ResponseEntity<ResponseDto<AddScenarioRes>> addScenario(@RequestBody @Valid AddScenarioReq param) {
        String userId = AuthInfoManager.getRequestMemId();
        String homeId = param.getHomeId();
        log.info("addScenario : {}, {}", userId, param);

        // Query to api server
        ResponseDto<Map<String, String>> responseDto = apiServerClient.checkHomeAuthority(userId, homeId, extractDeviceIds(param));
        String timezone = responseDto.getResultData().get("timezone");

        String scenarioId = scenarioService.addScenario(ScenarioMapper.INSTANCE.toScenario(param), timezone);

        log.info("Succeed to add scenario : {}", scenarioId);
        return new ResponseEntity<>(new ResponseDto<>(null, null, new AddScenarioRes(scenarioId)), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Object>> handleException(Exception exception) {
        if (exception instanceof CustomBusinessException) {
            Integer errorCode = ((CustomBusinessException) exception).getErrorCode();
            String errorMessage = ((CustomBusinessException) exception).getErrorMessage();
            Object resultData = ((CustomBusinessException) exception).getResultData();
            return new ResponseEntity<>(new ResponseDto<>(errorCode, errorMessage, resultData), HttpStatus.CONFLICT);
        }
        log.error("Server exception", exception);
        return new ResponseEntity<>(new ResponseDto<>(null, "server is not prepared.", null), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String extractDeviceIds(AddScenarioReq param) {
        List<String> deviceIds = new ArrayList<>();
        for (AddScenarioReq.AddConditionDto condition : param.getConditions()) {
            if (condition.getDeviceId() != null) {
                deviceIds.add(condition.getDeviceId());
            }
        }
        for (AddScenarioReq.AddActionDto action : param.getActions()) {
            if (action.getDeviceId() != null) {
                deviceIds.add(action.getDeviceId());
            }
        }
        return String.join(",", deviceIds);
    }
}
