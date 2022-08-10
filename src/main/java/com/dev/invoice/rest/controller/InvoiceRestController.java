package com.dev.invoice.rest.controller;

import com.dev.invoice.rest.entity.Invoice;
import com.dev.invoice.rest.exception.InvoiceNotFoundException;
import com.dev.invoice.rest.service.IInvoiceService;
import com.dev.invoice.rest.util.InvoiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceRestController {

    @Autowired
    private IInvoiceService invoiceService;

    @Autowired
    private InvoiceUtil util;

    /**
     * Takes Invoice Object as input and returns save Status as ResponseEntity<String>
     */

    @PostMapping("/invoices")
    public ResponseEntity<String> saveInvoice(@RequestBody Invoice invoice){
        ResponseEntity<String> responseEntity = null;
        try {
            Long id = invoiceService.saveInvoice(invoice);
            responseEntity = new ResponseEntity<String>(
                    "Invoice '"+id+"' created", HttpStatus.CREATED); //201-created
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to save invoice",HttpStatus.INTERNAL_SERVER_ERROR); //500-Internal Server Error
        }
        return  responseEntity;
    }

    /**
     * To retrieve all Invoices, returns data retrieval Status as ResponseEntity<?>
     */
    @GetMapping("/invoices")
    public ResponseEntity<?> getAllInvoices(){
        ResponseEntity<?> responseEntity = null;
        try{
            List<Invoice> invoices = invoiceService.getAllInvoice();
            responseEntity = new ResponseEntity<List<Invoice>>(
                    invoices,HttpStatus.OK
            );
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to get all invoices",HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return responseEntity;
    }

    /**
     * To retrieve one Invoice by providing id, returns Invoice object & Status as ResponseEntity<?>
     */
    @GetMapping("/invoices/{id}")
    public ResponseEntity<?> getOneInvoice(@PathVariable Long id){
        ResponseEntity<?> responseEntity = null;
        try{
            Invoice invoice = invoiceService.getOneInvoice(id);
            responseEntity = new ResponseEntity<Invoice>(
                    invoice,HttpStatus.OK
            );
        }catch (InvoiceNotFoundException notFoundException){
            throw notFoundException;
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to find Invoice", HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        return responseEntity;
    }

    /**
     * To delete one Invoice by providing id, returns Status as ResponseEntity<String>
     */
    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<String> deleteInvoice(@PathVariable Long id){
        ResponseEntity<String> responseEntity = null;
        try {
            invoiceService.deleteInvoice(id);
            responseEntity = new ResponseEntity<String>(
                    "Invoice '"+id+"' deleted",HttpStatus.OK);
        }catch (InvoiceNotFoundException notFoundException){
            throw notFoundException;
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to delete Invoice ",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    /**
     * To modify one Invoice by providing id, updates Invoice object & returns Status as ResponseEntity<String>
     */
    @PutMapping("/invoices/{id}")
    public ResponseEntity<String> updateInvoice(@PathVariable Long id,@RequestBody Invoice invoice){
        ResponseEntity<String> responseEntity = null;
        try {
            //db Object
            Invoice inv = invoiceService.getOneInvoice(id);
            //copy non-null values from request to Database object
            util.copyNonNullValues(invoice,inv);
            //finally update this object
            invoiceService.updateInvoice(inv);
            responseEntity = new ResponseEntity<String>(
                    "Invoice ID '"+id+"' updated",HttpStatus.OK); //205- Reset-Content(PUT)
        }catch (InvoiceNotFoundException notFoundException){
            throw notFoundException;
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to update Invoice",HttpStatus.INTERNAL_SERVER_ERROR
            ); //500-ISE
        }
        return responseEntity;
    }

    /**
     * To update one Invoice just like where clause condition, updates Invoice object & returns Status as ResponseEntity<String>
     */
    @PatchMapping("/invoices/{id}/{number}")
    public ResponseEntity<String> updateInvoiceNumberById(
            @PathVariable Long id,
            @PathVariable String number
    )
    {
        ResponseEntity<String> responseEntity = null;
        try{
            invoiceService.updateInvoiceNumberById(number,id);
            responseEntity = new ResponseEntity<String>(
                    "Invoice Id '"+id+"' Updated",HttpStatus.OK
            );
        }catch (InvoiceNotFoundException notFoundException){
            throw notFoundException; // re-throw exception to handler
        }catch (Exception e){
            e.printStackTrace();
            responseEntity = new ResponseEntity<String>(
                    "Unable to update Invoice",HttpStatus.INTERNAL_SERVER_ERROR
            );//500-ISE
        }
        return responseEntity;
    }
}
