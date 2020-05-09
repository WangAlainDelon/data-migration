package io.choerodon.migration.domian.hzero;


import io.choerodon.migration.domian.AuditDomain;

import javax.persistence.*;

/**
 * @author superlee
 * @since 2019-04-22
 */
@Table(name = "gitlab_acitve_user")
public class GitlabAcitveUser extends AuditDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gitLabUsrId;

    private Long name;

    private String mail;

    @Column(name = "username")
    private String userName;

    public Long getGitLabUsrId() {
        return gitLabUsrId;
    }

    public void setGitLabUsrId(Long gitLabUsrId) {
        this.gitLabUsrId = gitLabUsrId;
    }

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
