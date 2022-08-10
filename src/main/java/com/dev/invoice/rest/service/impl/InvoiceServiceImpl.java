package com.dev.invoice.rest.service.impl;

import com.dev.invoice.rest.entity.Invoice;
import com.dev.invoice.rest.exception.InvoiceNotFoundException;
import com.dev.invoice.rest.repo.InvoiceRepository;
import com.dev.invoice.rest.service.IInvoiceService;
import com.dev.invoice.rest.util.InvoiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements IInvoiceService {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceUtil invoiceUtil;

    @Override
    public Long saveInvoice(Invoice invoice) {
        invoiceUtil.CalculateFinalAmountIncludingGST(invoice);
        Long id = invoiceRepository.save(invoice).getId();
        return id;
    }

    @Override
    public void updateInvoice(Invoice invoice) {
        invoiceUtil.CalculateFinalAmountIncludingGST(invoice);
        invoiceRepository.save(invoice);

    }

    @Override
    public void deleteInvoice(Long id) {
        Invoice invoice = getOneInvoice(id);
        invoiceRepository.delete(invoice);
    }

    public Optional<Invoice> getSingleInvoice(Long Id) {
        return invoiceRepository.findById(Id);
    }

    @Override
    public Invoice getOneInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(()-> new InvoiceNotFoundException(
                        new StringBuffer().append("Product '")
                        .append(id)
                        .append("' not exist")
                        .toString()
                ));
        return invoice;
    }

    @Override
    public List<Invoice> getAllInvoice() {
        List<Invoice> invoices = invoiceRepository.findAll();
        invoices.sort((ob1,ob2)->ob1.getId().intValue() - ob2.getId().intValue());
        return invoices;
    }

    @Override
    public boolean isInvoiceExist(Long id) {
        return invoiceRepository.existsById(id);
    }

    @Override
    public Integer updateInvoiceNumberById(String number, Long id) {
        return null;
    }
}
