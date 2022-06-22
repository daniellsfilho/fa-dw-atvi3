package com.autobots.automanager.dtos;

import java.util.List;

import com.autobots.automanager.entidades.Mercadoria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FornecedorDTO extends UsuarioDTO{
	List<Mercadoria> mercadorias;
}
