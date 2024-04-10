package com.benkitoucoders.ecommerce.services.pdfs;

import com.benkitoucoders.ecommerce.dtos.*;
import com.benkitoucoders.ecommerce.services.inter.ClientOrderDetailsService;
import com.benkitoucoders.ecommerce.services.inter.ClientService;
import com.benkitoucoders.ecommerce.services.inter.EmailService;
import com.benkitoucoders.ecommerce.services.inter.SaleDetailsService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveredOrderStatement {
    private final ClientOrderDetailsService clientOrderDetailsService;
    private final SaleDetailsService saleDetailsService;
    private final EmailService emailService;
    private final ClientService clientService;
    double totalPrice = 0;

    private static final String FILE = "C:\\Users\\PC\\Desktop\\Projects\\1-Ecommerce\\deliveredOrderStatement.pdf";

    public void generateDeliveredOrderStatement(Long orderId, ClientOrderDto clientOrderDto, SaleDto saleDto, boolean isClientOrder) throws FileNotFoundException, DocumentException {
        if (isClientOrder) {
            List<ClientOrderDetailsDto> clientOrderDetails = clientOrderDetailsService.getClientOrderDetailsByQuery(clientOrderDto.getId());
            ClientDto clientDto = clientService.getClientById(clientOrderDto.getClientId());

            // Create PDF document
            Document document = new Document(PageSize.A4);
            OutputStream outputStream = new FileOutputStream(FILE);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Define colors
            BaseColor black = new BaseColor(0, 0, 0);
            BaseColor white = new BaseColor(255, 255, 255);
            BaseColor gray = new BaseColor(128, 128, 128);

            // Define fonts
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, black);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, white);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, black);

            // Set margins
            document.setMargins(36, 36, 36, 36);

            // Open document
            document.open();

            // Add company name
            Paragraph companyName = new Paragraph("BOKEITO SHOP", headingFont);
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setSpacingAfter(10f);
            document.add(companyName);

            // Add spacing between address and user info table and transactions table
            document.add(Chunk.NEWLINE); // Add a line break for more space

//**********************************************************
// Add address and user info
            PdfPTable addressAndUserInfoTable = new PdfPTable(2);
            addressAndUserInfoTable.setWidthPercentage(100);
            addressAndUserInfoTable.setWidths(new float[]{1, 1});
            addressAndUserInfoTable.setHorizontalAlignment(Element.ALIGN_CENTER);

// Set table border color and width
            addressAndUserInfoTable.getDefaultCell().setBorderColor(BaseColor.BLACK);
            addressAndUserInfoTable.getDefaultCell().setBorderWidth(1f);

            PdfPCell addressCell = new PdfPCell();
            Paragraph addressAndDate = new Paragraph();
            addressAndDate.add(new Phrase(clientDto.getAddress(), normalFont));
            addressAndDate.add(Chunk.NEWLINE);
            addressAndDate.add(new Phrase("Date: " + LocalDate.now(), normalFont));
            addressAndDate.setAlignment(Element.ALIGN_CENTER); // Align paragraph content to the center
            addressCell.addElement(addressAndDate);
            addressCell.setPadding(5); // Add padding of 5 points to the cell

// addressCell.setBorder(Rectangle.NO_BORDER);
// Remove individual cell border
            addressAndUserInfoTable.addCell(addressCell);

            PdfPCell userInfoCell = new PdfPCell();
            userInfoCell.setPadding(5); // Add padding of 5 points to the cell
            Paragraph userInfoParagraph = new Paragraph();
            userInfoParagraph.add(new Phrase(clientDto.getFirstName() + " " + clientDto.getLastName(), normalFont));
            userInfoParagraph.add(Chunk.NEWLINE);
            userInfoParagraph.add(new Phrase(clientDto.getEmail(), normalFont));
            userInfoParagraph.add(Chunk.NEWLINE);
            userInfoParagraph.add(new Phrase(clientDto.getPhoneNumber(), normalFont));
            userInfoParagraph.setAlignment(Element.ALIGN_CENTER); // Align paragraph content to the center
            userInfoCell.addElement(userInfoParagraph);


            addressAndUserInfoTable.addCell(userInfoCell);

// Add spacing between address and user info
            addressAndUserInfoTable.setSpacingAfter(13f); // Add space after the table

// Center the table horizontally
            Paragraph centerTable = new Paragraph();
            centerTable.add(addressAndUserInfoTable);
            centerTable.setAlignment(Element.ALIGN_CENTER);
            document.add(centerTable);


//**********************************************************


// Add spacing between address and user info table and transactions table
            document.add(Chunk.NEWLINE); // Add a line break for more space

            // Add transactions table
            PdfPTable transactionsTable = new PdfPTable(4);
            transactionsTable.setWidthPercentage(100);
            transactionsTable.setWidths(new float[]{2, 3, 2, 2});

            PdfPCell cell;

// Add table headers
            cell = new PdfPCell(new Phrase("Client Name", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Product Name", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Price", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Quantity", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

// Add transaction details
            for (ClientOrderDetailsDto clientOrderDetailsDto : clientOrderDetails) {
                transactionsTable.addCell(new Phrase(clientDto.getFirstName() + " " + clientDto.getLastName(), normalFont));
                transactionsTable.addCell(new Phrase(clientOrderDetailsDto.getProductName(), normalFont));
                transactionsTable.addCell(new Phrase(String.valueOf(clientOrderDetailsDto.getPrice() + " MAD"), normalFont));
                transactionsTable.addCell(new Phrase(String.valueOf(clientOrderDetailsDto.getQuantity()), normalFont));

                totalPrice += clientOrderDetailsDto.getPrice();
            }

            DecimalFormat df = new DecimalFormat("#.00");
            String formattedTotalPrice = df.format(totalPrice);

// Add single row for total
            cell = new PdfPCell(new Phrase("TOTAL", boldFont));
            cell.setColspan(2);
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase(formattedTotalPrice + " MAD", boldFont)); // Assuming total is 1000
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(); // Empty cell for the status
            cell.setBorderColor(white);
            transactionsTable.addCell(cell);

            document.add(transactionsTable);

            // Close document
            document.close();

            // Send email with attachment
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(clientDto.getEmail())
                    .subject("INVOICE FOR ORDER NUMBER: " + clientOrderDto.getId())
                    .messageBody("YOUR ORDER IS DELIVERED, YOU WELL BE CONTACTED BY THE DELIVERY PERSON.\n THANK YOU FOR YOUR TRUST!")
                    .attachment(FILE)
                    .build();

            emailService.sendEmailWithAttachment(emailDetails);
            totalPrice = 0;
        } else {
            List<SaleDetailsDto> saleDetailsDtos = saleDetailsService.findAllSaleDetailsById(saleDto.getId());

            // Create PDF document
            Document document = new Document(PageSize.A4);
            OutputStream outputStream = new FileOutputStream(FILE);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            // Define colors
            BaseColor black = new BaseColor(0, 0, 0);
            BaseColor white = new BaseColor(255, 255, 255);
            BaseColor gray = new BaseColor(128, 128, 128);

            // Define fonts
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, black);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, white);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, black);

            // Set margins
            document.setMargins(36, 36, 36, 36);

            // Open document
            document.open();

            // Add company name
            Paragraph companyName = new Paragraph("BOKEITO SHOP", headingFont);
            companyName.setAlignment(Element.ALIGN_CENTER);
            companyName.setSpacingAfter(10f);
            document.add(companyName);

            // Add spacing between address and user info table and transactions table
            document.add(Chunk.NEWLINE); // Add a line break for more space

//**********************************************************
// Add address and user info
            PdfPTable addressAndUserInfoTable = new PdfPTable(2);
            addressAndUserInfoTable.setWidthPercentage(100);
            addressAndUserInfoTable.setWidths(new float[]{1, 1});
            addressAndUserInfoTable.setHorizontalAlignment(Element.ALIGN_CENTER);

// Set table border color and width
            addressAndUserInfoTable.getDefaultCell().setBorderColor(BaseColor.BLACK);
            addressAndUserInfoTable.getDefaultCell().setBorderWidth(1f);

            PdfPCell addressCell = new PdfPCell();
            Paragraph addressAndDate = new Paragraph();
            addressAndDate.add(new Phrase(saleDto.getAddress(), normalFont));
            addressAndDate.add(Chunk.NEWLINE);
            addressAndDate.add(new Phrase("Date: " + LocalDate.now(), normalFont));
            addressAndDate.setAlignment(Element.ALIGN_CENTER); // Align paragraph content to the center
            addressCell.addElement(addressAndDate);
            addressCell.setPadding(5); // Add padding of 5 points to the cell

// addressCell.setBorder(Rectangle.NO_BORDER);
// Remove individual cell border
            addressAndUserInfoTable.addCell(addressCell);

            PdfPCell userInfoCell = new PdfPCell();
            userInfoCell.setPadding(5); // Add padding of 5 points to the cell
            Paragraph userInfoParagraph = new Paragraph();
            userInfoParagraph.add(new Phrase(saleDto.getFullName(), normalFont));
            userInfoParagraph.add(Chunk.NEWLINE);
            userInfoParagraph.add(new Phrase(saleDto.getEmail(), normalFont));
            userInfoParagraph.add(Chunk.NEWLINE);
            userInfoParagraph.add(new Phrase(saleDto.getPhone(), normalFont));
            userInfoParagraph.setAlignment(Element.ALIGN_CENTER); // Align paragraph content to the center
            userInfoCell.addElement(userInfoParagraph);


            addressAndUserInfoTable.addCell(userInfoCell);

// Add spacing between address and user info
            addressAndUserInfoTable.setSpacingAfter(13f); // Add space after the table

// Center the table horizontally
            Paragraph centerTable = new Paragraph();
            centerTable.add(addressAndUserInfoTable);
            centerTable.setAlignment(Element.ALIGN_CENTER);
            document.add(centerTable);


//**********************************************************


// Add spacing between address and user info table and transactions table
            document.add(Chunk.NEWLINE); // Add a line break for more space

            // Add transactions table
            PdfPTable transactionsTable = new PdfPTable(4);
            transactionsTable.setWidthPercentage(100);
            transactionsTable.setWidths(new float[]{2, 3, 2, 2});

            PdfPCell cell;

// Add table headers
            cell = new PdfPCell(new Phrase("Client Name", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Product Name", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Price", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase("Quantity", boldFont));
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

// Add transaction details
            for (SaleDetailsDto saleDetailsDto : saleDetailsDtos) {
                transactionsTable.addCell(new Phrase(saleDto.getFullName(), normalFont));
                transactionsTable.addCell(new Phrase(saleDetailsDto.getProductName(), normalFont));
                transactionsTable.addCell(new Phrase(String.valueOf(saleDetailsDto.getPrice() + " MAD"), normalFont));
                transactionsTable.addCell(new Phrase(String.valueOf(saleDetailsDto.getQuantity()), normalFont));

                totalPrice += saleDetailsDto.getPrice();
            }

            DecimalFormat df = new DecimalFormat("#.00");
            String formattedTotalPrice = df.format(totalPrice);

// Add single row for total
            cell = new PdfPCell(new Phrase("TOTAL", boldFont));
            cell.setColspan(2);
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(new Phrase(formattedTotalPrice + " MAD", boldFont)); // Assuming total is 1000
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(black);
            cell.setBorderColor(white);
            transactionsTable.addCell(cell);

            cell = new PdfPCell(); // Empty cell for the status
            cell.setBorderColor(white);
            transactionsTable.addCell(cell);

            document.add(transactionsTable);

            // Close document
            document.close();

            // Send email with attachment
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(saleDto.getEmail())
                    .subject("INVOICE FOR ORDER NUMBER: " + clientOrderDto.getId())
                    .messageBody("YOUR ORDER IS DELIVERED, YOU WELL BE CONTACTED BY THE DELIVERY PERSON.\n \nTHANK YOU FOR YOUR TRUST!")
                    .attachment(FILE)
                    .build();

            emailService.sendEmailWithAttachment(emailDetails);
            totalPrice = 0;
        }
    }
}
