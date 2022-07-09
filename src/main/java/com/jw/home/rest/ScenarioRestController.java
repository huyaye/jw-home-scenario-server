package com.jw.home.rest;

import com.jw.home.client.ApiServerClient;
import com.jw.home.domain.Scenario;
import com.jw.home.domain.mapper.ScenarioMapper;
import com.jw.home.exception.CustomBusinessException;
import com.jw.home.rest.dto.*;
import com.jw.home.service.ScenarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @DeleteMapping
    public ResponseEntity<ResponseDto<DeleteScenarioRes>> deleteScenario(@Valid @RequestBody DeleteScenarioReq param) {
        String userId = AuthInfoManager.getRequestMemId();
        log.info("deleteScenario : {}, {}", userId, param.getScenarioIds());

        List<String> targetScenarioIds = new ArrayList<>();
        // scenario
        Iterable<Scenario> scenarios = scenarioService.getScenarios(param.getScenarioIds());
        for (Scenario scenario : scenarios) {
            try {
                apiServerClient.checkHomeAuthority(userId, scenario.getHomeId(), null);
                targetScenarioIds.add(scenario.getId());
            } catch (Exception e) {
                log.warn("Including not authorized home or device : {}", e.toString());
            }
        }
        scenarioService.deleteScenarios(targetScenarioIds);

        log.info("Succeed to delete scenario : {}", targetScenarioIds);
        return new ResponseEntity<>(new ResponseDto<>(null, null, new DeleteScenarioRes(targetScenarioIds)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<GetScenarioRes>> getScenario(@RequestParam("homeId") String homeId) {
        String userId = AuthInfoManager.getRequestMemId();
        log.info("getScenario : {}, {}", userId, homeId);

        apiServerClient.checkHomeAuthority(userId, homeId, null);

        GetScenarioRes response = new GetScenarioRes();
        Iterable<Scenario> scenarios = scenarioService.getScenariosOfHome(homeId);
        scenarios.forEach(scenario -> response.add(ScenarioMapper.INSTANCE.toGetScenarioRes(scenario)));

        log.info("Succeed to get scenario");
        return new ResponseEntity<>(new ResponseDto<>(null, null, response), HttpStatus.OK);
    }

    @ExceptionHandler
    public ResponseEntity<ResponseDto<Object>> handleException(Exception exception) {
        if (exception instanceof CustomBusinessException) {
            Integer errorCode = ((CustomBusinessException) exception).getErrorCode();
            String errorMessage = ((CustomBusinessException) exception).getErrorMessage();
            Object resultData = ((CustomBusinessException) exception).getResultData();
            return new ResponseEntity<>(new ResponseDto<>(errorCode, errorMessage, resultData), HttpStatus.CONFLICT);
        } else if (exception instanceof MethodArgumentNotValidException) {
            List<String> errors = ((MethodArgumentNotValidException)exception).getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new ResponseDto<>(null, null, errors), HttpStatus.BAD_REQUEST);
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
