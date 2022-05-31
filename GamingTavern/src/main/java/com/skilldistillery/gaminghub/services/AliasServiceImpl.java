package com.skilldistillery.gaminghub.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skilldistillery.gaminghub.entities.Alias;
import com.skilldistillery.gaminghub.repositories.AliasRepository;
import com.skilldistillery.gaminghub.repositories.UserRepository;

@Service
public class AliasServiceImpl implements AliasService {
<<<<<<< HEAD
	
		@Autowired
		private AliasRepository aliasRepo;
		
		@Autowired
		private UserRepository userRepo;

		@Override
		public List<Alias> index() {
			return aliasRepo.findAll();
		}
		
		@Override
		public Alias getAliasById(int aliasId) {
			Optional<Alias> op = aliasRepo.findById(aliasId);
			if (op.isPresent()) {
				return op.get();
			} else {
				return null;
			}
		}
		
		@Override
		public Alias createAlias(Alias alias) {
			return aliasRepo.saveAndFlush(alias);
		}
=======
>>>>>>> ac5768c592865b39d9a76f056b99d8afbc521c3a

	@Autowired
	private AliasRepository aliasRepo;

	@Autowired
	private UserRepository userRepo;

	@Override
	public List<Alias> index() {
		return aliasRepo.findAll();
	}

	@Override
	public List<Alias> getAliasesByUsername(String username) {
		return aliasRepo.findByUserUsername(username);
	}
	
	@Override
	public Alias getAliasById(int aliasId) {
		Optional<Alias> op = aliasRepo.findById(aliasId);
		if (op.isPresent()) {
			return op.get();
		} else {
			return null;
		}
	}

	@Override
	public Alias createAlias(Alias alias) {
		return aliasRepo.saveAndFlush(alias);
	}

	@Override
	public Alias updateAlias(String user, Alias alias, int aliasId) {
		Optional<Alias> op = aliasRepo.findById(aliasId);
		if (op.isPresent()) {
			Alias result = op.get();
			if (result.getUser().equals(user)) {
				alias.setId(aliasId);
				return aliasRepo.saveAndFlush(alias);

			}
		}

		return null;
	}

<<<<<<< HEAD

=======
	@Override
	public boolean deleteAlias(String username, int aliasId) {
		Optional<Alias> op = aliasRepo.findById(aliasId);
		if (op.isPresent()) {
			Alias result = op.get();
			if (result.getId() == (aliasId)) {
				aliasRepo.deleteById(aliasId);
				op = aliasRepo.findById(aliasId);
				return !op.isPresent();
			}
		}
		return false;
>>>>>>> ac5768c592865b39d9a76f056b99d8afbc521c3a
	}

}