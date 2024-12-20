package org.bankAccountManager.service;

import org.bankAccountManager.entity.Branch;
import org.bankAccountManager.repository.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BranchServiceTest {

    /*@Mock
    private BranchRepository branchRepository;

    @InjectMocks
    private BranchServiceImplementation branchService;

    private Branch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        branch = new Branch(1, "Main Branch", "123 Main St, Anytown, USA", "123-456-7890");
    }

    @Test
    void testCreateBranch_success() {
        // Arrange
        when(branchRepository.existsById(branch.getId())).thenReturn(false);
        when(branchRepository.save(any(Branch.class))).thenReturn(branch);

        // Act
        Branch createdBranch = branchService.createBranch(branch);

        // Assert
        assertNotNull(createdBranch);
        assertEquals(branch.getId(), createdBranch.getId());
        assertEquals(branch.getName(), createdBranch.getName());
        verify(branchRepository, times(1)).save(branch);
    }

    @Test
    void testCreateBranch_branchExists_throwsException() {
        // Arrange
        when(branchRepository.existsById(branch.getId())).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            branchService.createBranch(branch);
        });

        assertEquals("Branch already exists", exception.getMessage());
        verify(branchRepository, never()).save(branch);
    }

    @Test
    void testGetBranchById_success() {
        // Arrange
        when(branchRepository.findBranchById(branch.getId())).thenReturn(branch);

        // Act
        Branch foundBranch = branchService.getBranchById(branch.getId());

        // Assert
        assertNotNull(foundBranch);
        assertEquals(branch.getId(), foundBranch.getId());
        verify(branchRepository, times(1)).findBranchById(branch.getId());
    }

    @Test
    void testGetBranchById_notFound() {
        // Arrange
        when(branchRepository.findBranchById(branch.getId())).thenReturn(null);

        // Act
        Branch foundBranch = branchService.getBranchById(branch.getId());

        // Assert
        assertNull(foundBranch);
        verify(branchRepository, times(1)).findBranchById(branch.getId());
    }

    @Test
    void testGetBranchByName_success() {
        // Arrange
        when(branchRepository.findBranchByName(branch.getName())).thenReturn(branch);

        // Act
        Branch foundBranch = branchService.getBranchByName(branch.getName());

        // Assert
        assertNotNull(foundBranch);
        assertEquals(branch.getName(), foundBranch.getName());
        verify(branchRepository, times(1)).findBranchByName(branch.getName());
    }

    @Test
    void testGetAllBranches() {
        // Arrange
        when(branchRepository.findAll()).thenReturn(List.of(branch));

        // Act
        List<Branch> branches = branchService.getAllBranches();

        // Assert
        assertNotNull(branches);
        assertFalse(branches.isEmpty());
        verify(branchRepository, times(1)).findAll();
    }

    @Test
    void testUpdateBranch() {
        // Arrange
        when(branchRepository.save(branch)).thenReturn(branch);

        // Act
        Branch updatedBranch = branchService.updateBranch(branch);

        // Assert
        assertNotNull(updatedBranch);
        assertEquals(branch.getId(), updatedBranch.getId());
        verify(branchRepository, times(1)).save(branch);
    }

    @Test
    void testDeleteBranch() {
        // Act
        branchService.deleteBranch(branch);

        // Assert
        verify(branchRepository, times(1)).delete(branch);
    }*/
}