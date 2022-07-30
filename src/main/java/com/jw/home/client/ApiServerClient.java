package com.jw.home.client;

import com.jw.home.client.exception.ApiServerClientErrorDecoder;
import com.jw.home.rest.dto.ResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "api-server", configuration = ApiServerClientErrorDecoder.class)
public interface ApiServerClient {
    @GetMapping("/api/v1/admin/check/home")
    ResponseDto<Map<String, String>> checkHomeAuthority(@RequestParam(name = "userId") String userId,
                                                        @RequestParam(name = "homeId") String homeId,
                                                        @RequestParam(name = "deviceIds", required = false) String deviceIds);

}
