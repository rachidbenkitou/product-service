package com.benkitoucoders.ecommerce.services.inter;

import com.benkitoucoders.ecommerce.dtos.ClientOrderDto;
import com.benkitoucoders.ecommerce.dtos.ResponseDto;
import com.itextpdf.text.DocumentException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ClientOrderService {
    List<ClientOrderDto> getClientOrdersByQuery(Long orderId, Long clientId, Long orderStatusId, LocalDateTime dateCreation, LocalDateTime dateUpdate) throws DocumentException, FileNotFoundException;

    List<ClientOrderDto> getClientOrdersByClientIdLong(String username);

    ClientOrderDto getClientOrderById(Long id);

    ClientOrderDto getClientOrderByClientId(Long username);

    ClientOrderDto addClientOrder(ClientOrderDto clientOrderDto) throws IOException;

    ClientOrderDto updateClientOrder(Long id, ClientOrderDto clientOrderDto);

    ResponseDto deleteClientOrderById(Long id);

    ClientOrderDto modifyClientOrderDtoStatusToAccepted(Long clientOrderId) throws DocumentException, FileNotFoundException;

    ClientOrderDto modifyClientOrderDtoStatusToReported(Long clientOrderId);

    ClientOrderDto modifyClientOrderDtoStatusToCancelled(Long clientOrderId);
}
