package br.com.api.produtos.controle;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

@CrossOrigin(origins = "https://cabeloeart.vercel.app")
    @GetMapping("/dados")
    public ResponseEntity<String> getDados() {
        return ResponseEntity.ok("Dados da API");
    }


}
