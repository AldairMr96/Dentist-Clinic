package com.company.bazar.serviceTest;
import com.company.bazar.model.Client;
import com.company.bazar.repository.IClientRepository;
import com.company.bazar.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


public class ClientServiceTest {

    @Mock //Simula el comportamiento del respository sin necesidad de interactuar con la db real
    private IClientRepository clientRepository;
    @InjectMocks //Inyecta el objeto para probar sus servicios
    private ClientService clientService;


    @BeforeEach
    // Prepara un objeto Client simulado antes de cada prueba.
    public void setUp() {
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public  void createClientTest() {

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        when(clientRepository.save(any())).thenReturn(client);
        clientService.createCLient(client);


        verify(clientRepository, times(1)).save(client);
    }
    @Test
    public void getClientErrorTest (){
        when(clientRepository.findAll()).thenReturn(Collections.emptyList());
        List<Client> clients = clientService.getClients();
        assertEquals(0, clients.size());
        assertTrue(clients.isEmpty());
    }
    @Test
    public void getClientTest(){
        List<Client> clients = List.of(
                new Client(1L, "Aldair", "Martinez", "123456789", new ArrayList<>()),
                new Client(2L, "Lina ", "Bermudez", "123456879", new ArrayList<>())
        );
        when(clientRepository.findAll()).thenReturn(clients);
        List<Client> result = clientService.getClients();
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

    @Test
    public  void editClientTest(){

        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        when(clientRepository.save(any())).thenReturn(client);
        clientService.editClient(client);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    public  void findClientErrorTest(){
        when(clientRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows( RuntimeException.class, ()-> clientService.findClient(1L));
    }
    @Test
    public  void findClientTest(){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());
        when(clientRepository.findById(any())).thenReturn(Optional.of(client));
        Client result = clientService.findClient(1L);

        assertEquals("Aldair", result.getNameClient());
        assertInstanceOf( Long.class, result.getIdClient());
        assertTrue(result.getSales().isEmpty());
    }
    @Test
    public  void deleteClientErrorTest (){
        when(clientRepository.existsById(any())).thenReturn(false);

        assertThrows(RuntimeException.class, ()->clientService.deleteClient(1L));
        verify(clientRepository, never()).deleteById(1L);
    }
    @Test
    public void deleteClientTest(){
        Client client = new Client(1L, "Aldair", "Martinez", "1234576", new ArrayList<>());

        when(clientRepository.existsById(any())).thenReturn(true);
        clientService.deleteClient(client.getIdClient());
        verify(clientRepository, times(1)).deleteById(client.getIdClient());

    }
}
