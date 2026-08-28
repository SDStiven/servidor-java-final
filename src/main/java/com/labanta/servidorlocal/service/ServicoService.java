package com.labanta.servidorlocal.service;

import com.labanta.servidorlocal.exception.ServicoNaoEmcontradoException;
import com.labanta.servidorlocal.model.ServicoModel;
import com.labanta.servidorlocal.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ServicoService {

    private final ServicoRepository repositorio;

    // construtor para injeção de dependência
    public ServicoService(ServicoRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Listar serviços
    public Page<ServicoModel> listarTodos(Pageable pageable) {
        return repositorio.findAll(pageable);
    }

    // apply discount to active services
    public List<ServicoModel> aplicarDescontoEmAtivos(Double percentagem) {
        List<ServicoModel> listaAtivos = repositorio.findByEstaAtivoTrue();

        for (ServicoModel s : listaAtivos) {
            System.out.println(s.getTitulo());
        }

        for (ServicoModel s : listaAtivos) {
            if (s.getPreco() >= 10000.0) {
                System.out.println(s.getPreco());
                Double precoComDesconto = s.getPreco() * (1 - (percentagem / 100));
                System.out.println(precoComDesconto);
                s.setPrecoComDesconto(precoComDesconto);
            }
        }

        for (ServicoModel s : listaAtivos) {
            System.out.println(s.getPrecoComDesconto());
        }

        return repositorio.saveAll(listaAtivos);
    }


    //criar servico
    public ServicoModel criarServico(ServicoModel servico) {
        return repositorio.save(servico);
    }

    //buscar servico por id
    public ServicoModel buscarServicoPorId(Long id) {

        return repositorio.findById(id)
                .orElseThrow(() -> new ServicoNaoEmcontradoException(
                        "O serviço com o ID " + id + " não existe no catálogo."));
    }

    // search services by title
    public List<ServicoModel> pesquisarServicos(String termo) {
        return repositorio.findByTituloContainingIgnoreCase(termo);
    }
}
