package api.common.mapper;

import entity.gathering.Gathering;
import entity.like.Like;
import entity.user.User;

public class LikeMapper {

    public static Like toLike(Gathering gathering, User user) {
        return Like.builder()
                .gathering(gathering)
                .likedBy(user)
                .build();
    }
}
