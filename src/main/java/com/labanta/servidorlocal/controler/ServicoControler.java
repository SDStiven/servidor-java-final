package com.labanta.servidorlocal.controler;

import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.repository.ServicoRepository;
import com.labanta.servidorlocal.service.EmailService;
import com.labanta.servidorlocal.service.ExchangeService;
import com.labanta.servidorlocal.service.FileStorageService;
import com.labanta.servidorlocal.service.ServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoControler {

    private final ServicoRepository repository;
    private final ServicoService service; // Adicionado para resolver 'service'
    private final ExchangeService exchangeService;
    private final EmailService emailService;
    private final FileStorageService fileStorageService;

    public ServicoControler(ServicoRepository repository, ServicoService service, ExchangeService exchangeService,
            EmailService emailService, FileStorageService fileStorageService) {
        this.repository = repository;
        this.service = service;
        this.exchangeService = exchangeService;
        this.emailService = emailService;
        this.fileStorageService = fileStorageService;

    }

    @Operation(
            summary = "Lista todos os serviços",
            description = "Rota para listar todos os serviços existente na plataforma"
    )
    @GetMapping
    public Page<ServicoModel> listarTodos(
            @PageableDefault(page = 0 ,size = 5,sort = "id", direction = Sort.Direction.DESC)
            Pageable pageble
    )
    {
        return service.listarTodos(pageble);
    }

    @Operation(
            summary = "criar o novo servico",
            description = "Rota para criar um novo serviço"
    )
    @SecurityRequirement(name = "bearerAuth")

    @PostMapping
    public ServicoModel criarServico(@RequestBody ServicoModel novoservico) {
        return repository.save(novoservico);
    }

    @Operation(
            summary = "buscar o servico por id",
            description = "Rota para buscar um serviço por id"
    )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")

    public ServicoModel buscarServicoPorId(@PathVariable Long id) {
        return service.buscarServicoPorId(id);
    }

    @Operation(
            summary = "aplicar desconto nos servicos ativos",
            description = "Rota para aplicar desconto nos serviços ativos"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/aplicar-desconto")
    public List<ServicoModel> aplicarDesconto(@RequestParam Double percentagem) {
        List<ServicoModel> servicosAtualizados = service.aplicarDescontoEmAtivos(percentagem);
        return servicosAtualizados;
    }

    @Operation(
            summary = "pesquisar o servico",
            description = "Rota para pesquisar um serviço"
    )

    @GetMapping("/pesquisa")
    public List<ServicoModel> pesquisar(@RequestParam String termo) {
        return service.pesquisarServicos(termo);
    }

    @Operation(
            summary = "pedir orcamento",
            description = "Rota para pedir orcamento"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{id}/orcamento")
    public String pedirOrcamento(@PathVariable Long id,
            @RequestParam String emailDestino,
            @RequestParam(defaultValue = "CVE") String moeda) {

        // 1. Ir à Base de Dados buscar o Serviço
        ServicoModel servico = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // 2. Ir à Internet converter o preço (Aula 16)
        Double precoConvertido = exchangeService.converterPreco(servico.getPreco(), moeda);

        // 3. Enviar o resultado para o Gmail do cliente (Aula 15)
        emailService.enviarEmailOrcamento(emailDestino, servico.getTitulo(), precoConvertido, moeda);

        return "Orçamento calculado e enviado com sucesso para " + emailDestino + "!";
    }

    @Operation(
        summary = "Carregar capa de servico",
        description = "Rota para upload da capa do servico com id"
    )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(value = "/{id}/uploud-capa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String>uploadFile(@RequestParam MultipartFile file, @PathVariable Long id){

        ServicoModel servico = service.buscarServicoPorId(id);
        String fileUploaded = fileStorageService.StoreImages(file);

        servico.setImagemCapa(fileUploaded);
        service.criarServico(servico);

        return  ResponseEntity.ok("FICHEIRO CARREGADO COM SUCESSO:"+fileUploaded);
    }







}
