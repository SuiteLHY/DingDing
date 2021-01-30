package github.com.suitelhy.dingding.core.infrastructure.web;

import github.com.suitelhy.dingding.core.domain.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * 用户（安全认证）基本信息
 *
 * @Description Spring Security 认证所需 {@link UserDetails} 实例.
 * @Design {@link UserDetails} 定制化抽象实现.
 * · 【安全设计】使用抽象类型且对构造方法进行限制是出于安全性考虑(其中很大一部分原因是为了杜绝开发人员的<b>不规范</b>操作).
 * @Solution · {@link <a href="https://blog.csdn.net/a1064072510/article/details/103848966">OAuth2  [org.springframework.data.redis.serializer.SerializationException: Cannot serialize_知我饭否-CSDN博客_springcloud oauth2.0 tokenstore serializationexcep</a>}
 * @see UserDetails
 */
@Slf4j
public abstract class AbstractSecurityUser
        implements UserDetails {

//    @NotNull
//    private final /*transient */PasswordEncoder passwordEncoder;

    /*// Redis 存储 Token 时会将当前 {@link SecurityUser} 对象进行反序列化, 所以不能持有该引用.
    private final SecurityUserService securityUserService;*/

//    /**
//     * 当前用户 Entity
//     *
//     * @see User
//     */
//    @NotNull
//    private final /*transient */User user;

    /**
     * 用户密码
     *
     * @Description 密文.
     */
    @NotNull
    private final String password;

    @NotNull
    private final String username;

    @NotNull
    private final Set<GrantedAuthority> authorities;

    /**
     * [标识]账户是否未过期
     */
    private final boolean accountNonExpired;

    /**
     * [标识]账户是否未被锁定
     */
    private final boolean accountNonLocked;

    /**
     * [标识]账户是否凭证未过期
     */
    private final boolean credentialsNonExpired;

    /**
     * [标识]账户是否可用
     */
    private final boolean enabled;

    /**
     * (Constructor)
     *
     * @param username
     * @param password
     * @param authorities
     * @param accountNonExpired     {@link this#isAccountNonExpired()}
     * @param accountNonLocked      {@link this#isAccountNonLocked()}
     * @param credentialsNonExpired {@link this#isCredentialsNonExpired()}
     * @param enabled               {@link this#isEnabled()}
     * @throws AccountStatusException
     * @throws IllegalArgumentException
     */
    protected AbstractSecurityUser(@NotNull String username
            , @NotNull String password
            , @NotNull Collection<? extends GrantedAuthority> authorities
            , boolean accountNonExpired
            , boolean accountNonLocked
            , boolean credentialsNonExpired
            , boolean enabled)
            throws AccountStatusException, IllegalArgumentException {
        if (!User.Validator.USER.username(username)) {
            throw new IllegalArgumentException("无效的 <param>username</param>");
        }
        if (null == password) {
            throw new IllegalArgumentException("无效的 <param>password</param> -> null");
        }
        if (null == authorities) {
            throw new IllegalArgumentException("无效的 <param>authorities</param> -> null");
        }

        /*if (user.isEmpty()) {
            if (User.Validator.USER.validateId(user)
                    && user.isEntityLegal()
                    && !Boolean.FALSE.equals(user.isEntityPersistence())) {
                if (Account.StatusVo.DESTRUCTION.equals(user.getStatus())) {
                    throw new AccountExpiredException("用户已注销");
                }
                if (Account.StatusVo.ABNORMAL.equals(user.getStatus())) {
                    throw new DisabledException("用户异常");
                }
                if (Account.StatusVo.LOCKED.equals(user.getStatus())) {
                    throw new LockedException("用户异常");
                }
            }
            throw new IllegalArgumentException("无效的 User -> " + user);
        }*/

        this.password = password;
        this.username = username;
        this.authorities = (Set<GrantedAuthority>) authorities;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        /*return null == user.getPassword()
                ? null
                : passwordEncoder.encode(user.getPassword());*/
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /*@NotNull
    public UserDto getUserDto() {
        return UserDto.Factory.USER_DTO.create(this.user);
    }*/

//    @NotNull
//    public BasicUserDto getUserInfo() {
//        return BasicUserDto.Factory.USER_DTO.create(this.user);
//    }

    /**
     * 账户是否未过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
//        return !Account.StatusVo.DESTRUCTION.equals(user.getStatus());
        return accountNonExpired;
    }

    /**
     * 账户是否未被锁定
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
//        return !Account.StatusVo.LOCKED.equals(user.getStatus());
        return accountNonLocked;
    }

    /**
     * 账户是否凭证未过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        // 凭证未过期... 暂无业务设计
//        return Account.StatusVo.NORMAL.equals(user.getStatus());
        return credentialsNonExpired;
    }

    /**
     * 账户是否可用
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
//        return !user.isEmpty();
        return enabled;
    }

    /**
     * 判断 {@link AbstractSecurityUser} 对象是否可用
     *
     * @return {@link Boolean#TYPE}
     * @Description 综合判断.
     */
    public boolean isEmpty() {
        return !this.isAccountNonExpired()
                || !this.isAccountNonLocked()
                || !this.isCredentialsNonExpired()
                || !this.isEnabled();
    }

    @NotNull
    @Override
    public String toString() {
        /*StringBuilder result = new StringBuilder("{");
        result.append("username:").append(username);
        result.append(",password:").append("******");
        result.append(",authorities:").append(Arrays.toString(authorities.toArray()));
        result.append(",accountNonExpired:").append(accountNonExpired);
        result.append(",accountNonLocked:").append(accountNonLocked);
        result.append(",credentialsNonExpired:").append(credentialsNonExpired);
        result.append(",enabled:").append(enabled);
        result.append("}");*/
        return String.format("{username=\"%s\", password=\"%s\", authorities=%s, accountNonExpired=%s, accountNonLocked=%s, credentialsNonExpired=%s, enabled=%s}"
                , username
                , "******"
                , Arrays.toString(authorities.toArray())
                , accountNonExpired
                , accountNonLocked
                , credentialsNonExpired
                , enabled);
    }

}
