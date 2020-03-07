package net.rogerplayer.csso.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import net.rogerplayer.csso.domain.CSSOUser;
import net.rogerplayer.csso.dto.CSSOUserDTO;
import net.rogerplayer.csso.repositories.CSSOUserRepository;
import net.rogerplayer.csso.services.exceptions.DataIntegrityException;
import net.rogerplayer.csso.services.exceptions.ObjectNotFoundException;

@Service
public class CSSOUserService {

	@Autowired
	private CSSOUserRepository cssoUserRepository;

	public CSSOUser find(Long id) {
		Optional<CSSOUser> obj = cssoUserRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + CSSOUser.class.getName()));
	}

	public CSSOUser findByUserName(String username) {
		Optional<CSSOUser> obj = cssoUserRepository.findByUserName(username);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Usuário não encontrado! Username: " + username + ", Tipo: " + CSSOUser.class.getName()));
	}

	public CSSOUser insert(CSSOUser obj) {
		obj.setId(null);
		return cssoUserRepository.save(obj);
	}

	public CSSOUser update(CSSOUser obj) {
		find(obj.getId()); // garantir que objeto existe na base
		return cssoUserRepository.save(obj);
	}

	public void delete(Long id) {
		find(id); // garantir que objeto existe na base
		try {
			cssoUserRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

	public List<CSSOUser> findAll() {
		return cssoUserRepository.findAll();
	}

	public Page<CSSOUser> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return cssoUserRepository.findAll(pageRequest);
	}

	public CSSOUserDTO toDTO(CSSOUser cssoUser) {
		CSSOUserDTO dto = new CSSOUserDTO();
		BeanUtils.copyProperties(cssoUser, dto);
		return dto;
	}

	public CSSOUser fromDTO(CSSOUserDTO dto) {
		CSSOUser cssoUser = new CSSOUser();
		BeanUtils.copyProperties(dto, cssoUser);
		return cssoUser;
	}

}
