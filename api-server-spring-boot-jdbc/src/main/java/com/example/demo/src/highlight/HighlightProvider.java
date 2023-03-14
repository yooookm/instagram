package com.example.demo.src.highlight;

import com.example.demo.config.BaseException;
import com.example.demo.src.highlight.model.GetHighlightByUserIdRes;
import com.example.demo.src.user.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class HighlightProvider {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final HighlightDao highlightDao;
    private final UserDao userDao;

    public HighlightProvider(HighlightDao highlightDao, UserDao userDao) {
        this.highlightDao = highlightDao;
        this.userDao = userDao;
    }

    public List<GetHighlightByUserIdRes> getHighlightsByUserId(int userId) throws BaseException {
        throwIfInvalidUserIdDetected(userId);
        throwIfInvalidUserStatus(userId);
        try{
            List<GetHighlightByUserIdRes> getHighlightByUserIdResList= new ArrayList<>();
            if(highlightDao.checkHighlightByUserId(userId)==1){
                getHighlightByUserIdResList = highlightDao.getHighlightsByUserId(userId);
            }
            return getHighlightByUserIdResList;
        }catch (Exception exception) {
            logger.error("App - getStoryByUserId Provider Error", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private void throwIfInvalidUserIdDetected(int userId) throws BaseException {
        if (userDao.checkUserId(userId) == 0) {
            throw new BaseException(GET_USERS_INVALID_USER_ID);
        }
    }

    private void throwIfInvalidUserStatus(int userId) throws BaseException {
        if (userDao.checkUserAccountStatus(userId).equals("INACTIVE")) {
            throw new BaseException(POST_USERS_ACCOUNT_INACTIVE);
        }
        if (userDao.checkUserAccountStatus(userId).equals("DELETED")) {
            throw new BaseException(POST_USERS_ACCOUNT_DELETED);
        }

    }
}
