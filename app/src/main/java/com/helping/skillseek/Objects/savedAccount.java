package com.helping.skillseek.Objects;

public class savedAccount {
  boolean liked;
  String likedID;

  public  savedAccount(){

  }
    public savedAccount(boolean b, String likedID) {
        this.liked = b;
        this.likedID = likedID;
    }

    public savedAccount(boolean liked) {
      this.liked = liked;
    }

  public String getLikedID() {
    return likedID;
  }

  public void setLikedID(String likedID) {

      this.likedID = likedID;
    }

    public boolean isLiked() {

      return liked;
    }

    public void setLiked(boolean liked) {

      this.liked = liked;
    }
}
