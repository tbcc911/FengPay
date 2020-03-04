package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "User")
public class User extends Model {
    @Column(name = "userId")
    private String userId;// 用户ID

    @Column(name = "acount")
    private String acount;// 帐号
    
    @Column(name = "phone")
    private String phone;// 手机

    @Column(name = "password")
    private String password;// 密码
    
    @Column(name = "head")
    private String head;// 头像

    @Column(name = "nickname")
    private String nickName;//昵称
    
    @Column(name = "sex")
    private String sex;//性别
    
    @Column(name = "personalSign")
    private String personalSign;//个性签名
    
    @Column(name = "balnace")
    private String balnace;//余额
    
    @Column(name = "alipayAcount")
    private String alipayAcount;//支付宝帐号
    
    @Column(name = "alipayName")
    private String alipayName;//支付宝姓名
    
    @Column(name = "inviter")
    private String inviter;//推荐人帐号
    
    @Column(name = "bankName")
    private String bankName;//开户行名称
    @Column(name = "bankAddress")
    private String bankAddress;//开户行地址
    @Column(name = "bankAccount")
    private String bankAccount;//银行卡帐号
    @Column(name = "bankMasterName")
    private String bankMasterName;//银行卡持卡人姓名
    
    @Column(name = "loginType")
    private String loginType;//第三方登录类型 weiixn:微信 qq:QQ alipay:支付宝 sina:新浪微博
    @Column(name = "openId")
    private String openId;// 对应第三方平台登录的平台ID

    @Column(name = "authCode")
    private String authCode;//验证码
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "tokenHead")
    private String tokenHead;

    @Column(name = "inviteCode")
    private String inviteCode;// 我的邀请码

    @Column(name = "idCard")
    private String idCard;// 身份证号码
    @Column(name = "idCardBack")
    private String idCardBack;// 身份证反面
    @Column(name = "idCardFront")
    private String idCardFront;// 身份证正面

    @Column(name = "isActivation")
    private int isActivation;// 是否激活

    @Column(name = "agentRatio")
    private String agentRatio;// 代理比例
    @Column(name = "alipayUid")
    private String alipayUid;// 支付宝UID
    @Column(name = "availableIntegration")
    private String availableIntegration;// 可用积分
    @Column(name = "createTime")
    private String createTime;// 注册时间
    @Column(name = "effectiveStatus")
    private String effectiveStatus;// 会员有效状态: 0无效  1有效
    @Column(name = "freezeIntegration")
    private String freezeIntegration;// 冻结积分
    @Column(name = "integration")
    private String integration;// 总积分
    @Column(name = "memberId")
    private String memberId;// 会员ID
    @Column(name = "userName")
    private String userName;// 姓名
    @Column(name = "status")
    private String status;// 账号启用状态: 0禁用 1启用
    @Column(name = "alipayBindingStatus")
    private String alipayBindingStatus;// 支付宝绑定状态: 0->未绑定; 1->已绑定
    @Column(name = "successLevel")
    private String successLevel;// 成功等级
    @Column(name = "successRate")
    private String successRate;// 成功率(直接在后面加上%号)
    
    private String usdtAddress;//USDT地址

    public String getAlipayBindingStatus() {
        return alipayBindingStatus;
    }
    public User setAlipayBindingStatus(String alipayBindingStatus) {
        this.alipayBindingStatus = alipayBindingStatus;
        return this;
    }

    public String getAgentRatio() {
        return agentRatio;
    }
    public User setAgentRatio(String agentRatio) {
        this.agentRatio = agentRatio;
        return this;
    }

    public String getAlipayUid() {
        return alipayUid;
    }
    public User setAlipayUid(String alipayUid) {
        this.alipayUid = alipayUid;
        return this;
    }

    public String getAvailableIntegration() {
        return availableIntegration;
    }
    public User setAvailableIntegration(String availableIntegration) {
        this.availableIntegration = availableIntegration;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }
    public User setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getEffectiveStatus() {
        return effectiveStatus;
    }
    public User setEffectiveStatus(String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
        return this;
    }

    public String getFreezeIntegration() {
        return freezeIntegration;
    }
    public User setFreezeIntegration(String freezeIntegration) {
        this.freezeIntegration = freezeIntegration;
        return this;
    }

    public String getIntegration() {
        return integration;
    }
    public User setIntegration(String integration) {
        this.integration = integration;
        return this;
    }

    public String getUserName() {
        return userName;
    }
    public User setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getStatus() {
        return status;
    }
    public User setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getMemberId() {
        return memberId;
    }
    public User setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }
    
    
    public String getPhone() {
        return phone;
    }
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAcount() {
        return acount;
    }
    public User setAcount(String acount) {
        this.acount = acount;
        return this;
    }

    public String getPassword() {
        return password;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getHead() {
        return head;
    }
    public User setHead(String head) {
        this.head = head;
        return this;
    }


    public String getBalnace() {
        return balnace;
    }
    public User setBalnace(String balnace) {
        this.balnace = balnace;
        return this;
    }

    public String getAlipayAcount() {
        return alipayAcount;
    }
    public User setAlipayAcount(String alipayAcount) {
        this.alipayAcount = alipayAcount;
        return this;
    }

    public String getAlipayName() {
        return alipayName;
    }
    public User setAlipayName(String alipayName) {
        this.alipayName = alipayName;
        return this;
    }

    public String getInviter() {
        return inviter;
    }
    public User setInviter(String inviter) {
        this.inviter = inviter;
        return this;
    }

    public String getBankName() {
        return bankName;
    }
    public User setBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public String getBankAddress() {
        return bankAddress;
    }
    public User setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
        return this;
    }

    public String getBankAccount() {
        return bankAccount;
    }
    public User setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
        return this;
    }

    public String getBankMasterName() {
        return bankMasterName;
    }
    public User setBankMasterName(String bankMasterName) {
        this.bankMasterName = bankMasterName;
        return this;
    }


    public String getLoginType() {
        return loginType;
    }
    public User setLoginType(String loginType) {
        this.loginType = loginType;
        return this;
    }

    public String getNickName() {
        return nickName;
    }
    public User setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public String getOpenId() {
        return openId;
    }
    public User setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public String getSex() {
        return sex;
    }
    public User setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getPersonalSign() {
        return personalSign;
    }
    public User setPersonalSign(String personalSign) {
        this.personalSign = personalSign;
        return this;
    }

    public String getAuthCode() {
        return authCode;
    }
    public User setAuthCode(String authCode) {
        this.authCode = authCode;
        return this;
    }

    public String getToken() {
        return token;
    }
    public User setToken(String token) {
        this.token = token;
        return this;
    }

    public String getTokenHead() {
        return tokenHead;
    }
    public User setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
        return this;
    }


    public String getInviteCode() {
        return inviteCode;
    }
    public User setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
        return this;
    }

    public String getIdCard() {
        return idCard;
    }
    public User setIdCard(String idCard) {
        this.idCard = idCard;
        return this;
    }
    public String getIdCardBack() {
        return idCardBack;
    }
    public User setIdCardBack(String idCardBack) {
        this.idCardBack = idCardBack;
        return this;
    }
    public String getIdCardFront() {
        return idCardFront;
    }
    public User setIdCardFront(String idCardFront) {
        this.idCardFront = idCardFront;
        return this;
    }

    public String getUserId() {
        return userId;
    }
    public User setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public boolean getIsActivation() {
        return isActivation==1;
    }
    public User setIsActivation(boolean isActivation) {
        if (isActivation){
            this.isActivation=1;
        }else{
            this.isActivation=0;
        }
        return this;
    }

    public String getUsdtAddress() {
        return usdtAddress;
    }
    public User setUsdtAddress(String usdtAddress) {
        this.usdtAddress = usdtAddress;
        return this;
    }

    public String getSuccessLevel() {
        return successLevel;
    }
    public User setSuccessLevel(String successLevel) {
        this.successLevel = successLevel;
        return this;
    }

    public String getSuccessRate() {
        return successRate;
    }
    public User setSuccessRate(String successRate) {
        this.successRate = successRate;
        return this;
    }

}
