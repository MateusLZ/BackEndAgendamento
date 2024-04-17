package br.com.api.produtos.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CadastrarProdutoRequest {
    private ProdutoModelo produto;
    private List<String> userIds;
}
