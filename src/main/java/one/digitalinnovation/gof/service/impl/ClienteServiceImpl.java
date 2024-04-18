package one.digitalinnovation.gof.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.repository.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.repository.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;


/**
 * Implementação da <b>Strategy</b> {@link ClienteService}, a qual pode ser
 * injetada pelo Spring (via {@link Autowired}). Com isso, como essa classe é um
 * {@link Service}, ela será tratada como um <b>Singleton</b>.
 *
 */
@Service
public class ClienteServiceImpl implements ClienteService {

	// Singleton: Injetar os componentes do Spring com @Autowired.
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	private EnderecoRepository enderecoRepository;
	@Autowired
	private ViaCepService viaCepService;
	
	// Strategy: Implementar os métodos definidos na interface.
	// Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

	@Override
	public Iterable<Cliente> buscarTodos() {
		// Buscar todos os Clientes.
		return clienteRepository.findAll();
	}

	@Override
	public Cliente buscarPorId(Long id) {
		// Buscar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if(cliente.isPresent())
		{
			return cliente.get();
		}
		else
		{
			throw new IllegalArgumentException("There is no client!");
		}
	}

	@Override
	public void inserir(Cliente cliente) {
		//Optional<Cliente> clienteToSave = clienteRepository.findByName(cliente.getNome());
		Optional<Cliente> clienteToSave = clienteRepository.findByNameAndCpf(cliente.getNome(), cliente.getCpf());
		System.out.println("-----------------------------------------------------------> " + clienteToSave);
		if(validaCpf(cliente)) {
			if(clienteToSave.isEmpty()){
				salvarClienteComCep(cliente);
			} else {
				throw new IllegalArgumentException("This client already exist");
			}
		} else {
			throw new IllegalArgumentException("This cpf is invalid");
		}
	}

	@Override
	public void atualizar(Long id, Cliente cliente) {
		// Buscar Cliente por ID, caso exista:
		Optional<Cliente> clienteBd = clienteRepository.findById(id);
		if (clienteBd.isPresent()) {
			if(validaCpf(cliente)){
				atualizarClienteExistente(cliente);
			} else {
				throw new IllegalArgumentException("This cpf is invalid");
			}
		} else {
			throw new IllegalArgumentException("This client does not exist");
		}
	}

	@Override
	public void deletar(Long id) {
		// Deletar Cliente por ID.
		Optional<Cliente> cliente = clienteRepository.findById(id);
		if(cliente.isPresent()) {
			clienteRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("This client does not exist");
		}
	}

	private void salvarClienteComCep(Cliente cliente) {
		// Verificar se o Endereco do Cliente já existe (pelo CEP).
		String cep = cliente.getEndereco().getCep();
		Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
			// Caso não exista, integrar com o ViaCEP e persistir o retorno.
			Endereco novoEndereco = viaCepService.consultarCep(cep);
			enderecoRepository.save(novoEndereco);
			return novoEndereco;
		});
		cliente.setEndereco(endereco);
		// Inserir Cliente, vinculando o Endereco (novo ou existente).
		clienteRepository.save(cliente);
	}
	private void atualizarClienteExistente(Cliente cliente) {
		// Verificar se o Cliente já existe pelo ID.
		Long clienteId = cliente.getId();
		if (clienteId == null || !clienteRepository.existsById(clienteId)) {
			throw new IllegalArgumentException("Client not found for update.");
		}
		// Atualizar o Cliente
		salvarClienteComCep(cliente);
	}

	private boolean validaCpf(Cliente cliente){
		String cpf = cliente.getCpf();
		boolean response = false;
		cpf = cpf.replaceAll("[^0-9]", "");

		try {
			int invalidcpf = 0;
			int digit1 = 0;
			int digit2 = 0;

			while (cpf.length() < 11) {
				cpf = "0" + cpf;
			}

			if (cpf.length() == 11) {
				for (int i = 1; i <= cpf.length(); i++) {
					if (cpf.charAt(0) == cpf.charAt(i - 1)) {
						invalidcpf++;
					}
					if (i < 10) {
						digit1 += Integer.parseInt(String.valueOf(cpf.charAt(i - 1))) * i;
					}
					if (i < 11) {
						digit2 += Integer.parseInt(String.valueOf(cpf.charAt(i - 1))) * (i - 1);
					}
				}

				if (invalidcpf == 11) {
					System.out.println("CPF inválido! " + cpf);
					return response;
				}

				digit1 = digit1 % 11;
				digit1 = (digit1 == 10) ? 0 : digit1;
				digit2 = digit2 % 11;
				digit2 = (digit2 == 10) ? 0 : digit2;

				if (Integer.parseInt(String.valueOf(cpf.charAt(9))) + Integer.parseInt(String.valueOf(cpf.charAt(10))) == (digit1 + digit2)) {
					System.out.println("CPF válido! " + cpf);
					response = true;
				} else {
					System.out.println("CPF inválido! " + cpf);
				}
			} else {
				System.out.println("CPF inválido! " + cpf);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return response;
	}
}
