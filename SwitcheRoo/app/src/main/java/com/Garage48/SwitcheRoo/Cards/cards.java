package com.Garage48.SwitcheRoo.Cards;

/**
 * Created by manel on 9/5/2017.
 */

public class cards {
    private String itemId;
    private String parentId;
    private String name;
    private String desc;
    private String profileImageUrl;
    public cards (String itemId, String parentId, String name, String desc, String profileImageUrl){
        this.itemId = itemId;
        this.parentId = parentId;
        this.name = name;
        this.desc = desc;
        this.profileImageUrl = profileImageUrl;
    }

    public String getItemId(){
        return itemId;
    }
    public String getparentId(){
        return parentId;
    }

    public String getName(){
        return name;
    }

    public String getProfileImageUrl(){
        return profileImageUrl;
    }
    public void setProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
}
