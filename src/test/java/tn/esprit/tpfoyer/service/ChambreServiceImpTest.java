package tn.esprit.tpfoyer.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.tpfoyer.ChambreServiceImpl;
import tn.esprit.tpfoyer.entity.Chambre;
import tn.esprit.tpfoyer.entity.TypeChambre;
import tn.esprit.tpfoyer.repository.ChambreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceImpTest {
    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreServiceImpl chambreService;

    private Chambre chambre;
    private List<Chambre> chambreList;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);

        Chambre chambre2 = new Chambre();
        chambre2.setIdChambre(2L);
        chambre2.setNumeroChambre(102);
        chambre2.setTypeC(TypeChambre.DOUBLE);

        chambreList = Arrays.asList(chambre, chambre2);
    }

    // Test pour la méthode addChambre
    @Test
    void testAddChambre() {
        // Arrange
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre createdChambre = chambreService.addChambre(chambre);

        // Assert
        assertNotNull(createdChambre);
        assertEquals(chambre.getNumeroChambre(), createdChambre.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    // Test pour la méthode retrieveChambre
    @Test
    void testRetrieveChambre() {
        // Arrange
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        // Act
        Chambre retrievedChambre = chambreService.retrieveChambre(1L);

        // Assert
        assertNotNull(retrievedChambre);
        assertEquals(101, retrievedChambre.getNumeroChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    // Test pour la méthode retrieveAllChambres
    @Test
    void testRetrieveAllChambres() {
        // Arrange
        when(chambreRepository.findAll()).thenReturn(chambreList);

        // Act
        List<Chambre> result = chambreService.retrieveAllChambres();

        // Assert
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    // Test pour la méthode modifyChambre
    @Test
    void testModifyChambre() {
        // Arrange
        chambre.setNumeroChambre(202);
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        // Act
        Chambre modifiedChambre = chambreService.modifyChambre(chambre);

        // Assert
        assertNotNull(modifiedChambre);
        assertEquals(202, modifiedChambre.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    // Test pour la méthode removeChambre
    @Test
    void testRemoveChambre() {
        // Act
        chambreService.removeChambre(1L);

        // Assert
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    // Test pour la méthode recupererChambresSelonTyp
    @Test
    void testRecupererChambresSelonTyp() {
        // Arrange
        when(chambreRepository.findAllByTypeC(TypeChambre.SIMPLE)).thenReturn(Arrays.asList(chambre));

        // Act
        List<Chambre> result = chambreService.recupererChambresSelonTyp(TypeChambre.SIMPLE);

        // Assert
        assertEquals(1, result.size());
        assertEquals(TypeChambre.SIMPLE, result.get(0).getTypeC());
        verify(chambreRepository, times(1)).findAllByTypeC(TypeChambre.SIMPLE);
    }

    // Test pour la méthode trouverchambreSelonEtudiant
    @Test
    void testTrouverchambreSelonEtudiant() {
        // Arrange
        when(chambreRepository.trouverChselonEt(123456L)).thenReturn(chambre);

        // Act
        Chambre result = chambreService.trouverchambreSelonEtudiant(123456L);

        // Assert
        assertNotNull(result);
        assertEquals(101, result.getNumeroChambre());
        verify(chambreRepository, times(1)).trouverChselonEt(123456L);
    }


}
