package com.elbehiry.followusers.model;

import com.elbehiry.followusers.view.fragment.UserShowFragment;

/**
 * Created by elbehiry on 15/10/16.
 */

public class User {

    private String name;
    private String image_link;
    private Boolean isfollow;





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsfollow() {
        return isfollow;
    }

    public void setIsfollow(Boolean isfollow) {
        this.isfollow = isfollow;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
