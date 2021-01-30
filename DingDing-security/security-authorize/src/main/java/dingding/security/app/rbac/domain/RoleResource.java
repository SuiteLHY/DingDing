package dingding.security.app.rbac.domain;

import java.util.Date;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;

/**
 * 角色 - 资源 关系表
 *
 * @author zhailiang
 */
@Entity
@Table(name = "SECURITY_ROLE_RESOURCE")
public class RoleResource {

    /**
     * 数据库表主键
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 审计日志，记录条目创建时间
     *
     * @Description 自动赋值，不需要程序员手工赋值
     */
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    /**
     * 资源
     */
    @ManyToOne
    private Resource resource;

    /**
     * 角色
     */
    @ManyToOne
    private Role role;

    //===== Getter And Setter =====//

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the authorization
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @param resource the authorization to set
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

    /**
     * @return the role
     */
    public Role getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Role role) {
        this.role = role;
    }

}
