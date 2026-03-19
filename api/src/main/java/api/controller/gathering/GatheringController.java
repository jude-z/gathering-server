package api.controller.gathering;

import api.common.resolver.annotation.Username;
import api.response.ApiResponse;
import api.service.gathering.GatheringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static api.requeset.gathering.GatheringRequestDto.*;

@RestController
@RequiredArgsConstructor
public class GatheringController {

    private final GatheringService gatheringService;

    @PostMapping("/gathering")
    public ResponseEntity<ApiResponse> addGathering(@RequestPart AddGatheringRequest addGatheringRequest,
                                                    @RequestPart MultipartFile file,
                                                    @Username Long userId) throws IOException {
        ApiResponse apiResponse = gatheringService.addGathering(addGatheringRequest, file, userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/gathering/{gatheringId}")
    public ResponseEntity<ApiResponse> updateGathering(@RequestPart UpdateGatheringRequest updateGatheringRequest,
                                                       @PathVariable Long gatheringId,
                                                       @RequestPart(required = false) MultipartFile file,
                                                       @Username Long userId) throws IOException {
        ApiResponse apiResponse = gatheringService.updateGathering(updateGatheringRequest, file, userId, gatheringId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/{gatheringId}")
    public ResponseEntity<ApiResponse> gatheringDetail(@PathVariable Long gatheringId){
        ApiResponse apiResponse = gatheringService.gatheringDetail(gatheringId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering/participated/{gatheringId}")
    public ResponseEntity<ApiResponse> participated(@PathVariable Long gatheringId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        ApiResponse apiResponse = gatheringService.participated(gatheringId, pageNum, pageSize);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/gatherings")
    public ResponseEntity<ApiResponse> gatherings(){
        ApiResponse apiResponse = gatheringService.gatherings();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/v2/gatherings")
    public ResponseEntity<ApiResponse> gatheringsV2(){
        ApiResponse apiResponse = gatheringService.gatheringsV2();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/v3/gatherings")
    public ResponseEntity<ApiResponse> gatheringsV3(){
        ApiResponse apiResponse = gatheringService.gatheringsV3();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/v4/gatherings")
    public ResponseEntity<ApiResponse> gatheringsV4(){
        ApiResponse apiResponse = gatheringService.gatheringsV4();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/v5/gatherings")
    public ResponseEntity<ApiResponse> gatheringsV5(){
        ApiResponse apiResponse = gatheringService.gatheringsV5();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/gathering")
    public ResponseEntity<ApiResponse> gatheringCategory(@RequestParam String category,
                                                         @RequestParam(defaultValue = "1") Integer pageNum,
                                                         @RequestParam(defaultValue = "10") Integer pageSize){
        ApiResponse apiResponse = gatheringService.gatheringCategory(category, pageNum, pageSize);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/gatherings/like")
    public ResponseEntity<ApiResponse> gatheringsLike(@RequestParam(defaultValue = "1") Integer pageNum,
                                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                                      @Username Long userId){
        ApiResponse apiResponse = gatheringService.gatheringsLike(pageNum, pageSize, userId);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
