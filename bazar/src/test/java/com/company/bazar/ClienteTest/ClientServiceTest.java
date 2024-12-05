package com.company.bazar.ClienteTest;
import com.company.bazar.model.Client;
import com.company.bazar.repository.IClientRepository;
import com.company.bazar.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock //Simula el comportamiento del respository sin necesidad de interactuar con la db real
    private IClientRepository clientRepository;
    @InjectMocks //Inyecta el objeto para probar sus servicios
    private ClientService clientService;

    private Client mockClient;
    @BeforeEach
    // Prepara un objeto Client simulado antes de cada prueba.
    public void setUp() {

        mockClient = new Client();
        mockClient.setIdClient(1L);
        mockClient.setNameClient("John");
        mockClient.setLastnameClient("Doe");
    }
    @Test
    void createClient_ShouldSaveClient() {
        // Llamar al método bajo prueba
        clientService.createCLient(mockClient);

        // Verificar que el repositorio se llamó correctamente
        verify(clientRepository, times(1)).save(mockClient);
    }
}
