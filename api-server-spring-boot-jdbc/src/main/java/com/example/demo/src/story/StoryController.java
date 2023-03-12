package com.example.demo.src.story;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.story.model.GetStoryRes;
import com.example.demo.src.story.model.GetStoryUserRes;
import com.example.demo.src.user.UserProvider;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.GET_STORIES_EMPTY_USER_ID;

@RestController
@RequestMapping("/app/stories")
public class StoryController {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoryProvider storyProvider;
    @Autowired
    private final StoryService storyService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final UserProvider userProvider;

    public StoryController(StoryProvider storyProvider, StoryService storyService, JwtService jwtService, UserProvider userProvider) {
        this.storyProvider = storyProvider;
        this.storyService = storyService;
        this.jwtService = jwtService;
        this.userProvider = userProvider;
    }

    /**
     * 스토리 목록 조회 API
     * [GET] /app/stories/followings
     *
     * @return BaseResponse<List<GetStoryUserRes>>
     */
    @GetMapping("/followings")
    public BaseResponse<List<GetStoryUserRes>> getStoryUsers() {
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetStoryUserRes> getStoryUserResList = storyProvider.getStoryUsers(userIdByJwt);
            return new BaseResponse<>(getStoryUserResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저의 스토리 전체 조회 API
     * [GET] /app/stories?user-id=
     *
     * @return BaseResponse<List<GetStoryRes>>
     */
    @GetMapping("")
    public BaseResponse<List<GetStoryRes>> getStoryByUserId(@RequestParam("user-id")Integer userId) {
        if(userId==null){
            return new BaseResponse<>(GET_STORIES_EMPTY_USER_ID);
        }
        try{
            int userIdByJwt = jwtService.getUserId();
            List<GetStoryRes> getStoryResList;
            getStoryResList = storyProvider.getStoryByUserId(userIdByJwt,userId);
            return new BaseResponse<>(getStoryResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
