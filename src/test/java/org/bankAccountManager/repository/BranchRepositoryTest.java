package org.bankAccountManager.repository;

import org.bankAccountManager.entity.Branch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
public class BranchRepositoryTest {

    /*@Autowired
    private BranchRepository branchRepository;

    private Branch branch1;
    private Branch branch2;

    @BeforeEach
    public void setUp() {
        // Limpia la base de datos antes de cada prueba
        branchRepository.deleteAll();

        // Inicializa las sucursales de prueba
        branch1 = new Branch();
        branch1.setId(1L);
        branch1.setName("Main Branch");
        branch1.setAddress("123 Main St, Anytown, USA");
        branch1.setPhone("123-456-7890");

        branch2 = new Branch();
        branch2.setId(2L);
        branch2.setName("Second Branch");
        branch2.setAddress("456 Elm St, Othertown, USA");
        branch2.setPhone("987-654-3210");

        branchRepository.saveAll(Arrays.asList(branch1, branch2));
    }

    @Test
    public void testFindBranchById_Positive() {
        // Prueba positiva: Encontrar una sucursal por su ID
        Branch foundBranch = branchRepository.findBranchById(1);
        assertThat(foundBranch).isNotNull();
        assertThat(foundBranch.getName()).isEqualTo("Main Branch");
    }

    @Test
    public void testFindBranchById_Negative() {
        // Prueba negativa: Intentar encontrar una sucursal inexistente
        Branch foundBranch = branchRepository.findBranchById(999);
        assertThat(foundBranch).isNull();
    }

    @Test
    public void testExistsById_Positive() {
        // Prueba positiva: Verificar que una sucursal existe por su ID
        Boolean exists = branchRepository.existsById(1);
        assertThat(exists).isTrue();
    }

    @Test
    public void testExistsById_Negative() {
        // Prueba negativa: Verificar que una sucursal inexistente no existe
        Boolean exists = branchRepository.existsById(999);
        assertThat(exists).isFalse();
    }

    @Test
    public void testFindBranchByName_Positive() {
        // Prueba positiva: Encontrar una sucursal por su nombre
        Branch foundBranch = branchRepository.findBranchByName("Main Branch");
        assertThat(foundBranch).isNotNull();
        assertThat(foundBranch.getAddress()).isEqualTo("123 Main St, Anytown, USA");
    }

    @Test
    public void testFindBranchByName_Negative() {
        // Prueba negativa: Intentar encontrar una sucursal con un nombre inexistente
        Branch foundBranch = branchRepository.findBranchByName("Nonexistent Branch");
        assertThat(foundBranch).isNull();
    }

    @Test
    public void testFindAll_Positive() {
        // Prueba positiva: Obtener todas las sucursales
        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).isNotEmpty();
        assertThat(branches.size()).isEqualTo(2);
    }

    @Test
    public void testFindAll_Negative() {
        // Prueba negativa: Vaciar la base de datos y verificar
        branchRepository.deleteAll();
        List<Branch> branches = branchRepository.findAll();
        assertThat(branches).isEmpty();
    }

    @Test
    public void testSaveBranch_Positive() {
        // Prueba positiva: Guardar una nueva sucursal
        Branch newBranch = new Branch();
        newBranch.setId(3L);
        newBranch.setName("Third Branch");
        newBranch.setAddress("789 Pine St, Thirdtown, USA");
        newBranch.setPhone("111-222-3333");

        Branch savedBranch = branchRepository.save(newBranch);
        assertThat(savedBranch).isNotNull();
        assertThat(savedBranch.getId()).isEqualTo(3L);
    }

    @Test
    public void testSaveBranch_Negative() {
        // Prueba negativa: Intentar guardar una sucursal con campos faltantes
        Branch invalidBranch = new Branch();
        invalidBranch.setId(4L);
        invalidBranch.setPhone("999-888-7777"); // Falta el nombre y la dirección

        // Intenta guardar una sucursal inválida
        Branch savedBranch = branchRepository.save(invalidBranch);
        assertThat(savedBranch.getName()).isNull(); // Los campos faltantes quedan como null
    }*/
}
