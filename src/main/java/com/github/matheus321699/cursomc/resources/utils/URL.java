package com.github.matheus321699.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/*
 * Classe auxiliar com operação para converter string de categorias 
 * que vem da requisição para um objeto do tipo List Integer de acordo
 * com o método searche do ProdutoService.
 */
public class URL {
	
	/*
	 * encode: Converter uma string que pode ter um espaço em branco ou
	 * caracter especial pra uma string com caracteres básicos.
	 */
	/*
	 * decode: descodificar parâmetro do encode.
	 */
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "";
		}
	}

	public static List<Integer> decodeIntList(String s) {
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>(); 
		for (int i=0; i<vet.length; i++) {
			list.add(Integer.parseInt(vet[1]));
		}
		return list;
		/*
		 * Operação com Lambda: return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
		 */
		}
}
