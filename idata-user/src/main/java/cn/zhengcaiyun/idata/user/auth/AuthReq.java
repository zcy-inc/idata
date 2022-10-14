package cn.zhengcaiyun.idata.user.auth;

import cn.zhengcaiyun.idata.user.constant.enums.AuthActionEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthResourceTypeEnum;
import cn.zhengcaiyun.idata.user.constant.enums.AuthSubjectTypeEnum;

import java.util.List;

public class AuthReq {

    private AuthSubjectTypeEnum subjectType;
    private String subjectIdentifier;
    private AuthResourceTypeEnum resourceType;
    private List<String> resourceIdentifiers;
    private AuthActionEnum actionType;

    private AuthReq() {
    }

    public AuthSubjectTypeEnum getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(AuthSubjectTypeEnum subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectIdentifier() {
        return subjectIdentifier;
    }

    public void setSubjectIdentifier(String subjectIdentifier) {
        this.subjectIdentifier = subjectIdentifier;
    }

    public AuthResourceTypeEnum getResourceType() {
        return resourceType;
    }

    public void setResourceType(AuthResourceTypeEnum resourceType) {
        this.resourceType = resourceType;
    }

    public List<String> getResourceIdentifiers() {
        return resourceIdentifiers;
    }

    public void setResourceIdentifiers(List<String> resourceIdentifiers) {
        this.resourceIdentifiers = resourceIdentifiers;
    }

    public AuthActionEnum getActionType() {
        return actionType;
    }

    public void setActionType(AuthActionEnum actionType) {
        this.actionType = actionType;
    }

    public static final class AuthReqBuilder {
        private AuthReq authReq;

        private AuthReqBuilder() {
            authReq = new AuthReq();
        }

        public static AuthReqBuilder newAuthReq() {
            return new AuthReqBuilder();
        }

        public AuthReqBuilder setSubject(AuthSubjectTypeEnum subjectType, String subjectIdentifier) {
            authReq.setSubjectType(subjectType);
            authReq.setSubjectIdentifier(subjectIdentifier);
            return this;
        }

        public AuthReqBuilder setResources(AuthResourceTypeEnum resourceType, List<String> resourceIdentifiers) {
            authReq.setResourceType(resourceType);
            authReq.setResourceIdentifiers(resourceIdentifiers);
            return this;
        }

        public AuthReqBuilder setAction(AuthActionEnum actionType) {
            authReq.setActionType(actionType);
            return this;
        }

        public AuthReq build() {
            return authReq;
        }
    }
}
