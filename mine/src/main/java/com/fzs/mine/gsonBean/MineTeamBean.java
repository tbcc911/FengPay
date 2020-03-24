package com.fzs.mine.gsonBean;

import java.util.List;


public class MineTeamBean {
    public String code;
    public String message;
    public Data data;
    
    public static class Data{
        public String nickname;
        public String avatarUrl;
        public String teamTotalCount;
        public String monthPublishTotalCount;
        public String todayPublishTotalCount;
        public String todayIncreasedMemberCount;
        public String sttails;
        public List<TeamList> teamList;
    }
    
    
    public static class TeamList{
        public String memberId;
        public String nickname;
        public String avatarUrl;
        public String phone;
        public String shareCount;
        public String publishCount;
        public String registerTime;
    }
}
