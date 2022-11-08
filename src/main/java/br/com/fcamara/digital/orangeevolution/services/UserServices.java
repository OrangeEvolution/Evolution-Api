package br.com.fcamara.digital.orangeevolution.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.fcamara.digital.orangeevolution.data.vo.TrailVO;
import br.com.fcamara.digital.orangeevolution.data.vo.UserVO;
import br.com.fcamara.digital.orangeevolution.exception.ResourceNotFoundException;
import br.com.fcamara.digital.orangeevolution.model.Permission;
import br.com.fcamara.digital.orangeevolution.model.Trail;
import br.com.fcamara.digital.orangeevolution.model.User;
import br.com.fcamara.digital.orangeevolution.repository.PermissionRepository;
import br.com.fcamara.digital.orangeevolution.repository.UserRepository;

@Service
public class UserServices implements UserDetailsService {

	@Autowired
	UserRepository repository;
	@Autowired
	PermissionRepository repositoryPermission;

	private UserVO convertToUserVO(User card) {
		return toConvert(card);

	}

	private User toConvert(UserVO userVO) {
		List<Trail> trails = new ArrayList<>();
		User user = new User(userVO.getKey(), userVO.getUserName(), userVO.getFullName(), userVO.getPassword(), trails,
				true, true, true, true, null);
		for (TrailVO trailVO : userVO.getTrails()) {
			Trail trail = new Trail(trailVO.getKey(), trailVO.getName(), trailVO.getDescription(),
					trailVO.getMounted_by());
			trails.add(trail);

		}
		user.setTrails(trails);
		return user;
	}

	private UserVO toConvert(User user) {
		List<TrailVO> trails = new ArrayList<>();
		UserVO userVO = new UserVO(user.getId(), user.getUsername(), user.getFullName(), user.getPassword(), trails);
		for (Trail trail : user.getTrails()) {
			TrailVO trailVO = new TrailVO(trail.getId(), trail.getName(), trail.getDescription(),
					trail.getMounted_by());
			trails.add(trailVO);
		}
		userVO.setTrails(trails);
		return userVO;

	}

	public UserVO create(UserVO user) {
		BCryptPasswordEncoder criptografar = new BCryptPasswordEncoder();
		var entity = toConvert(user);
		List<Permission> permisssions = new ArrayList<>();
		permisssions.add(repositoryPermission.findById(3L).orElseThrow());
		entity.setPermissions(permisssions);
		entity.setPassword(criptografar.encode(entity.getPassword()));
		var cardVO = toConvert(repository.save(entity));
		return cardVO;
	}

	public UserVO findById(Long id) {
		return toConvert(repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not records found for thins ID")));
	}

	public Page<UserVO> findAll(Pageable pageable) {
		var page = repository.findAll(pageable);
		return page.map(this::convertToUserVO);
	}

	public void delete(Long id) {
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Unable to delete in this scenario"));
		repository.delete(entity);
	}

	public UserVO update(UserVO user) {
		var entity = repository.findById(user.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("Not records found for thins ID"));
		entity.setFullName(user.getFullName());
		var vo = toConvert(repository.save(entity));
		return vo;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = repository.findByUsername(username);
		if (user != null) {
			return user;
		} else {
			throw new UsernameNotFoundException("Username" + username + "Not Found");
		}
	}

	public User findUser(String username) {
		return repository.findByUsername(username);
	}
}
