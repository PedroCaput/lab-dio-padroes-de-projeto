package one.digitalinnovation.gof.repository;

import one.digitalinnovation.gof.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @Query("SELECT c FROM tb_clientes c WHERE c.nome = :nome")
    Optional<Cliente> findByName(@Param("nome") String nome);

    @Query("SELECT c FROM tb_clientes c WHERE c.nome = :nome AND c.cpf = :cpf")
    Optional<Cliente> findByNameAndCpf(@Param("nome") String nome, @Param("cpf") String cpf);

}