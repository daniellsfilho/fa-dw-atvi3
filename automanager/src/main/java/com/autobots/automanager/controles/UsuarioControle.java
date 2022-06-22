package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.dtos.FornecedorDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.criadores.CriadorUsuario;
import com.autobots.automanager.modelos.selecionadores.EmpresaSelecionador;
import com.autobots.automanager.modelos.selecionadores.UsuarioSelecionador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
public class UsuarioControle {
	
	@Autowired
	UsuarioRepositorio repositorio;
	@Autowired
	UsuarioSelecionador selecionador;
	@Autowired
	CriadorUsuario criador;
	@Autowired
	EmpresaRepositorio empresaRepositorio;
	@Autowired
	EmpresaSelecionador empresaSelecionador;
	
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
			status = HttpStatus.FOUND;
			return new ResponseEntity<List<Usuario>>(usuarios, status);
		}
	}
	
	@PostMapping("/usuario/cadastrar")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody FornecedorDTO usuarioDTO){
		HttpStatus status = HttpStatus.CONFLICT;
		List<Empresa> empresas = empresaRepositorio.findAll();
		Empresa empresa = empresaSelecionador.selecionarRazaoSocial(empresas, usuarioDTO.getRazaoSocial());
		Usuario novoUsuario = null;
		if (empresa == null) {
			status = HttpStatus.BAD_REQUEST;
		} else {
			
			if (usuarioDTO.getPerfilUsuario().toString() == "FORNECEDOR") {
				criador.registrarMercadorias(usuarioDTO);
				novoUsuario = criador.criar(usuarioDTO, usuarioDTO.getPerfilUsuario());
			} else {
				novoUsuario = criador.criar(usuarioDTO, usuarioDTO.getPerfilUsuario());
			}
			
			repositorio.save(novoUsuario);
			empresa.getUsuarios().add(novoUsuario);
			empresaRepositorio.save(empresa);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}
}
