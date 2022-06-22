package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
public class UsuarioControle {
	
	@Autowired
	UsuarioRepositorio repositorio;
	@Autowired
	UsuarioSelecionador selecionador;
	
	@GetMapping("/usuario/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable Long id) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Usuario> usuarios = repositorio.findAll();
		Usuario selecionado = selecionador.selecionar(usuarios, id);
		if (selecionado == null) {
			return new ResponseEntity<>(status);
		} else {
			status = HttpStatus.FOUND;
			return new ResponseEntity<Usuario>(selecionado, status);
		}
	}
	
	@GetMapping("/usuarios")
	public ResponseEntity<List<Usuario>> obterUsuarios(){
		HttpStatus status = HttpStatus.NOT_FOUND;
		List<Usuario> usuarios = repositorio.findAll();
		if (usuarios == null) {
			return new ResponseEntity<>(status);
		} else {
			return new ResponseEntity<List<Usuario>>(usuarios, status);
		}
	}
}
