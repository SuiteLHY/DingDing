/**
 * 
 */
package dingding.security.app.rbac.service.impl;

import dingding.security.app.rbac.domain.Admin;
import dingding.security.app.rbac.domain.RoleAdmin;
import dingding.security.app.rbac.dto.AdminCondition;
import dingding.security.app.rbac.dto.AdminInfo;
import dingding.security.app.rbac.repository.AdminRepository;
import dingding.security.app.rbac.repository.RoleAdminRepository;
import dingding.security.app.rbac.repository.RoleRepository;
import dingding.security.app.rbac.repository.spec.AdminSpec;
import dingding.security.app.rbac.repository.support.QueryResultConverter;
import dingding.security.app.rbac.service.AdminService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhailiang
 *
 */
@Service
@Transactional
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleAdminRepository roleAdminRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/* (non-Javadoc)
	 * @see AdminService#create(AdminInfo)
	 */
	@Override
	public AdminInfo create(AdminInfo adminInfo) {
		
		Admin admin = new Admin();
		BeanUtils.copyProperties(adminInfo, admin);
		admin.setPassword(passwordEncoder.encode("123456"));
		adminRepository.save(admin);
		adminInfo.setId(admin.getId());
		
		createRoleAdmin(adminInfo, admin);
		
		return adminInfo;
	}

	/* (non-Javadoc)
	 * @see AdminService#update(AdminInfo)
	 */
	@Override
	public AdminInfo update(AdminInfo adminInfo) {
		
		Admin admin = adminRepository.findById(adminInfo.getId()).get();
		BeanUtils.copyProperties(adminInfo, admin);
		
		createRoleAdmin(adminInfo, admin);
		
		return adminInfo;
	}

	/**
	 * 创建角色用户关系数据。
	 * @param adminInfo
	 * @param admin
	 */
	private void createRoleAdmin(AdminInfo adminInfo, Admin admin) {
		if(CollectionUtils.isNotEmpty(admin.getRoles())){
			roleAdminRepository.deleteAll(admin.getRoles());
		}
		RoleAdmin roleAdmin = new RoleAdmin();
		roleAdmin.setRole(roleRepository.getOne(adminInfo.getRoleId()));
		roleAdmin.setAdmin(admin);
		roleAdminRepository.save(roleAdmin);
	}

	/* (non-Javadoc)
	 * @see AdminService#delete(java.lang.Long)
	 */
	@Override
	public void delete(Long id) {
		adminRepository.deleteById(id);
	}

	/* (non-Javadoc)
	 * @see AdminService#getInfo(java.lang.Long)
	 */
	@Override
	public AdminInfo getInfo(Long id) {
		Admin admin = adminRepository.findById(id).get();
		AdminInfo info = new AdminInfo();
		BeanUtils.copyProperties(admin, info);
		return info;
	}

	/* (non-Javadoc)
	 * @see AdminService#query(AdminInfo, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<AdminInfo> query(AdminCondition condition, Pageable pageable) {
		Page<Admin> admins = adminRepository.findAll(new AdminSpec(condition), pageable);
		return QueryResultConverter.convert(admins, AdminInfo.class, pageable);
	}

}
